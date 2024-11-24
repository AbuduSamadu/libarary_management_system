package abudu.lms.library.models;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty publisher;
    private final IntegerProperty year;
    private final IntegerProperty isbn;
    private final BooleanProperty available;
    private final StringProperty category;
    private final IntegerProperty quantity;
    private final StringProperty description;
    private final LongProperty userId;

    public Book(int id, String title, String author, String publisher, int year, int isbn, boolean available, String category, int quantity, String description, long userId) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.year = new SimpleIntegerProperty(year);
        this.isbn = new SimpleIntegerProperty(isbn);
        this.available = new SimpleBooleanProperty(available);
        this.category = new SimpleStringProperty(category);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.description = new SimpleStringProperty(description);
        this.userId = new SimpleLongProperty(userId);
    }

    // Getters and setters for properties
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }
    public void setTitle(String title) { this.title.set(title); }

    public String getAuthor() { return author.get(); }
    public StringProperty authorProperty() { return author; }
    public void setAuthor(String author) { this.author.set(author); }

    public String getPublisher() { return publisher.get(); }
    public StringProperty publisherProperty() { return publisher; }
    public void setPublisher(String publisher) { this.publisher.set(publisher); }

    public int getYear() { return year.get(); }
    public IntegerProperty yearProperty() { return year; }
    public void setYear(int year) { this.year.set(year); }

    public int getIsbn() { return isbn.get(); }
    public IntegerProperty isbnProperty() { return isbn; }
    public void setIsbn(int isbn) { this.isbn.set(isbn); }

    public boolean isAvailable() { return available.get(); }
    public BooleanProperty availableProperty() { return available; }
    public void setAvailable(boolean available) { this.available.set(available); }

    public String getCategory() { return category.get(); }
    public StringProperty categoryProperty() { return category; }
    public void setCategory(String category) { this.category.set(category); }

    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }

    public long getUserId() { return userId.get(); }
    public LongProperty userIdProperty() { return userId; }
    public void setUserId(long userId) { this.userId.set(userId); }

    public ObservableValue<String> actionsProperty() {
        return new SimpleStringProperty("Edit | Delete");
    }
}