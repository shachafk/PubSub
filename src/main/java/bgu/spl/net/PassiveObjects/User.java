package bgu.spl.net.PassiveObjects;


import bgu.spl.net.srv.LogManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class User {

    private Integer connectionId;
    private boolean isDefault;
    private String name;
    private String password;
    private HashMap<String, Integer> subscriptions;
    private LogManager logM = LogManager.getInstance();


    public User(int connectionId) {
        this.connectionId = connectionId;
        this.isDefault = true;
        this.subscriptions = new HashMap<>();
    }

    public User(int connectionId, String name, String password) {
        this.connectionId = connectionId;
        this.name = name;
        this.password = password;
        this.isDefault = false;

    }


    public void setNameAndPass(String name, String pass) {
        this.name = name;
        this.password = pass;
        this.isDefault = false;
    }

    public Integer getConnectionId() {
        return connectionId;
    }

    public String getName() {
        return name;
    }




    public User getClient(Integer connectionId) {
        if (this.connectionId == connectionId) {
            return this;
        } else
            return null;
    }

    public int getSubscriptionIdPerTopic(String genre) {
        Integer tmp = subscriptions.get(genre);
        if (tmp!=null){
            return tmp;
        }
        else
            return -1;
    }

    public void removeSubscriptionIdPerTopic(String genre) {
        logM.log.info("User: " + name + " subscriptionid removed. topic: " + genre);
        this.subscriptions.remove(genre);
    }

    public void addSubscriptionIdPerTopic(String genre, int id) {
        if (this.subscriptions.get(genre) != null) {
            logM.log.severe("subscriptionid already exists topic: " + genre + " id: " + id);
            return;
        }
        logM.log.info("User: " + name + " subscriptionid added. topic: " + genre + " id: " + id);
        this.subscriptions.put(genre, id);

    }

    public String getPassword() {
        return password;
    }


    public boolean isDefault() {
        return isDefault;
    }


}
