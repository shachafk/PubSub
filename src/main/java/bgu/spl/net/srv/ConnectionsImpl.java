package bgu.spl.net.srv;
import bgu.spl.net.api.Message;
import bgu.spl.net.PassiveObjects.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionsImpl implements Connections {

    private HashMap<Integer,ConnectionHandler> activeClients = new HashMap<>(); // ConnectionID, ConnectionHandler

    private LogManager logM = LogManager.getInstance();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> topics; // will hold all topics in the broker
    private ConcurrentHashMap<String, User> registered; //will hold all registered clients
    private ConcurrentHashMap<String, User> loggedIn; //will hold all loggedin clients
    private ConcurrentHashMap<Message, User> msgClientMap;
    private static class singletonHolder{ private static ConnectionsImpl ConnectionInstance = new ConnectionsImpl();}
    public static ConnectionsImpl getInstance() {
        return ConnectionsImpl.singletonHolder.ConnectionInstance;
    }

    /** Sends a message T to client represented by the given connectionId.
     */

    public boolean send(int connectionId, Object msg) {
        boolean status;
        ConnectionHandler curr = activeClients.get(connectionId);
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


    public void addActiveClient(int connectionID, ConnectionHandler handler){
        this.activeClients.put(connectionID,handler);
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
                User currclient = (User) client; // casting
                send(currclient.getConnectionId(),msg);
            }
        }
    }

    /**
     Removes an active client connectionId from the map
     */
    public void disconnect(int connectionId) {
        activeClients.remove(connectionId);
        logM.log.info("Disconnect - connectionId " + connectionId + " removed from activeUser");
    }

    public void subscribe(String genre, User user) {
        ConcurrentLinkedQueue<User> queue = topics.computeIfAbsent(genre, k -> new ConcurrentLinkedQueue<>());
        queue.add(user);
        logM.log.info("User: " + user.getName() + " subscribed from genre: "+ genre);
    }

    /**
     * Unsubscribe user from topic genre
     * @param genre
     * @param user
     */
    public void unsubscribe(String genre, User user) {
        ConcurrentLinkedQueue<User> queue = topics.get(genre);
        queue.remove(user);
        logM.log.info("User: " + user.getName() + " unsubscribed from genre: "+ genre);
    }

    public User getClientByMsg(Message m){
        return msgClientMap.get(m);
    }

    public ConcurrentHashMap<String, User> getRegistered() {
        return registered;
    }

    public ConcurrentLinkedQueue getUsersByTopic(String genre){
        return topics.get(genre);
    }

    public ConcurrentHashMap<String, User> getLoggedIn() {
        return loggedIn;
    }
    public void addMsgPerclient(Message msg, User user){
        this.msgClientMap.put(msg, user);
    }
    public void addToLoggedIn(String name, User user){
        loggedIn.put(name,user);
    }
    public ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> getTopics() {
        return topics;
    }
    public HashMap<Integer, ConnectionHandler> getActiveClients() {
        return activeClients;
    }
}
