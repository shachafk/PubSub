package bgu.spl.net.Commands;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.srv.LogManager;

/**
 * Structure: borrow {genre} {book name}
 * o For this command a SEND frame is sent to the topic {genre} with the “{user} wish to borrow
 * {book name}“ in the content.
 * o The server distribute the msg to all users subscribed to the {genre}, if one of them holds the book
 * in his stock, he will send a SEND frame to the topic {genre}, with “{username} has {book name}”
 * as content.
 * o If some one has the requested book, another SEND frame will be sent to the {genre} topic, with
 * "Taking {book name} from {book owner username}", this will result in the book adding up to the
 * original (the borrower) user inventory, and being removed from the lender inventory.
 * o Transitive borrowing is allowed (I.e. Bob borrows from John which borrowed from Alice).
 * o If multiple users has a book, the borrower will take from the first one only (according to msg
 * arrival order).
 * o This command has multiple frames involved.
 */
public class BorrowBook implements Command {
    private String bookName;
    private String genre;
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.BorrowBook;


    public BorrowBook(Message msg) {
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("BorrowBook msg is not valid");
            return;
        } else {
            this.genre = msg.getGenre();
            String body = msg.getBody();
            this.bookName = body.substring(body.indexOf("borrow") + 7);
        }
    }



    @Override
    public Message execute(User arg) {
        if (arg instanceof User) {
            User user = (User) arg;
            Message toReturn = new Message();
            toReturn.setCommand("MESSAGE");
            toReturn.addHeader("subscription", ""+ user.getSubscriptionIdPerTopic(genre));
            toReturn.addHeader("Message-id", ""+ MessageID.getMessageId());
            toReturn.addHeader("destination",""+genre);
            toReturn.setBody("" + user.getName()+" wish to borrow "+bookName);
            return toReturn;
        }
        else{
            logM.log.severe("arg isnt instance of User");
            return null;
        }
    }
    public CommandType getType() {
        return type;
    }
}
