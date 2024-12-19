package abudu.lms.library.utils;

import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {
    private UserSession userSession;
    private User mockUser;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, ERole.Patron));
        userSession = UserSession.getInstance();
        mockUser = new User(1, "John", "Doe", "John Doe", "John Doe@gmail.com", "123456", LocalDateTime.now(), roles);
    }

    @Test
    void testSingletonInstance() {
        UserSession instance1 = UserSession.getInstance();
        UserSession instance2 = UserSession.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testSetCurrentUser() {
        userSession.setCurrentUser(mockUser);
        assertEquals(mockUser, userSession.getCurrentUser());
    }

    @Test
    void testGetCurrentUserWithin30Minutes() {
        userSession.setCurrentUser(mockUser);
        assertEquals(mockUser, userSession.getCurrentUser());
    }


    @Test
    void testClearSession() {
        userSession.setCurrentUser(mockUser);
        userSession.clearSession();
        assertNull(userSession.getCurrentUser());
    }

    @Test
    void testSetInstance() {
        UserSession mockSession = new UserSession();
        UserSession.setInstance(mockSession);
        assertSame(mockSession, UserSession.getInstance());
    }
}