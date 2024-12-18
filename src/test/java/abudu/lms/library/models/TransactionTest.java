package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {
    private Set<Role> roles;
    private Patron patron;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        Patron patron = new Patron(1, "Abudu", "Sam", "abudu", "abudu@gmail.com", "123456", LocalDateTime.now(), roles, null, 0);
        Book book = new Book(1, "Some Book", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1);
        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDateTime returnDate = LocalDateTime.now().plusDays(7);
        transaction = new Transaction(1, patron, book, borrowDate, returnDate);
    }

    @Test
    void getId() {
        assertEquals(1, transaction.getId());
    }

    @Test
    void setId() {
        transaction.setId(2);
        assertEquals(2, transaction.getId());
    }

    @Test
    void getPatron() {
        assertEquals("Abudu", transaction.getPatron().getName());
    }

    @Test
    void setPatron() {
        Patron newPatron = new Patron(1, "Sam", "Abudu", "sam", "sam@gmail.com", "123456", LocalDateTime.now(), roles, null, 0);
        transaction.setPatron(newPatron);
        assertEquals("Sam", transaction.getPatron().getName());
    }

    @Test
    void getBook() {
        assertEquals("Some Book", transaction.getBook().getTitle());
    }

    @Test
    void setBook() {
        Book newBook = new Book(1, "New Book", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1);
        transaction.setBook(newBook);
        assertEquals("New Book", transaction.getBook().getTitle());
    }

    @Test
    void getBorrowDate() {
        LocalDateTime borrowDate = LocalDateTime.now();
        transaction.setBorrowDate(borrowDate);
        assertEquals(borrowDate, transaction.getBorrowDate());
    }

    @Test
    void setBorrowDate() {
        LocalDateTime newBorrowDate = LocalDateTime.now().plusDays(1);
        transaction.setBorrowDate(newBorrowDate);
        assertEquals(newBorrowDate, transaction.getBorrowDate());
    }

    @Test
    void getReturnDate() {
        LocalDateTime returnDate = LocalDateTime.now().plusDays(7);
        transaction.setReturnDate(returnDate);
        assertEquals(returnDate, transaction.getReturnDate());
    }
}