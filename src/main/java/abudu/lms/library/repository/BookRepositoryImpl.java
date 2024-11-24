package abudu.lms.library.repository;

import abudu.lms.library.database.DatabaseHandler;

import abudu.lms.library.models.Book;
import abudu.lms.library.models.BookLinkedList;
import abudu.lms.library.models.BookNode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookRepositoryImpl implements BookRepository {
    private final DatabaseHandler dbHandler;
    private final BookLinkedList bookList;

    public BookRepositoryImpl() {
        dbHandler = DatabaseHandler.getInstance();
        bookList = new BookLinkedList();
        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        String query = "SELECT * FROM books";
        try (Connection connection = dbHandler.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year"),
                        resultSet.getInt("isbn"),
                        resultSet.getBoolean("available"),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("description"),
                        resultSet.getLong("user_id")
                );
                bookList.addBook(book);
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while loading books from the database", e);
        }
    }

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, year, isbn, available, category, quantity, description, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getYear());
            pstmt.setInt(5, book.getIsbn());
            pstmt.setBoolean(6, book.isAvailable());
            pstmt.setString(7, book.getCategory());
            pstmt.setInt(8, book.getQuantity());
            pstmt.setString(9, book.getDescription());
            pstmt.setLong(10, book.getUserId());
            pstmt.executeUpdate();

            // Retrieve the generated id
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while adding a book to the database", e);
        }
    }

    @Override
    public void updateBook(Book book) {
        Book existingBook = bookList.findBookById(book.getId());
        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPublisher(book.getPublisher());
            existingBook.setYear(book.getYear());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setAvailable(book.isAvailable());
            existingBook.setCategory(book.getCategory());
            existingBook.setQuantity(book.getQuantity());
            existingBook.setDescription(book.getDescription());
            existingBook.setUserId(book.getUserId());
        }
        String query = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, isbn = ?, available = ?, category = ?, quantity = ?, description = ?, user_id = ? WHERE id = ?";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setInt(5, book.getIsbn());
            statement.setBoolean(6, book.isAvailable());
            statement.setString(7, book.getCategory());
            statement.setInt(8, book.getQuantity());
            statement.setString(9, book.getDescription());
            statement.setLong(10, book.getUserId());
            statement.setInt(11, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while updating a book in the database", e);
        }
    }

    @Override
    public void deleteBook(int bookId) {
        bookList.removeBook(bookId);
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while deleting a book from the database", e);
        }
    }

    @Override
    public Book getBookByIsbn(int bookIsbn) {
        // Implement this method if needed
        return null;
    }

    @Override
    public Book getBookById(int bookId) {
        return bookList.findBookById(bookId);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        BookNode current = bookList.getHead();
        while (current != null) {
            books.add(current.getBook());
            current = current.getNext();
        }
        return books;
    }

    @Override
    public List<Book> searchBooks(String query) {
        // Implement this method if needed

        return List.of();
    }

    @Override
    public boolean returnBook(int id) {
        // Implement this method if needed
        return false;
    }

    @Override
    public boolean borrowBook(int id) {
        // Implement this method if needed
        return false;
    }
}