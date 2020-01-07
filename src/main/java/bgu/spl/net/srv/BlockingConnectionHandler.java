package bgu.spl.net.srv;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.PassiveObjects.User;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final StompMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private LogManager logM = LogManager.getInstance();
    private ConnectionsImpl connections=ConnectionsImpl.getInstance();
    private int connectionID;
    private User activeUser;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, StompMessagingProtocol<T> protocol,int connectionID, User activeUser) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connectionID = connectionID;
        this.activeUser = activeUser;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            protocol.start(connectionID,connections);
            out = new BufferedOutputStream(sock.getOutputStream());
            Message nextMessage = new Message();
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                String toAdd= (String)encdec.decodeNextByte((byte) read);
                if (toAdd!=null) {
                    nextMessage.addNextInput(toAdd);
                }
                if (nextMessage.isEndOfMsg()){
                    Message readyMsg = nextMessage;
                    System.out.println(readyMsg.toString());
                        this.connections.addMsgPerclient(readyMsg, activeUser);
                        protocol.process((T) readyMsg); //should send the response

                    nextMessage.clear();
                }
            }




        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg) throws IOException {
           out=new BufferedOutputStream(sock.getOutputStream());
           byte[] tmp=encdec.encode(msg);
           out.write(tmp);
           out.flush();

    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
    public boolean statusOk(){ return sock.isConnected();}
}
