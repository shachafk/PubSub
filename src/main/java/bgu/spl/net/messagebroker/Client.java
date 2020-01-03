package bgu.spl.net.messagebroker;

public class Client {

    private Integer ConnectionId;
    private String name;
    private Inventory inventory;
    private String password;

    public Client(Integer ConnectionId,String name,String password){
        this.ConnectionId=ConnectionId;
        this.name=name;
        this.password=password;
    }

    public Integer getConnectionId() {
        return ConnectionId;
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }
    public String getPassword(){
        return password;
    }
}
