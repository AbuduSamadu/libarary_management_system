package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book(1, "Java Programming", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1);
    }

    @Test
    void getId() {
        assertEquals(1, book.getId());
    }

    @Test
    void idProperty() {
        assertEquals(1, book.idProperty().get());
    }

    @Test
    void setId() {
        book.setId(2);
        assertEquals(2, book.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Java Programming", book.getTitle());
    }

    @Test
    void titleProperty() {
        assertEquals("Java Programming", book.titleProperty().get());
    }

    @Test
    void setTitle() {
        book.setTitle("Java Programming 2");
        assertEquals("Java Programming 2", book.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals("John Doe", book.getAuthor());
    }

    @Test
    void authorProperty() {
        assertEquals("John Doe", book.authorProperty().get());
    }

    @Test
    void setAuthor() {
        book.setAuthor("John Doe 2");
        assertEquals("John Doe 2", book.getAuthor());
    }

    @Test
    void getPublisher() {
        assertEquals("John Doe", book.getPublisher());
    }

    @Test
    void publisherProperty() {
        assertEquals("John Doe", book.publisherProperty().get());
    }

    @Test
    void setPublisher() {
        book.setPublisher("John Doe 2");
        assertEquals("John Doe 2", book.getPublisher());
    }

    @Test
    void getYear() {
        assertEquals(2021, book.getYear());
    }

    @Test
    void yearProperty() {
        assertEquals(2021, book.yearProperty().get());
    }

    @Test
    void setYear() {
        book.setYear(2022);
        assertEquals(2022, book.getYear());
    }

    @Test
    void getIsbn() {
        assertEquals(1234567890, book.getIsbn());
    }

    @Test
    void isbnProperty() {
        assertEquals(1234567890, book.isbnProperty().get());
    }

    @Test
    void setIsbn() {
        book.setIsbn(1234567890);
        assertEquals(1234567890, book.getIsbn());
    }

    @Test
    void isAvailable() {
        assertTrue(book.isAvailable());
    }

    @Test
    void availableProperty() {
        assertTrue(book.availableProperty().get());
    }

    @Test
    void setAvailable() {
        book.setAvailable(false);
        assertFalse(book.isAvailable());
    }

    @Test
    void getCategory() {
        assertEquals("Programming", book.getCategory());
    }

    @Test
    void categoryProperty() {
        assertEquals("Programming", book.categoryProperty().get());
    }

    @Test
    void setCategory() {
        book.setCategory("Programming 2");
        assertEquals("Programming 2", book.getCategory());
    }

    @Test
    void getQuantity() {
        assertEquals(10, book.getQuantity());
    }

    @Test
    void quantityProperty() {
        assertEquals(10, book.quantityProperty().get());
    }

    @Test
    void setQuantity() {
        book.setQuantity(20);
        assertEquals(20, book.getQuantity());
    }

    @Test
    void getDescription() {
        assertEquals("Java Programming Book", book.getDescription());
    }

    @Test
    void descriptionProperty() {
        assertEquals("Java Programming Book", book.descriptionProperty().get());
    }

    @Test
    void setDescription() {
        book.setDescription("Java Programming Book 2");
        assertEquals("Java Programming Book 2", book.getDescription());
    }

    @Test
    void getUserId() {
        assertEquals(1, book.getUserId());
    }

    @Test
    void userIdProperty() {
        assertEquals(1, book.userIdProperty().get());
    }

    @Test
    void setUserId() {
        book.setUserId(2);
        assertEquals(2, book.getUserId());
    }

    @Test
    void actionsProperty() {
        assertEquals("Actions", book.actionsProperty().getValue());
    }
}