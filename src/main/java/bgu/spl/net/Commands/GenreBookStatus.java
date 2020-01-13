package bgu.spl.net.Commands;

import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;


/**
 * o Structure: status {genre}
 * o For this command a SEND frame is sent to the {genre} topic with "book status” in the body.
 * o All the subscribed users will send a SEND frame, each with its current inventory, each book
 * seperated by a comma (,), and the name (example below).
 */
public class GenreBookStatus implements Command {
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.GenreBookStatus;
    private String genre;
    private String body;


    public GenreBookStatus(Message msg ) {
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("AddBook msg is not valid");
            return;
        } else {
            this.genre = msg.getGenre();
            this.body = msg.getBody();

        }
    }

    @Override
    public Serializable execute(Object arg) {
        User user = (User) arg;
        Message toReturn = new Message();
        toReturn.setCommand("MESSAGE");
        toReturn.addHeader("subscription", "" + user.getSubscriptionIdPerTopic(genre));
        toReturn.addHeader("Message-id", "" + MessageID.getMessageId());
        toReturn.addHeader("destination", genre);
        toReturn.setBody(body);

        return toReturn;
    }

    public CommandType getType() {
        return type;
    }
}
