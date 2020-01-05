package bgu.spl.net.Commands;

import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;
import bgu.spl.net.srv.LogManager;

import java.io.Serializable;

/**
 *  Structure: return {genre} {book name}
 * o For this command a SEND frame is sent to {genre} topic with the content “Returning {book name}
 * to {book lender}”.
 * o This will result in removing the book from the borrower inventory, and adding it back to the
 * lender.
 * o If a book has been double borrowed, it need to be returned in the correct order.
 */

public class ReturnBook implements Command {
    private String bookName;
    private String genre;
    private String user;
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.ReturnBook;


    public ReturnBook(Message msg){
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("ReturnBook msg is not valid");
            return;
        } else {
            this.genre = msg.getHeader().get(0).getSecond();
            String body = msg.getBody();
            this.bookName = body.substring(body.indexOf("Returning")+10,body.indexOf("to"));
            this.user = body.substring(body.indexOf("to") +3);
        }
    }


    @Override
    public Serializable execute(Object arg) {
        Client client = (Client) arg;
        client.getInventory().returnBook(genre,bookName); //If a book has been double borrowed, it need to be returned in the correct order - TBD


        Message toReturn = new Message();
        toReturn.setCommand("MESSAGE");
        toReturn.addHeader("subscription", ""+client.getSubscription());
        toReturn.addHeader("Message-id", ""+ MessageID.getMessageId());
        toReturn.addHeader("destination",genre);
        toReturn.setBody( "Returning "+ bookName+ " to "+ client.getName());
        return toReturn;
    }

    @Override
    public CommandType getType() {
        return type;
    }
}
