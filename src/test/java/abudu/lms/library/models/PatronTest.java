package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatronTest {
    private Patron patron;
    private Set<Book> borrowedBooks;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        borrowedBooks = Set.of(new Book(1, "Java Programming", "John Doe", "John Doe", 2021, 1234567890, true, "Programming", 10, "Java Programming Book", 1));
        double fines = 100;
        patron = new Patron(1, "John", "Doe", "John Doe", "John Doe@gmail.com", "123456", LocalDateTime.now(), roles, borrowedBooks, fines);

    }

    @Test
    void getId() {
        assertEquals(1, patron.getId());
    }

    @Test
    void setId() {
        patron.setId(2);
        assertEquals(2, patron.getId());
    }

    @Test
    void getFirstName() {
        assertEquals("John", patron.getFirstName());
    }

    @Test
    void setFirstName() {
        patron.setFirstName("Jane");
        assertEquals("Jane", patron.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Doe", patron.getLastName());
    }

    @Test
    void setLastName() {
        patron.setLastName("Jane");
        assertEquals("Jane", patron.getLastName());
    }

    @Test
    void getName() {
        assertEquals("John Doe", patron.getName());
    }

    @Test
    void setName() {
        patron.setName("Jane Doe");
        assertEquals("Jane Doe", patron.getName());
    }

    @Test
    void getEmail() {
        assertEquals("John Doe@gmail.com", patron.getEmail());
    }

    @Test
    void setEmail() {
        patron.setEmail("Jane Doe@gmail.com");
        assertEquals("Jane Doe@gmail.com", patron.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals("123456", patron.getPassword());   //
    }

    @Test
    void setPassword() {
        patron.setPassword("1234567");
        assertEquals("1234567", patron.getPassword());
    }


    @Test
    void getRoles() {
        assertEquals(roles, patron.getRoles());
    }

    @Test
    void setRoles() {
        roles.add(new Role(2, ERole.Patron));
        patron.setRoles(roles);
        assertEquals(roles, patron.getRoles());
    }

    @Test
    void getBorrowedBooks() {
        assertEquals(borrowedBooks, patron.getBorrowedBooks());
    }

    @Test
    void getFines() {
        assertEquals(100, patron.getFines());
    }

    @Test
    void setFines() {
        patron.setFines(200);
        assertEquals(200, patron.getFines());
    }
}