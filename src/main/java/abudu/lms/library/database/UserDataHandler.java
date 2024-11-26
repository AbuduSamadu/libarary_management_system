package abudu.lms.library.database;

import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;

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


    public boolean addUser(String firstName, String lastName, String username, String email, String hashedPassword, LocalDateTime createdAt, Set<Role> roles) {
        final String query = "INSERT INTO users (first_name, last_name, username, email, password, created_at, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            dbHandler.executeUpdate(query, firstName, lastName, username, email, hashedPassword, createdAt, roles.iterator().next().getName());
            return true;
        } catch (SQLException e) {
            Logger.getLogger(UserDataHandler.class.getName()).log(Level.SEVERE, "Error adding user: " + email, e);
            return false;
        }
    }

    /**
     * Retrieve a user by their email.
     *
     * @param email the email to search for
     * @return a User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        final String query = "SELECT id, first_name, last_name, username, email, password, created_at, role FROM users WHERE email = ?";
        try (ResultSet resultSet = dbHandler.executeQuery(query, email)) {
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
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
    public boolean isValidInput(String firstName, String lastName, String username, String email, String password) {
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
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
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