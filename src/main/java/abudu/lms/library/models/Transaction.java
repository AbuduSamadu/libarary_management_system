package abudu.lms.library.models;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private Patron patron;
    private Book book;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    public Transaction(int id, Patron patron, Book book, LocalDateTime borrowDate, LocalDateTime returnDate) {
        this.id = id;
        this.patron = patron;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}