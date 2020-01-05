package bgu.spl.net.Commands;
import bgu.spl.net.api.Message;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;
import java.sql.Connection;

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
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.JoinGenre;


    public JoinGenre(Message msg ) {
        if (!msg.getCommand().equals("SUBSCRIBE") | msg.getHeader().size() < 3) {
            logM.log.severe("JoinGenre msg is not valid");
            return;
        } else {
            this.genre = msg.getHeader().get(0).getSecond();
            this.id = Integer.valueOf(msg.getHeader().get(1).getSecond());
            this.receiptid = Integer.valueOf(msg.getHeader().get(2).getSecond());
        }
    }

    @Override
    public Serializable execute(Object arg) {
        Client client = (Client) arg;
        ConnectionsImpl conn = ConnectionsImpl.getInstance();
        conn.subscribe(genre,client);
        System.out.println("Joined club "+ genre);
        Message receipt = new Message();
        receipt.setCommand("RECEIPT");
        receipt.addHeader("receipt-id", ""+ receiptid);
        receipt.setBody("Subscribed successfully from genre " + genre);
        return receipt;
    }

    @Override
    public CommandType getType() {
        return type;
    }
}
