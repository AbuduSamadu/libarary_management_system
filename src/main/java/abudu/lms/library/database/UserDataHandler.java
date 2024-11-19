package abudu.lms.library.database;

import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDataHandler {

    private final DatabaseHandler dbHandler;

    // Constructor
    public UserDataHandler() {
        this.dbHandler = DatabaseHandler.getInstance();
    }

    /**
     * Register a new user.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param username  the username
     * @param email     the email
     * @param password  the raw password
     * @param roles     the roles
     * @return a message indicating if registration is successful or not
     */
    public String registerUser(String firstName, String lastName, String username, String email, String password, Set<Role> roles) {
        // Validate input
        if (!validateInput(firstName, lastName, username, email, password)) {
            return "Invalid input: Ensure all fields are filled correctly.";
        }

        // Check email existence
        User existingUser = getUserByEmail(email);
        if (existingUser != null) {
            return "Email already exists.";
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Add user
        if (addUser(firstName, lastName, username, email, hashedPassword, LocalDateTime.now(), roles)) {
            return "User registered successfully.";
        }

        return "Registration failed.";
    }

    private boolean validateInput(String firstName, String lastName, String username, String email, String password) {
        return firstName != null && !firstName.isEmpty() &&
                lastName != null && !lastName.isEmpty() &&
                username != null && !username.isEmpty() &&
                email != null && email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$") &&
                password != null && !password.isEmpty();
    }


    /**
     * Add a new user to the database.
     *
     * @param firstName      the first name
     * @param lastName       the last name
     * @param username       the username
     * @param email          the email
     * @param hashedPassword the hashed password
     * @param createdAt      the creation timestamp
     * @param roles          the roles
     * @return true if the user is added successfully, false otherwise
     */
    public boolean addUser(String firstName, String lastName, String username, String email, String hashedPassword, LocalDateTime createdAt, Set<Role> roles) {
        final String query = "INSERT INTO users (first_name, last_name, username, email, password, created_at, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, firstName, lastName, username, email, hashedPassword, createdAt, roles.iterator().next().getName());
    }

    /**
     * Retrieve a user by their email.
     *
     * @param email the email to search for
     * @return a User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        final String query = "SELECT id, first_name, last_name, username, email, password, created_at, role FROM users WHERE email = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDataHandler.class.getName())
                    .log(Level.SEVERE, "Error retrieving user by email: " + email, e);
        }
        return null;
    }

    /**
     * Map a ResultSet to a User object.
     *
     * @param resultSet the ResultSet containing user data
     * @return a User object with populated fields
     * @throws SQLException if a database error occurs
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        try {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
            String roleName = resultSet.getString("role");
            ERole role = ERole.valueOf(roleName); // Map string to ERole enum

            Set<Role> roles = Set.of(new Role(role.ordinal(), role));

            return new User(id, firstName, lastName, username, email, password, createdAt, roles);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Invalid role in the database for user: " + resultSet.getString("email"), e);
        }
    }

    /**
     * Validate user input for registration.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param username  the username
     * @param email     the email
     * @param password  the password
     * @return true if any input is invalid, false otherwise
     */
    private boolean isInvalidInput(String firstName, String lastName, String username, String email, String password) {
        return firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty();
    }

    /**
     * Validate email format.
     *
     * @param email the email
     * @return true if the email format is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Execute an update query (INSERT, UPDATE, DELETE) with parameters.
     *
     * @param query  the SQL query
     * @param params the parameters for the query
     * @return true if the query executes successfully, false otherwise
     */
    private boolean executeUpdate(String query, Object... params) {
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            setPreparedStatementParameters(stmt, params);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            logSQLException("Error executing update query", e);
            return false;
        }
    }

    /**
     * Log SQLException with a specific context.
     *
     * @param context the context of the exception
     * @param e       the SQLException
     */
    private void logSQLException(String context, SQLException e) {
        Logger.getLogger(UserDataHandler.class.getName())
                .log(Level.SEVERE, context + ": " + e.getMessage(), e);
    }

    /**
     * Set parameters on a PreparedStatement.
     *
     * @param stmt   the PreparedStatement
     * @param params the parameters
     * @throws SQLException if a database error occurs
     */
    private void setPreparedStatementParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof ERole) {
                stmt.setString(i + 1, ((ERole) params[i]).name());
            } else {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }
}