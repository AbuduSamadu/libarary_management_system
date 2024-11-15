package abudu.lms.library.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import abudu.lms.library.models.User;

public class UserDataHandler {
    private final DatabaseHandler dbHandler;

    public UserDataHandler() {
        dbHandler = DatabaseHandler.getInstance();
    }

    // Register a new user
    public boolean registerUser(String name, String email, String password) {
        if (isInvalidInput(name, email, password)) {
            return false; // Validation failed
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return addUser(name, email, hashedPassword);
    }

    // Add new user to the database
    public boolean addUser(String name, String email, String password) {
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        return executeUpdate(query, name, email, password);
    }

    // Get user by email
    public abudu.lms.library.models.User getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    // Update user details
    public boolean updateUser(User user) {
        String query = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        return executeUpdate(query, user.getName(), user.getEmail(), user.getPassword(), user.getId());
    }

    // Delete user
    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        return executeUpdate(query, id);
    }

    // Helper method to execute update queries
    private boolean executeUpdate(String query, Object... params) {
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to validate input
    private boolean isInvalidInput(String name, String email, String password) {
        return name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty();
    }
}