package abudu.lms.library.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DatabaseHandlerTest {
    //Arrange
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private DatabaseHandler databaseHandler;

    @BeforeEach
    void setUp() throws SQLException {
        // Deregister any existing static mock
        Mockito.framework().clearInlineMocks();

        // Mocking the database components
        mockStatement = mock(PreparedStatement.class);
        mockConnection = mock(Connection.class);
        mockResultSet = mock(ResultSet.class);

        // Mocking the DriverManager class
        Mockito.mockStatic(DriverManager.class);
        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);

        // Common behavior
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);

        databaseHandler = DatabaseHandler.getInstance();
    }

    @Test
    void testGetConnection() throws SQLException {
        when(mockConnection.isValid(2)).thenReturn(true);

        Connection connection = databaseHandler.getConnection();

        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(2);
    }

    @Test
    void testExecuteQuery() throws SQLException {

        String query = "SELECT * FROM books WHERE id = ?";
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet resultSet = databaseHandler.executeQuery(query, 1);

        assertNotNull(resultSet);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(mockStatement).setObject(eq(1), captor.capture());
        assertEquals(1, captor.getValue());

        verify(mockStatement).executeQuery();
    }

    @Test
    void testExecuteUpdate() throws SQLException {
        //Arrange
        String query = "Update books SET title = ? WHERE id = ?";

        //Act
        databaseHandler.executeUpdate(query, "New Title");

        //Assert
        verify(mockStatement).setObject(1, "New Title");
        verify(mockStatement).executeUpdate();
    }


    @Test
    void testExecuteQueryWithMultipleParameters() throws SQLException {
        String query = "SELECT * FROM books WHERE id = ? AND title = ?";
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        ResultSet resultSet = databaseHandler.executeQuery(query, 1, "Book Title");

        assertNotNull(resultSet);

        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockStatement).setObject(eq(1), intCaptor.capture());
        verify(mockStatement).setObject(eq(2), stringCaptor.capture());

        assertEquals(1, intCaptor.getValue());
        assertEquals("Book Title", stringCaptor.getValue());

        verify(mockStatement).executeQuery();
    }

    @Test
    void testSingletonBehavior() throws SQLException {
        DatabaseHandler instance1 = DatabaseHandler.getInstance();
        DatabaseHandler instance2 = DatabaseHandler.getInstance();

        assertEquals(instance1, instance2);
    }

    @Test
    void testReconnectWhenConnectionIsClosed() throws SQLException {
        //Arrange
        when(mockConnection.isClosed()).thenReturn(true);

        //Act
        Connection connection = databaseHandler.getConnection();

        //Assert
        assertNotNull(connection);
        verify(mockConnection, times(1)).isClosed();
        verify(DriverManager.class);
    }

    @Test
    void testReconnectWhenConnectionIsInValid() throws SQLException {
        //Arrange
        when(mockConnection.isValid(2)).thenReturn(false);

        //Act
        Connection connection = databaseHandler.getConnection();

        //Assert
        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(2);
        verify(DriverManager.class);
    }


}