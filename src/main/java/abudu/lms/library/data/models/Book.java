package abudu.lms.library.data.models;

public class Book {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String isbn;
    private boolean available;  // Add this line

    public Book(String id, String title, String author, String publisher, int year, String isbn, boolean available) {  // Add boolean available to constructor
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.available = available;  // Add this line
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {  // Add this method
        return available;
    }

    public void setAvailable(boolean available) {  // Add this method
        this.available = available;
    }
}
