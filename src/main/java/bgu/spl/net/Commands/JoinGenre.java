package bgu.spl.net.Commands;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.messagebroker.MessageBroker;
import java.io.Serializable;

/**
 * Join Genre Reading Club Command
 * o Structure: join {genre}
 * o For this command a SUBSCRIBE frame is sent to the {genre} topic.
 * o As a result, a RECIEPT will be returned to the client. A message “Joined club {genre}” will be
 * displayed to the screen.
 */


public class JoinGenre implements Command {
    private String genre;
    private int id;
    private int receiptid;

    public JoinGenre(String genre, int id,int receiptid){
        this.genre = genre;
        this.id = id;
        this.receiptid = receiptid;
    }

    @Override
    public Serializable execute(Object arg) {
        MessageBroker.getInstance().subscribe(genre,(Client) arg);
        System.out.println("Joined club "+ genre);
        return new Receipt(receiptid,"Subscribed successfully");
    }
}
