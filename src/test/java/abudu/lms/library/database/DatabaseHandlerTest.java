package abudu.lms.library.database;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DatabaseHandlerTest {
    private DatabaseHandler databaseHandler;
    private Connection mockConnection;
    private PreparedStatement mockStatement;

    @BeforeEach
    void setUp() throws SQLException {
        mockStatement = mock(PreparedStatement.class);
        mockConnection = mock(Connection.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class);
             MockedStatic<Dotenv> mockedDotenv = mockStatic(Dotenv.class)) {

            Dotenv mockDotenv = mock(Dotenv.class);
            when(mockDotenv.get("DB_URL")).thenReturn("jdbc:h2:mem:test");
            when(mockDotenv.get("DB_USER")).thenReturn("sa");
            when(mockDotenv.get("DB_PASSWORD")).thenReturn("");

            mockedDotenv.when(Dotenv::load).thenReturn(mockDotenv);
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

            databaseHandler = DatabaseHandler.getInstance();
        }
    }

    @Test
    void testGetInstance() throws SQLException {
        assertNotNull(databaseHandler);
        verify(mockConnection).isClosed();
        verify(mockConnection).isValid(2);
    }

    @Test
    void testGetConnection() throws SQLException {
        Connection connection = databaseHandler.getConnection();
        assertNotNull(connection);
        verify(mockConnection).isClosed();
        verify(mockConnection).isValid(2);
    }

    @Test
    void testExecuteQuery() throws SQLException {
        String query = "SELECT * FROM books";
        ResultSet resultSet = databaseHandler.executeQuery(query);
        assertNotNull(resultSet);
        verify(mockConnection).prepareStatement(query);
        verify(mockStatement).executeQuery();
    }

    @Test
    void testExecuteUpdate() throws SQLException {
        String query = "UPDATE books SET title = ? WHERE id = ?";
        databaseHandler.executeUpdate(query, "Java Programming", 1);
        verify(mockStatement).setObject(1, "Java Programming");
        verify(mockStatement).setObject(2, 1);
        verify(mockConnection).prepareStatement(query);
        verify(mockStatement).executeUpdate();
    }
}