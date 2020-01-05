package bgu.spl.net.messagebroker;

public class Client {

    private Integer ConnectionId;
    private String name;
    private Inventory inventory;

    public Integer getConnectionId() {
        return ConnectionId;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Client getClient(Integer connectionId){
        if (this.ConnectionId == connectionId){
            return this;
        }
        else
            return null;
    }
}
