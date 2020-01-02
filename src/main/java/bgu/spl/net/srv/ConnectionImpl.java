package bgu.spl.net.srv;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;
import java.util.HashMap;
import java.util.Queue;

public class ConnectionImpl implements Connections {
    private HashMap<Integer,ConnectionHandler> activeUsers = new HashMap<>();
    private LogManager logM = LogManager.getInstance();
    private MessageBroker messageBroker = MessageBroker.getInstance();

    /** Sends a message T to client represented by the given connectionId.
     */
    public boolean send(int connectionId, Object msg) {
        boolean status;
        ConnectionHandler curr = activeUsers.get(connectionId);
        status = (curr!=null);
        if (status)
            curr.send(msg);
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
                ConnectionHandler curr = activeUsers.get(currclient.getConnectionId());
                if (curr != null)
                    curr.send(msg);
                else {
                    logM.log.severe("Connection " + currclient.getConnectionId() + " does not appear in active users");
                }
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
}
