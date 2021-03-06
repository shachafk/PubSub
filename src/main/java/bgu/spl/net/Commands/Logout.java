package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logout implements Command {
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.Logout;
    private ConnectionsImpl connections = ConnectionsImpl.getInstance();
    private int receiptid;


    public Logout(Message msg){
        this.receiptid = msg.getReceiptId();
    }





    @Override
    public Message execute(User arg) {
        if (arg instanceof User) {
            User user = (User) arg;
            connections.getLoggedIn().remove(user.getName());
            ConcurrentHashMap<String, ConcurrentLinkedQueue<User>>topics= connections.getTopics();
            for (String s : topics.keySet()) {
                connections.unsubscribe(s,user);
            }
            Message recepit = new Message();
            recepit.setCommand("RECEIPT");
            recepit.addHeader("receipt-id", "" + receiptid);
            recepit.setBody("");
            return recepit;
        }
        else{
            logM.log.severe("Object isnot instance of User");
            return null;
        }
    }


    @Override
    public CommandType getType() {
        return type;
    }
}

