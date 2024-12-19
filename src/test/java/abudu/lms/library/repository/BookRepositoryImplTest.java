package abudu.lms.library.repository;

import abudu.lms.library.database.DatabaseHandler;
import abudu.lms.library.models.Book;
import abudu.lms.library.models.BookLinkedList;
import abudu.lms.library.models.BookNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookRepositoryImplTest {
    private BookRepositoryImpl bookRepository;
    private DatabaseHandler dbHandler;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private BookLinkedList bookList;

    @BeforeEach
    void setUp() throws Exception {
        dbHandler = mock(DatabaseHandler.class);
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        bookList = mock(BookLinkedList.class);

        when(dbHandler.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        bookRepository = new BookRepositoryImpl();
        bookRepository.setDatabaseHandler(dbHandler);
        bookRepository.setBookList(bookList);
    }

    @Test
    void testAddBook() throws Exception {
        Book book = new Book(1, "Title", "Author", "Publisher", 2021, 1234567890, true, "Category", 1, "Description", 1);
        bookRepository.addBook(book);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(bookList, times(1)).addBook(book);
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book(1, "Title", "Author", "Publisher", 2021, 1234567890, true, "Category", 1, "Description", 1);
        when(bookList.findBookById(1)).thenReturn(book);
        bookRepository.updateBook(book);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(bookList, times(1)).findBookById(1);
    }

    @Test
    void testDeleteBook() throws Exception {
        bookRepository.deleteBook(1);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(bookList, times(1)).removeBook(1);
    }

    @Test
    void testGetBookById() {
        Book book = new Book(1, "Title", "Author", "Publisher", 2021, 1234567890, true, "Category", 1, "Description", 1);
        when(bookList.findBookById(1)).thenReturn(book);
        Book result = bookRepository.getBookById(1);
        assertEquals(book, result);
    }

    @Test
    void testGetAllBooks() {
        BookNode node1 = new BookNode(new Book(1, "Title1", "Author1", "Publisher1", 2021, 1234567890, true, "Category1", 1, "Description1", 1));
        BookNode node2 = new BookNode(new Book(2, "Title2", "Author2", "Publisher2", 2022, 1234567891, true, "Category2", 2, "Description2", 2));
        when(bookList.getHead()).thenReturn(node1);
        node1.setNext(node2);
        List<Book> books = bookRepository.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void testCountBooks() throws Exception {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        int count = bookRepository.countBooks();
        assertEquals(5, count);
    }

    @Test
    void testReserveBook() throws Exception {
        Book book = new Book(1, "Title", "Author", "Publisher", 2021, 1234567890, true, "Category", 1, "Description", 1);
        when(bookList.findBookById(1)).thenReturn(book);
        bookRepository.reserveBook(1);
        verify(preparedStatement, times(1)).executeUpdate();
    }
}