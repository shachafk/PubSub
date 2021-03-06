package bgu.spl.net.Commands;
import bgu.spl.net.api.Message;
import bgu.spl.net.api.MessageID;
import bgu.spl.net.PassiveObjects.User;
import bgu.spl.net.srv.LogManager;

/**
 * Structure: add {genre} {book name}
 * o For this command a SEND frame is sent to the topic {genre} with the content: “{user} has added
 * the book {book name}
 * o The book will be added to the client inventory.
 * o The inventory is per genre, no book is multi genre.
 */
public class AddBook implements Command {
    private String bookName;
    private String genre;
    private LogManager logM = LogManager.getInstance();
    private CommandType type = CommandType.AddBook;

    public AddBook(Message msg) {
        if (!msg.getCommand().equals("SEND") | msg.getHeader().size() < 1) {
            logM.log.severe("AddBook msg is not valid");
            return;
        } else {
            this.genre = msg.getGenre();
            String body = msg.getBody();
            this.bookName = body.substring(body.indexOf("book")+5);

        }
    }

    @Override
    public Message execute(User arg) {
        User user = (User) arg;
     //   Book toAdd = new Book(bookName,user);
     //   user.getInventory().addBook(genre,toAdd);

        Message toReturn = new Message();
        toReturn.setCommand("MESSAGE");
        toReturn.addHeader("subscription", ""+ user.getSubscriptionIdPerTopic(genre));
        toReturn.addHeader("Message-id", ""+ MessageID.getMessageId());
        toReturn.addHeader("destination",genre);
        toReturn.setBody(user.getName()+ " has added the book "+ bookName);
        return toReturn;
    }

    public CommandType getType() {
        return type;
    }
}
