package bgu.spl.net.srv;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final MessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private LogManager logM = LogManager.getInstance();

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, MessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());
            Message nextMessage = new Message();
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                nextMessage.addNextInput((String) encdec.decodeNextByte((byte) read));
            }

            if (nextMessage.getHeader().size()>0) {
                T response = protocol.process((T) nextMessage);
                if (response != null) {
                    out.write(encdec.encode(response));
                    out.flush();
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
}
