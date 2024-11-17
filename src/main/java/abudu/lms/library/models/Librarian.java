package abudu.lms.library.models;

import java.time.LocalDateTime;
import java.util.*;

public class Librarian extends User {
    private List<Book> bookList;          // For ordered, index-based access
    private Map<Integer, Patron> patronMap; // For efficient lookup by ID
    private Queue<Transaction> transactionList; // FIFO transaction management

    public Librarian(
            int id,
            String firstname,
            String lastname,
            String username,
            String email,
            String password,
            LocalDateTime createdAt,
            Set<Role> roles,
            List<Book> bookList,
            Map<Integer, Patron> patronMap,
            Queue<Transaction> transactionList
    ) {
        super(id, firstname, lastname, username, email, password, createdAt, roles);
        this.bookList = bookList;
        this.patronMap = patronMap;
        this.transactionList = transactionList;
    }

    // Getters and Setters
    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Map<Integer, Patron> getPatronMap() {
        return patronMap;
    }

    public void setPatronMap(Map<Integer, Patron> patronMap) {
        this.patronMap = patronMap;
    }

    public Queue<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(Queue<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    // Additional methods for managing books, patrons, and transactions can be added here
}
