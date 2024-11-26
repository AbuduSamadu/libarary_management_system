package abudu.lms.library.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private static final Logger LOGGER = Logger.getLogger(DatabaseHandler.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/library";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Abudu?0248";

    private Connection connection;

    private DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to the database", e);
            throw new RuntimeException("Database connection error", e);
        }
    }

    private static class SingletonHelper {
        private static final DatabaseHandler INSTANCE = new DatabaseHandler();
    }

    public static DatabaseHandler getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to reconnect to the database", e);
            throw new RuntimeException("Database reconnection error", e);
        }
        return connection;
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeQuery();
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params);
            statement.executeUpdate();
        }
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
