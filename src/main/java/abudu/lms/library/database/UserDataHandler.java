package abudu.lms.library.database;

import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserDataHandler {
    private final DatabaseHandler dbHandler;

    // Constructor
    public UserDataHandler() {
        dbHandler = DatabaseHandler.getInstance();
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
        if (isInvalidInput(firstName, lastName, username, email, password)) {
            return "Invalid input";
        }
        if (isEmailExists(email)) {
            return "Email already exists"; // Email check
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        boolean userCreated = addUser(firstName, lastName, username, email, hashedPassword, LocalDateTime.now(), roles);

        return userCreated ? "User registered successfully" : "Registration failed";
    }

    // Check if an email already exists
    private boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Check if count > 0
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDataHandler.class.getName()).log(Level.SEVERE, "An error occurred while checking if email exists", e);
        }
        return false;
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
        return executeUpdate(query, firstName, lastName, username, email, hashedPassword, createdAt, roles.iterator().next().name());
    }

    /**
     * Retrieve a user by their email.
     *
     * @param email the email
     * @return a User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User getUserByEmail(String email) throws SQLException {
        final String query = "SELECT id, first_name, last_name, username, email, password, created_at, role FROM users WHERE email = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        }
        return null;
    }

    /**
     * Update user details in the database.
     *
     * @param user the User object with updated data
     * @return true if the update is successful, false otherwise
     */
    public boolean updateUser(User user) {
        final String query = "UPDATE users SET first_name = ?, last_name = ?, username = ?, email = ?, password = ? WHERE id = ?";
        return executeUpdate(query, user.getFirstName(), user.getLastName(), user.getName(), user.getEmail(), user.getPassword(), user.getId());
    }

    /**
     * Delete a user from the database by their ID.
     *
     * @param id the user ID
     * @return true if the user is deleted, false otherwise
     */
    public boolean deleteUser(int id) {
        final String query = "DELETE FROM users WHERE id = ?";
        return executeUpdate(query, id);
    }

    // ====================
    // Helper Methods
    // ====================

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
            Logger.getLogger(UserDataHandler.class.getName()).log(Level.SEVERE, "An error occurred while executing an update query", e);
            return false;
        }
    }

    /**
     * Map a ResultSet to a User object.
     *
     * @param resultSet the ResultSet
     * @return a User object
     * @throws SQLException if a database error occurs
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                Set.of(Role.valueOf(resultSet.getString("role")))
        );
    }

    /**
     * Validate input for user registration.
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
     * Set parameters on a PreparedStatement.
     *
     * @param stmt   the PreparedStatement
     * @param params the parameters
     * @throws SQLException if a database error occurs
     */
    private void setPreparedStatementParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
}