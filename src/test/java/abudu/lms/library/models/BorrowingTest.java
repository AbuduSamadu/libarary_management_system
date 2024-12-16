package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BorrowingTest {
    private Borrowing borrowing;

    @BeforeEach
    void setUp() {
        borrowing = new Borrowing(1, "Java Programming", "John Doe", 1234567890, 1, "2021-07-01", "Java Programming Book");
    }

    @Test
    void getId() {
        assertEquals(1, borrowing.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Java Programming", borrowing.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals("John Doe", borrowing.getAuthor());
    }

    @Test
    void getIsbn() {
        assertEquals(1234567890, borrowing.getIsbn());
    }

    @Test
    void getUserId() {
        assertEquals(1, borrowing.getUserId());
    }

    @Test
    void getBorrowDate() {
        assertEquals("2021-07-01", borrowing.getBorrowDate());
    }

    @Test
    void getNotes() {
        assertEquals("Java Programming Book", borrowing.getNotes());
    }

    @Test
    void isActive() {
        assertTrue(borrowing.isActive());
    }
}