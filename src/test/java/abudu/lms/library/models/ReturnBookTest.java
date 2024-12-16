package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

class ReturnBookTest {
    private ReturnBook returnBook;
    private Patron patron;
    private Book book;
    private LocalDateTime returnDate;
    private Set<Role> roles;
    private Set<Book> borrowedBooks;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        borrowedBooks = new HashSet<>();
        patron = new Patron(1, "Abudu", "Sam", "abudu", "abudu@gmail.com", "123456", LocalDateTime.now(), roles, borrowedBooks, 0);
        book = new Book(1, "Java Programming", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1);      //
        returnDate = LocalDateTime.now();
        returnBook = new ReturnBook(1, patron, book, returnDate);
    }

    @Test
    void getId() {
        assert returnBook.getId() == 1;
    }


    @Test
    void setId() {
        returnBook.setId(2);
        assert returnBook.getId() == 2;
    }

    @Test
    void getPatron() {
        assert returnBook.getPatron().equals(patron);
    }

    @Test
    void setPatron() {
        Patron newPatron = new Patron(2, "Sam", "Abudu", "sam", "sam@gmail.com", "123456", LocalDateTime.now(), roles, borrowedBooks, 0);
        returnBook.setPatron(newPatron);
        assert returnBook.getPatron().equals(newPatron);
    }

    @Test
    void getBook() {
        assert returnBook.getBook().equals(book);
    }

    @Test
    void setBook() {
        Book newBook = new Book(2, "Java Programming 2", "John Doe 2", "John Doe 2", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 2", 1);
        returnBook.setBook(newBook);
        assert returnBook.getBook().equals(newBook);
    }

    @Test
    void getReturnDate() {
        assert returnBook.getReturnDate().equals(returnDate);
    }

    @Test
    void setReturnDate() {
        LocalDateTime newReturnDate = LocalDateTime.now();
        returnBook.setReturnDate(newReturnDate);
        assert returnBook.getReturnDate().equals(newReturnDate);
    }
}