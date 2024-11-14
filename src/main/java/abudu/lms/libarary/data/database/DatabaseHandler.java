package abudu.lms.libarary.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private static DatabaseHandler instance;
    private Connection connection;

    private DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeQuery();
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeUpdate();
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
