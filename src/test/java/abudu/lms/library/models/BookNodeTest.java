package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookNodeTest {
    private BookNode bookNode;
    private Book book;

    @BeforeEach
    void setUp() {

        book = new Book(1, "Java Programming 3", "John Doe 3", "John Doe 3", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 3", 1);
        bookNode = new BookNode(book);
    }

    @Test
    void getBook() {
        assertEquals(book, bookNode.getBook());
    }

    @Test
    void setBook() {
        bookNode.setBook(new Book(2, "Java Programming 4", "John Doe 4", "John Doe 4", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 4", 1));
        assertEquals(2, bookNode.getBook().getId());
    }

    @Test
    void getNext() {
        assertNull(bookNode.getNext());
    }

    @Test
    void setNext() {
        bookNode.setNext(new BookNode(book));
        assertNotNull(bookNode.getNext());
    }
}