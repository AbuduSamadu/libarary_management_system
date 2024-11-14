package abudu.lms.libarary.data.database;

import javax.swing.tree.RowMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private static DataHandler instance;
    private Connection connection;

    private DataHandler() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DataHandler getInstance() {
        if (instance == null) {
            instance = new DataHandler();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Generic create (insert)
    public int create(String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        setParameters(statement, params);
        int affectedRows = statement.executeUpdate();

        if (affectedRows > 0) {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // Return generated ID
            }
        }
        return -1;
    }

    // Generic read
    public <T> List<T> read(String query, RowMapper<T> mapper, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, params);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            results.add(mapper.mapRow(resultSet));
        }
        return results;
    }

    // Generic update
    public int update(String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeUpdate();
    }

    // Generic delete
    public int delete(String query, Object... params) throws SQLException {
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
