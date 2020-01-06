package bgu.spl.net.PassiveObjects;

public class Book {
    private String name;
    private User owner;
    private User loaner;

public Book(String name, User owner){
    this.name = name;
    this.owner = owner;

}

public void resetLoaner(){
    this.loaner = null;
}

    public void setLoaner(User user){
        this.loaner = user;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;

    }


    public User getLoaner() {
        return loaner;
    }
}
