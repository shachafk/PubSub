package bgu.spl.net.messagebroker;

public class User {

    private Integer connectionId;
    private boolean isDefault;
    private String name;
    private String password;
    private Inventory inventory;
    private int subscription;


    public User(int connectionId){
        this.connectionId = connectionId;
        this.isDefault = true;
    }

    public User(int connectionId, String name, String password){
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

    public User getClient(Integer connectionId){
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

    public boolean isDefault() {
        return isDefault;
    }
}
