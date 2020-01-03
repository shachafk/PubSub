package bgu.spl.net.srv;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;

public class ConnectionImpl implements Connections {
    private HashMap<Integer,ConnectionHandler> activeUsers = new HashMap<>();
    private LogManager logM = LogManager.getInstance();
    private MessageBroker messageBroker = MessageBroker.getInstance();
    private static class singletonHolder{ private static ConnectionImpl ConnectionInstance = new ConnectionImpl();}


    public static ConnectionImpl getInstance() {
        return ConnectionImpl.singletonHolder.ConnectionInstance;
    }

    /** Sends a message T to client represented by the given connectionId.
     */

    public boolean send(int connectionId, Object msg) {
        boolean status;
        ConnectionHandler curr = activeUsers.get(connectionId);
        status = (curr!=null);
        if (status)
            try {
                curr.send(msg);
            }
            catch(IOException e){
                logM.log.severe("ConnectionImpl send :"+e);
            }
        else {
            logM.log.severe("Connection " + connectionId + " does not appear in active users");
        }

        return status;
    }

    /**
    Sends a message T to clients subscribed to channel.
     */
    public void send(String channel, Object msg) {
        Queue channelClients = messageBroker.getClientPerTopic(channel);
        if (channelClients == null || channelClients.size() == 0) {
            logM.log.warning("no clients are subscribed to channel: " + channel);
        } else {
            for (Object client : channelClients) {
                Client currclient = (Client) client; // casting
                send(currclient.getConnectionId(),msg);
            }
        }
    }

    /**
     Removes an active client connectionId from the map
     */
    public void disconnect(int connectionId) {
        activeUsers.remove(connectionId);
        logM.log.info("Disconnect - connectionId " + connectionId + " removed from activeUser");
    }
    public HashMap<Integer,ConnectionHandler> getActiveUsers(){
        return activeUsers;
    }

}
