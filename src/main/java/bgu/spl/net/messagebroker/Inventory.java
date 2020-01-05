package bgu.spl.net.messagebroker;

import bgu.spl.net.srv.LogManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Inventory {
    private HashMap<String,List<String> > books; //genre , list of books names
    private HashMap<String,List<String> > borrowedBooks; //genre , list of books names
    private LogManager logM = LogManager.getInstance();


    public Inventory() {
        books = new HashMap<>();
        borrowedBooks = new HashMap<>();
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

    public void returnBook(String genre,String bookName){
        if (borrowedBooks.get(genre) != null) {
            if (!borrowedBooks.get(genre).contains(bookName)) {
                logM.log.warning("Book: " + bookName + " not in borrowed inventory");
            } else {
                if (books.get(genre) == null){ // genre not in books
                    books.put(genre,new LinkedList<String>()); // add the genre
                }
                books.get(genre).add(bookName); // add to books
                borrowedBooks.get(genre).remove(bookName); // remove from borrowed
                logM.log.info("Book: " + bookName + " added to inventory");
            }
    }


}}
