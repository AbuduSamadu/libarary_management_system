package abudu.lms.library.models;

import java.util.LinkedList;

public class Patron {
    private int id;
    private String name;
    private String email;
    private String password;
    private final LinkedList<Book> borrowedBooks;

    public Patron(int id, String name, String email, String password, LinkedList<Book> borrowedBooks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.borrowedBooks = borrowedBooks;
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
    public LinkedList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

}
