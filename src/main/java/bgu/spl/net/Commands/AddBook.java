package bgu.spl.net.Commands;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.messagebroker.Client;

import java.io.Serializable;

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



    @Override
    public Serializable execute(Object arg) {
        Client client = (Client) arg;
        client.getInventory().addBook(genre,bookName);

        return null;
    }
}
