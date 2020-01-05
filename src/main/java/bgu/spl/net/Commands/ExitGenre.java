package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;

/**
 Exit Genre Reading Club Command
 o Structure: exit {genre}
 o For this command a UNSUBSCRIBE frame is sent to the {genre} topic.
 o As a result, a RECIEPT will be returned to the client. A message “Exited club {genre}” will be
 displayed to the screen.
 */

public class ExitGenre implements Command {
    private String genre;
    private int id;
    private int receiptid;
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.ExitGenre;


    public ExitGenre(Message msg){
        if (!msg.getCommand().equals("SUBSCRIBE") | msg.getHeader().size() < 3) {
            logM.log.severe("ExitGenre msg is not valid");
            return;
        } else {
            this.genre = msg.getHeader().get(0).getSecond();
            this.id = Integer.valueOf(msg.getHeader().get(1).getSecond());
            this.receiptid = Integer.valueOf(msg.getHeader().get(2).getSecond());
        }
    }


    public Serializable execute(Object arg) {
        Client client = (Client) arg;
        ConnectionsImpl conn = ConnectionsImpl.getInstance();
        conn.unsubscribe(genre,client);
        System.out.println("Exited club "+ genre);
        Message receipt = new Message();
        receipt.setCommand("RECEIPT");
        receipt.addHeader("receipt-id", ""+ receiptid);
        receipt.setBody("Unsubscribed successfully from genre " + genre);
        return receipt;
    }

    public CommandType getType() {
        return type;
    }
}
