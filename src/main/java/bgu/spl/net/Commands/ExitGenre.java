package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

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


    public ExitGenre(Message msg) {
        if (!msg.getCommand().equals("UNSUBSCRIBE") | msg.getHeader().size() < 1) {
            logM.log.severe("ExitGenre msg is not valid");
            return;
        } else {
                this.genre = msg.getGenre();
                this.id = msg.getSubId();
                this.receiptid = msg.getReceiptId();
            }

    }



    public Message execute(User arg) {
        User user = (User) arg;
        ConnectionsImpl conn = ConnectionsImpl.getInstance();
        conn.unsubscribe(genre, user);
        //System.out.println("Exited club "+ genre);
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
