package bgu.spl.net.messagebroker;

public class Client {

    private Integer connectionId;
    private boolean isDefault;
    private String name;
    private String password;
    private Inventory inventory;
    private int subscription;
    private String password;


    public Client(int connectionId){
        this.connectionId = connectionId;
        this.isDefault = true;
    }

    public Client(int connectionId, String name, String password){
        this.connectionId = connectionId;
        this.name = name;
        this.password = password;
        this.isDefault = false;
    }

    public void setNameAndPass(String name,String Pass){
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

    public Client getClient(Integer connectionId){
        if (this.connectionId == connectionId){
            return this;
        }
        else
            return null;
    }

    public int getSubscription() {
        return subscription;
    }

    public String getPassword() {
        return password;
    }
}
