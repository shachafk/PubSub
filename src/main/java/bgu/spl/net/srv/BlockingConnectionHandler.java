package bgu.spl.net.srv;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.messagebroker.Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final StompMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private LogManager logM = LogManager.getInstance();
    private ConnectionsImpl connections;
    private int connectionID;
    private Client activeUser;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, StompMessagingProtocol<T> protocol,ConnectionsImpl connections, int connectionID, Client activeClient) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections = connections;
        this.connectionID = connectionID;
        this.activeUser = activeClient;
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
                nextMessage.addNextInput((String) encdec.decodeNextByte((byte) read));
            }

            if (nextMessage.getHeader().size()>0) {
                this.connections.addMsgPerclient(nextMessage,activeClient);
                protocol.process((T) nextMessage); //should send the response
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

    public void setActiveUser(Client activeClient) {
        this.activeUser = activeClient;
    }
}
