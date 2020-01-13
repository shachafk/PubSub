package bgu.spl.net.srv;

import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NonBlockingConnectionHandler<T> implements ConnectionHandler<T> {

    private static final int BUFFER_ALLOCATION_SIZE = 1 << 13; //8k
    private static final ConcurrentLinkedQueue<ByteBuffer> BUFFER_POOL = new ConcurrentLinkedQueue<>();

    private final StompMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    private final SocketChannel chan;
    private final Reactor reactor;
    private ConnectionsImpl connections=ConnectionsImpl.getInstance();
    private User activeUser;
    private int connectionID;






    public NonBlockingConnectionHandler(
            MessageEncoderDecoder<T> reader,
            StompMessagingProtocol<T> protocol,
            SocketChannel chan,
            Reactor reactor,int connectionID, User activeUser) {
        this.chan = chan;
        this.encdec = reader;
        this.protocol = protocol;
        this.reactor = reactor;
        this.connectionID = connectionID;
        this.activeUser = activeUser;
        this.protocol.start(this.connectionID,connections);

    }

    public Runnable continueRead() {
        ByteBuffer buf = leaseBuffer();

        boolean success = false;
        try {
            success = chan.read(buf) != -1;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (success) {
            buf.flip();
            return () -> {
                try {
                    Message nextMessage = new Message();

                    while (buf.hasRemaining()) {
                        String toAdd= (String) encdec.decodeNextByte(buf.get());
                        if (toAdd!=null) {
                            nextMessage.addNextInput(toAdd);
                        }
                        if (nextMessage.isEndOfMsg()){
                            Message readyMsg = nextMessage;
//                            System.out.println(readyMsg.toString());
                            this.connections.addMsgPerclient(readyMsg, activeUser);
                            protocol.process((T) readyMsg); //should send the response

                            nextMessage.clear();
                        }
//                        T nextMessage = encdec.decodeNextByte(buf.get());
//                        if (nextMessage != null) {
                            //protocol.process(nextMessage); //should send the msg
//                            if (response != null) {
//                                writeQueue.add(ByteBuffer.wrap(encdec.encode(response)));
//                                reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
//                            }
                        }

                } finally {
                    releaseBuffer(buf);
                }
            };
        } else {
            releaseBuffer(buf);
            close();
            return null;
        }

    }

    public void close() {
        try {
            chan.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isClosed() {
        return !chan.isOpen();
    }

    public void continueWrite() {
        while (!writeQueue.isEmpty()) {
            try {
                ByteBuffer top = writeQueue.peek();
                chan.write(top);
                if (top.hasRemaining()) {
                    return;
                } else {
                    writeQueue.remove();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                close();
            }
        }

        if (writeQueue.isEmpty()) {
            if (protocol.shouldTerminate()) close();
            else reactor.updateInterestedOps(chan, SelectionKey.OP_READ);
        }
    }

    private static ByteBuffer leaseBuffer() {
        ByteBuffer buff = BUFFER_POOL.poll();
        if (buff == null) {
            return ByteBuffer.allocateDirect(BUFFER_ALLOCATION_SIZE);
        }

        buff.clear();
        return buff;
    }

    private static void releaseBuffer(ByteBuffer buff) {
        BUFFER_POOL.add(buff);
    }

    @Override
    public void send(T msg) {
      writeQueue.add(ByteBuffer.wrap(encdec.encode(msg)));
      reactor.updateInterestedOps(chan,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

//    public boolean statusOk() {
//        return false;
//    }
}
