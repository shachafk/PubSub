package bgu.spl.net.srv;

import bgu.spl.net.api.Message;
import bgu.spl.net.PassiveObjects.User;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void send(String channel, T msg);

    void disconnect(int connectionId);
     User getClientByMsg(Message m);
}
