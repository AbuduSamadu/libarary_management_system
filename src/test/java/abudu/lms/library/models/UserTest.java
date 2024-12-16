package abudu.lms.library.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private User user;

    private Set<Role> roles;


    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        user = new User(1, "Abudu", "Sam", "abudu", "abudu@gmail.com", "123456", LocalDateTime.now(), roles);

    }

    @Test
    void getId() {
        assertEquals(1, user.getId());
    }

    @Test
    void setId() {
        user.setId(2);
        assertEquals(2, user.getId());
    }

    @Test
    void getFirstName() {
        assertEquals("Abudu", user.getFirstName());
    }

    @Test
    void setFirstName() {
        user.setFirstName("Sam");
        assertEquals("Sam", user.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Sam", user.getLastName());
    }

    @Test
    void setLastName() {
        user.setLastName("Abudu");
        assertEquals("Abudu", user.getLastName());
    }

    @Test
    void getName() {
        assertEquals("Abudu Sam", user.getName());
    }

    @Test
    void setName() {
        user.setName("Sam Abudu");
        assertEquals("Sam Abudu", user.getName());
    }

    @Test
    void getEmail() {
        assertEquals("abudu@gmail.com", user.getEmail());
    }


    @Test
    void setEmail() {
        user.setEmail("abudu@gmail.com");
        assertEquals("abudu@gmail.com", user.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals("123456", user.getPassword());
    }

    @Test
    void setPassword() {
        user.setPassword("123456");
        assertEquals("123456", user.getPassword());
    }

    @Test
    void getCreatedAt() {
        assertEquals(LocalDateTime.now(), user.getCreatedAt());
    }

    @Test
    void setCreatedAt() {
        LocalDateTime time = LocalDateTime.now();
        user.setCreatedAt(time);
        assertEquals(time, user.getCreatedAt());
    }

    @Test
    void getRoles() {
        assertEquals(roles, user.getRoles());
    }

    @Test
    void setRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testToString() {
        assertEquals("User{id=1, firstName='Abudu', lastName='Sam', username='abudu', email='abudu@gmail.com', role='[Patron]', createdAt='2023-01-01T00:00:00'}", user.toString());
    }
}