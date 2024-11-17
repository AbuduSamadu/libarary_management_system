package abudu.lms.library.models;

import java.time.LocalDateTime;

public class ReturnBook {
    private int id;
    private Patron patron;
    private Book book;
    private LocalDateTime returnDate;

    public ReturnBook(int id, Patron patron, Book book, LocalDateTime returnDate) {
        this.id = id;
        this.patron = patron;
        this.book = book;
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

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}
