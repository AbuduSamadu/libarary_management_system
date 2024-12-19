package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionTest {
    private Transaction transaction;
    private Patron patron;
    private Book book;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private Set<Role> roles;
    private Set<Book> borrowedBooks;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        borrowedBooks = new HashSet<>();
        borrowedBooks.add(new Book(1, "Some Book", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1));
        patron = new Patron(1, "Abudu", "Sam", "abudu", "abudu@gmail.com", "123456", LocalDateTime.now(), roles, borrowedBooks, 0);
        book = new Book(1, "Some Book", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1);
        borrowDate = LocalDateTime.now();
        returnDate = LocalDateTime.now().plusDays(14); // Assuming a 14-day borrowing period
        transaction = new Transaction(1, patron, book, borrowDate, returnDate);
    }

    @Test
    void testGetId() {
        assertEquals(1, transaction.getId());
    }

    @Test
    void testSetId() {
        transaction.setId(2);
        assertEquals(2, transaction.getId());
    }

    @Test
    void testGetPatron() {
        assertEquals(patron, transaction.getPatron());
    }

    @Test
    void testSetPatron() {
        Patron newPatron = new Patron(2, "Jane", "Doe", "jane", "jane@gmail.com", "654321", LocalDateTime.now(), roles, borrowedBooks, 0);
        transaction.setPatron(newPatron);
        assertEquals(newPatron, transaction.getPatron());
    }

    @Test
    void testGetBook() {
        assertEquals(book, transaction.getBook());
    }

    @Test
    void testSetBook() {
        Book newBook = new Book(2, "Another Book", "Jane Doe", "Jane Doe", 2022, 98765432, true, "Programming", 15, "Another Programming Book", 2);
        transaction.setBook(newBook);
        assertEquals(newBook, transaction.getBook());
    }

    @Test
    void testGetBorrowDate() {
        assertEquals(borrowDate, transaction.getBorrowDate());
    }

    @Test
    void testSetBorrowDate() {
        LocalDateTime newBorrowDate = LocalDateTime.now().minusDays(7);
        transaction.setBorrowDate(newBorrowDate);
        assertEquals(newBorrowDate, transaction.getBorrowDate());
    }

    @Test
    void testGetReturnDate() {
        assertEquals(returnDate, transaction.getReturnDate());
    }

    @Test
    void testSetReturnDate() {
        LocalDateTime newReturnDate = LocalDateTime.now().plusDays(7);
        transaction.setReturnDate(newReturnDate);
        assertEquals(newReturnDate, transaction.getReturnDate());
    }

    @Test
    void testNullPatron() {
        transaction.setPatron(null);
        assertNull(transaction.getPatron());
    }

    @Test
    void testNullBook() {
        transaction.setBook(null);
        assertNull(transaction.getBook());
    }

    @Test
    void testNullBorrowDate() {
        transaction.setBorrowDate(null);
        assertNull(transaction.getBorrowDate());
    }

    @Test
    void testNullReturnDate() {
        transaction.setReturnDate(null);
        assertNull(transaction.getReturnDate());
    }

    @Test
    void testNegativeId() {
        transaction.setId(-1);
        assertEquals(-1, transaction.getId());
    }
}