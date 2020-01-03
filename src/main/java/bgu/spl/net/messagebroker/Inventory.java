package bgu.spl.net.messagebroker;

import bgu.spl.net.srv.LogManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Inventory {
    private HashMap<String,List<String> > books;
    private LogManager logM = LogManager.getInstance();


    public Inventory() {
        books = new HashMap<>();
    }

    public void addBook(String genre,String bookName){
        if (books.get(genre) != null) {
            if (books.get(genre).contains(bookName)) {
                logM.log.warning("Book: " + bookName + " already in the inventory");
            } else {
                books.get(genre).add(bookName);
                logM.log.info("Book: " + bookName + " added to inventory");
            }
        }
        else {
            books.put(genre,new LinkedList<String>());
            books.get(genre).add(bookName);
            logM.log.info("Book: " + bookName + " added to inventory");
        }


    }


}
