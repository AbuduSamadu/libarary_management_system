package abudu.lms.library.database;

import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDataHandlerIntegrationTest {

    private UserDataHandler userDataHandler;
    private DatabaseHandler databaseHandler;

    @BeforeAll
    public void setUp() throws SQLException {
        // Ensure a clean test database state
        userDataHandler = new UserDataHandler();
        databaseHandler = DatabaseHandler.getInstance();

        // Setup test database schema and clear existing data
        setupTestDatabase();
    }

    private void setupTestDatabase() throws SQLException {
        try (Connection connection = databaseHandler.getConnection()) {
            // Drop existing users table if it exists
            try (PreparedStatement dropstmt = connection.prepareStatement("DROP TABLE IF EXISTS users CASCADE")) {
                dropstmt.execute();
            }

            // Create users table with same schema as production
            String createTableQuery = """
                        CREATE TABLE users (
                            id SERIAL PRIMARY KEY,
                            first_name VARCHAR(50) NOT NULL,
                            last_name VARCHAR(50) NOT NULL,
                            username VARCHAR(50) UNIQUE NOT NULL,
                            email VARCHAR(100) UNIQUE NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP NOT NULL,
                            role VARCHAR(50) NOT NULL
                        )
                    """;

            try (PreparedStatement createTableStmt = connection.prepareStatement(createTableQuery)) {
                createTableStmt.execute();
            }
        }
    }

    @Test
    public void testAddUser_ValidInput_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe";
        String email = "john.doe@example.com";
        String hashedPassword = "hashed_password_123";
        LocalDateTime createdAt = LocalDateTime.now();
        Set<Role> roles = Set.of(new Role(ERole.Librarian.ordinal(), ERole.Librarian));

        // Act
        boolean result = userDataHandler.addUser(
                firstName, lastName, username, email,
                hashedPassword, createdAt, roles
        );

        // Assert
        assertTrue(result, "User should be added successfully");
        assertEquals(1, userDataHandler.countUsers(), "User count should be 1");
    }

    @Test
    public void testAddUser_DuplicateEmail_Failure() {
        // Arrange
        String email = "duplicate@example.com";
        LocalDateTime createdAt = LocalDateTime.now();
        Set<Role> roles = Set.of(new Role(ERole.ROLE_USER.ordinal(), ERole.ROLE_USER));

        // First user
        userDataHandler.addUser(
                "First", "User", "firstuser", email,
                "password1", createdAt, roles
        );

        // Attempt to add second user with same email
        boolean result = userDataHandler.addUser(
                "Second", "User", "seconduser", email,
                "password2", createdAt, roles
        );

        // Assert
        assertFalse(result, "Adding user with duplicate email should fail");
    }

    @Test
    public void testGetUserByEmail_ExistingUser_Success() {
        // Arrange
        String email = "retrieve@example.com";
        String firstName = "Retrieve";
        String lastName = "User";
        String username = "retrieveuser";
        String hashedPassword = "hashed_password";
        LocalDateTime createdAt = LocalDateTime.now();
        Set<Role> roles = Set.of(new Role(ERole.Librarian.ordinal(), ERole.Librarian));

        // Add user first
        userDataHandler.addUser(
                firstName, lastName, username, email,
                hashedPassword, createdAt, roles
        );

        // Act
        User retrievedUser = userDataHandler.getUserByEmail(email);

        // Assert
        assertNotNull(retrievedUser, "User should be retrieved");
        assertEquals(email, retrievedUser.getEmail(), "Email should match");
        assertEquals(firstName, retrievedUser.getFirstName(), "First name should match");
        assertEquals(lastName, retrievedUser.getLastName(), "Last name should match");
    }

    @Test
    public void testGetUserByEmail_NonExistentUser_ReturnsNull() {
        // Act
        User retrievedUser = userDataHandler.getUserByEmail("nonexistent@example.com");

        // Assert
        assertNull(retrievedUser, "Non-existent user should return null");
    }

    @Test
    public void testIsValidInput_InvalidInputs() {
        // Test various invalid input scenarios
        String[][] invalidInputs = {
                {null, "Doe", "johndoe", "john@example.com", "password"},    // Null first name
                {"John", null, "johndoe", "john@example.com", "password"},   // Null last name
                {"John", "Doe", null, "john@example.com", "password"},       // Null username
                {"John", "Doe", "johndoe", null, "password"},                // Null email
                {"John", "Doe", "johndoe", "john@example.com", null}         // Null password
        };

        for (String[] input : invalidInputs) {
            boolean isInvalid = userDataHandler.isValidInput(
                    input[0], input[1], input[2], input[3], input[4]
            );

            assertTrue(isInvalid, "Input should be considered invalid");
        }
    }

    @Test
    public void testIsValidEmail_ValidEmails() {
        String[] validEmails = {
                "user@example.com",
                "user.name@example.co.uk",
                "user+tag@example.org",
                "firstname.lastname@example.net"
        };

        for (String email : validEmails) {
            assertTrue(userDataHandler.isValidEmail(email),
                    "Email '" + email + "' should be considered valid");
        }
    }

    @Test
    public void testIsValidEmail_InvalidEmails() {
        String[] invalidEmails = {
                "invalid-email",
                "user@.com",
                "@example.com",
                "user@example",
                null
        };

        for (String email : invalidEmails) {
            assertFalse(userDataHandler.isValidEmail(email),
                    "Email '" + email + "' should be considered invalid");
        }
    }

    @Test
    public void testCountUsers_EmptyDatabase_ReturnsZero() {
        // Clear database before test
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM users")) {
            deleteStmt.execute();
        } catch (SQLException e) {
            fail("Error clearing database: " + e.getMessage());
        }

        // Act
        int userCount = userDataHandler.countUsers();

        // Assert
        assertEquals(0, userCount, "User count should be zero in an empty database");
    }

    @AfterEach
    public void clearDatabase() throws SQLException {
        // Clear users table after each test to ensure clean state
        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM users")) {
            deleteStmt.execute();
        }
    }
}