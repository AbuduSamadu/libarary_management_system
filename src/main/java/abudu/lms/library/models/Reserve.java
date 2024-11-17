package abudu.lms.library.models;

import java.time.LocalDateTime;

public class Reserve {
    private int id;
    private Patron patron;
    private Book book;
    private LocalDateTime reserveDate;

    public Reserve(int id, Patron patron, Book book, LocalDateTime reserveDate) {
        this.id = id;
        this.patron = patron;
        this.book = book;
        this.reserveDate = reserveDate;
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

    public LocalDateTime getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(LocalDateTime reserveDate) {
        this.reserveDate = reserveDate;
    }
}