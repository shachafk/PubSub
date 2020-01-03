package bgu.spl.net.messagebroker;

import bgu.spl.net.srv.LogManager;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public class MessageBroker {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Client>> topics; // will hold all topics in the broker
    private ConcurrentHashMap<Client, ConcurrentLinkedQueue<String>> registered; //will hold all registered client and their queues
    private LogManager logM = LogManager.getInstance();


    /**
     * Subscribe client to topic genre
     * @param genre
     * @param client
     */
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

    private static class singletonHolder{ private static MessageBroker MessageBrokerInstance = new MessageBroker();}

    public static MessageBroker getInstance() {
        return singletonHolder.MessageBrokerInstance;
    }

    public Queue getClientPerTopic(String s){
        return topics.get(s); //may return null
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<Client>> getTopics() {
        return topics;
    }
    public ConcurrentHashMap<Client, ConcurrentLinkedQueue<String>> getRegistered(){
        return registered;
    }
}
