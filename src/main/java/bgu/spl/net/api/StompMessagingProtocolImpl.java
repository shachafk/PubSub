package bgu.spl.net.api;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;
import bgu.spl.net.srv.Connections;
import java.io.Serializable;
import java.util.function.Supplier;

public class StompMessagingProtocolImpl<T> implements StompMessagingProtocol<Serializable>, Supplier<MessagingProtocol<T>> {
    private MessageBroker messageBroker;
    private Client client;
    private int connectionid;
    private Connections connections;
    private boolean terminate;

    public StompMessagingProtocolImpl(MessageBroker a){
        this.messageBroker = a;
    }

    public void start(int connectionId, Connections<String> connections) {
        this.connectionid = connectionId;
        this.connections = connections;
    }
    @Override
    public void process(String message) {
    }
    @Override
    public boolean shouldTerminate() {
        if (this.terminate ==true){
            connections.disconnect(connectionid);
        }
        return this.terminate;
    }

    @Override
    public MessagingProtocol<T> get() {
        return null;
    }
}
