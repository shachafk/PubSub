package bgu.spl.net.srv;

import bgu.spl.net.api.Message;
import bgu.spl.net.messagebroker.Client;

import java.io.IOException;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void send(String channel, T msg);

    void disconnect(int connectionId);
     Client getClientByMsg(Message m);
}
