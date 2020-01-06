package bgu.spl.net.PassiveObjects;

import bgu.spl.net.srv.LogManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Inventory {

    private HashMap<String,List<Book>> books; //genre , list of books names
    private HashMap<String,List<Book>> borrowedBooks; //genre , list of books names
    private LogManager logM = LogManager.getInstance();


    public Inventory() {
        books = new HashMap<>();
        borrowedBooks = new HashMap<>();
    }


    public void addBook(String genre,Book book){
        if (books.get(genre) != null) {
            if (books.get(genre).contains(book)) {
                logM.log.warning("Book: " + book.getName() + " already in the inventory");
            } else {
                books.get(genre).add(book);
                logM.log.info("Book: " + book.getName() + " added to inventory");
            }
        }
        else {
            books.put(genre,new LinkedList<Book>());
            books.get(genre).add(book);
            logM.log.info("Book: " + book.getName() + " added to inventory");

        }


    }


    public User returnBook(String genre,String bookname){
        Boolean returned = false;
        for (Book book : borrowedBooks.get(genre)){ // iterate over all books in borrowed for this genre
            if (book.getName().equals(bookname)){
                borrowedBooks.get(genre).remove(book);
                book.resetLoaner();
                returned = true;
                logM.log.info("Book " + book.getName() + " removed from "+ book.getLoaner().getName() + " borrowed inventory");
                User owner = book.getOwner();
                owner.getInventory().returnBorrowedBookToOwner(genre,book);
                return owner;
            }
        }
        if (!returned){
            logM.log.severe("Book " + bookname + " was not returned");
        }
        return null;
    }

    public void returnBorrowedBookToOwner(String genre,Book book){
        this.books.get(genre).add(book);
        logM.log.info("Book "+ book.getName() + "returned to " + book.getOwner().getName());

    }

    public HashMap<String, List<Book>> getBooks() {
        return books;
    }

    public HashMap<String, List<Book>> getBorrowedBooks() {
        return borrowedBooks;
    }
}
