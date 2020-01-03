package bgu.spl.net.messagebroker;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageBroker {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Client>> topics; // will hold all topics in the broker
    private ConcurrentHashMap<Client, ConcurrentLinkedQueue<String>> registered; //will hold all registered client and their queues



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
}
