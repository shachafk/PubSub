package bgu.spl.net.Commands;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.LogManager;
import java.io.Serializable;

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



    @Override
    public Serializable execute(Object arg) {
        return null;
    }

    public CommandType getType() {
        return type;
    }
}
