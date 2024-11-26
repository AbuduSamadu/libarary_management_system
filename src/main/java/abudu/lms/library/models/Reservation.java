package abudu.lms.library.models;

import javafx.beans.property.*;

public class Reservation {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final LongProperty isbn;
    private final LongProperty userId;
    private final StringProperty reservationDate;
    private final StringProperty notes;
    private final BooleanProperty active;

    public Reservation(int id, String title, String author, long isbn, long userId, String reservationDate, String notes) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleLongProperty(isbn);
        this.userId = new SimpleLongProperty(userId);
        this.reservationDate = new SimpleStringProperty(reservationDate);
        this.notes = new SimpleStringProperty(notes);
        this.active = new SimpleBooleanProperty(true);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public long getIsbn() {
        return isbn.get();
    }

    public LongProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn.set(isbn);
    }

    public long getUserId() {
        return userId.get();
    }

    public LongProperty userIdProperty() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId.set(userId);
    }

    public String getReservationDate() {
        return reservationDate.get();
    }

    public StringProperty reservationDateProperty() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate.set(reservationDate);
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }
}