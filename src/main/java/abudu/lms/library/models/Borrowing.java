package abudu.lms.library.models;

import javafx.beans.property.*;

public class Borrowing {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final LongProperty isbn;
    private final LongProperty userId;
    private final StringProperty borrowDate;
    private final StringProperty notes;
    private final BooleanProperty active;

    public Borrowing(int id, String title, String author, long isbn, long userId, String borrowDate, String notes) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleLongProperty(isbn);
        this.userId = new SimpleLongProperty(userId);
        this.borrowDate = new SimpleStringProperty(borrowDate);
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

    public String getBorrowDate() {
        return borrowDate.get();
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate.set(borrowDate);
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