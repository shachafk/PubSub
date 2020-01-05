package bgu.spl.net.srv;
import bgu.spl.net.api.Message;
import bgu.spl.net.messagebroker.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionsImpl implements Connections {
    private HashMap<Integer,ConnectionHandler> activeUsers = new HashMap<>();
    private LogManager logM = LogManager.getInstance();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Client>> topics; // will hold all topics in the broker
    private ConcurrentHashMap<String, Client> registered; //will hold all registered clients
    private ConcurrentHashMap<String, Client> loggedIn; //will hold all loggedin clients
    private ConcurrentHashMap<Message,Client> msgClientMap;
    private static class singletonHolder{ private static ConnectionsImpl ConnectionInstance = new ConnectionsImpl();}
    public static ConnectionsImpl getInstance() {
        return ConnectionsImpl.singletonHolder.ConnectionInstance;
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
                logM.log.severe("ConnectionsImpl send :"+e);
            }
        else {
            logM.log.severe("Connection " + connectionId + " does not appear in active users");
        }

        return status;
    }

    public void addActiveUser(int connectionID,ConnectionHandler handler){
        this.activeUsers.put(connectionID,handler);
    }

    /**
    Sends a message T to clients subscribed to channel.
     */
    public void send(String channel, Object msg) {
        Queue channelClients = topics.get(channel);
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

    public void subscribe(String genre, Client client) {
        ConcurrentLinkedQueue<Client> queue = topics.computeIfAbsent(genre, k -> new ConcurrentLinkedQueue<>());
        queue.add(client);
        logM.log.info("Client: " + client.getName() + " subscribed from genre: "+ genre);
    }

    /**
     * Unsubscribe client from topic genre
     * @param genre
     * @param client
     */
    public void unsubscribe(String genre, Client client) {
        ConcurrentLinkedQueue<Client> queue = topics.get(genre);
        queue.remove(client);
        logM.log.info("Client: " + client.getName() + " unsubscribed from genre: "+ genre);
    }

    public Client getClientByMsg(Message m){
        return msgClientMap.get(m);
    }

    public void addMsgPerclient(Message msg,Client client){
        this.msgClientMap.put(msg,client);
    }


}
