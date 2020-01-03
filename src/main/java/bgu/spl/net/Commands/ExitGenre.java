package bgu.spl.net.Commands;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;

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

    public ExitGenre(String genre, int id,int receiptid){
        this.genre = genre;
        this.id = id;
        this.receiptid = receiptid;
    }

    @Override
    public Serializable execute(Object arg) {
        MessageBroker.getInstance().unsubscribe(genre,(Client) arg);
        System.out.println("Exited club "+ genre);
        return new Receipt(receiptid,"Unsubscribed successfully");
    }
}
