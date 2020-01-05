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
    private Inventory inventory;
    private HashMap<String, Integer> subscriptions;
    private LogManager logM = LogManager.getInstance();


    public User(int connectionId) {
        this.connectionId = connectionId;
        this.isDefault = true;
        this.inventory = new Inventory();
    }

    public User(int connectionId, String name, String password) {
        this.connectionId = connectionId;
        this.name = name;
        this.password = password;
        this.isDefault = false;
        this.inventory = new Inventory();
    }

    public void setNameAndPass(String name, String Pass) {
        this.name = name;
        this.password = password;
        this.isDefault = false;
    }

    public Integer getConnectionId() {
        return connectionId;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public User getClient(Integer connectionId) {
        if (this.connectionId == connectionId) {
            return this;
        } else
            return null;
    }

    public int getSubscriptionIdPerTopic(String genre) {
        return this.subscriptions.get(genre);
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

    public String getInventoryStatus() {
        String toReturn = name + ":";
        Boolean firstBook = true;

        Iterator itMap = inventory.getBooks().entrySet().iterator();
        while (itMap.hasNext()) { // iterate over the genre map
            Map.Entry pair = (Map.Entry) itMap.next();
            List list = (List) pair.getValue();
            Iterator itList = list.iterator();
            while (itList.hasNext()) { //iterate over the books list
                Book curr = (Book) itList.next();
                if (firstBook) {
                    toReturn = toReturn + curr.getName();
                    firstBook = false;
                } else {
                    toReturn = toReturn + "," + curr.getName();
                }
            }
        }
        if (firstBook) {
            toReturn = "Book status";
        }
        return toReturn;
    }
}
