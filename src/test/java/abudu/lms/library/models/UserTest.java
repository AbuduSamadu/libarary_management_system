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
        user = new User(1, "John", "Doe", "John Doe", "John Doe@gmail.com", "123456", LocalDateTime.now(), roles);

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
        assertEquals("John", user.getFirstName());
    }

    @Test
    void setFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void setLastName() {
        user.setLastName("Jane");
        assertEquals("Jane", user.getLastName());
    }

    @Test
    void getName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    void setName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    void getEmail() {
        assertEquals("John Doe@gmail.com", user.getEmail());
    }

    @Test
    void setEmail() {
        user.setEmail("Jane XXXXXXXXXXXXX");
        assertEquals("Jane XXXXXXXXXXXXX", user.getEmail());
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
    void getRoles() {
        assertEquals(roles, user.getRoles());
    }

    @Test
    void setRoles() {
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

}