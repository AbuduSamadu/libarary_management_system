package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingTest {
    private Borrowing borrowing;
    private Set<Book> borrowedBooks;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        borrowedBooks = new HashSet<>();
        borrowing = new Borrowing(1, "Java Programming", "John Doe", 1234567890, 1, "2021-07-01", "Java Programming Book");
    }

    @Test
    void getId() {
        assertEquals(1, borrowing.getId());
    }

    @Test
    void setId() {
        borrowing.setId(2);
        assertEquals(2, borrowing.getId());
    }

    @Test
    void getTitle() {
        assertEquals("Java Programming", borrowing.getTitle());
    }

    @Test
    void setTitle() {
        borrowing.setTitle("Python Programming");
        assertEquals("Python Programming", borrowing.getTitle());
    }

    @Test
    void getAuthor() {
        assertEquals("John Doe", borrowing.getAuthor());
    }

    @Test
    void setAuthor() {
        borrowing.setAuthor("Jane Doe");
        assertEquals("Jane Doe", borrowing.getAuthor());
    }

    @Test
    void getIsbn() {
        assertEquals(1234567890, borrowing.getIsbn());
    }

    @Test
    void setIsbn() {
        borrowing.setIsbn(1234567891);
        assertEquals(1234567891, borrowing.getIsbn());
    }

    @Test
    void getUserId() {
        assertEquals(1, borrowing.getUserId());
    }

    @Test
    void setUserId() {
        borrowing.setUserId(2);
        assertEquals(2, borrowing.getUserId());
    }

    @Test
    void getBorrowDate() {
        assertEquals("2021-07-01", borrowing.getBorrowDate());
    }

    @Test
    void setBorrowDate() {
        borrowing.setBorrowDate("2021-07-02");
        assertEquals("2021-07-02", borrowing.getBorrowDate());
    }

    @Test
    void getNotes() {
        assertEquals("Java Programming Book", borrowing.getNotes());
    }

    @Test
    void setNotes() {
        borrowing.setNotes("Python Programming Book");
        assertEquals("Python Programming Book", borrowing.getNotes());
    }

    @Test
    void isActive() {
        assertTrue(borrowing.isActive());
    }

    @Test
    void setActive() {
        borrowing.setActive(false);
        assertFalse(borrowing.isActive());
    }
}