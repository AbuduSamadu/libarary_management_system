package abudu.lms.library.data.models;

import java.util.LinkedList;
import java.util.Queue;

public class Librarian {
    private int id;
    private String name;
    private String email;
    private String password;
    private LinkedList<Book> bookList;
    private LinkedList<Patron> patronList;
    private Queue<Transaction> transactionList;

    public Librarian(int id, String name, String email, String password, LinkedList<Book> bookList, LinkedList<Patron> patronList, Queue<Transaction> transactionList) {    // Add this line
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.bookList = bookList;  // Add this line
        this.patronList = patronList;  // Add this line
        this.transactionList = transactionList;  // Add this line

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LinkedList<Book> getBookList() {
        return bookList;
    }

    public void setBookList(LinkedList<Book> bookList) {
        this.bookList = bookList;
    }

    public LinkedList<Patron> getPatronList() {
        return patronList;
    }

    public void setPatronList(LinkedList<Patron> patronList) {
        this.patronList = patronList;
    }

    public Queue<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(Queue<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

}
