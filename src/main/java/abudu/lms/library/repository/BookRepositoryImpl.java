package abudu.lms.library.repository;

import abudu.lms.library.database.DatabaseHandler;
import abudu.lms.library.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookRepositoryImpl implements BookRepository {
    private final DatabaseHandler dbHandler;

    public BookRepositoryImpl() {
        dbHandler = DatabaseHandler.getInstance();
    }

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (id,title, author, publisher, year, isbn, available, category, quantity, description, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = dbHandler.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getPublisher());
            stmt.setInt(5, book.getYear());
            stmt.setString(6, book.getIsbn());
            stmt.setBoolean(7, book.isAvailable());
            stmt.setString(8, book.getCategory());
            stmt.setInt(9, book.getQuantity());
            stmt.setString(10, book.getDescription());
            stmt.setLong(11, book.getUserId()); // Assuming user_id is a long
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBook(Book book) {
        String query = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, isbn = ?, available = ? WHERE id = ?";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getYear());
            statement.setString(6, book.getIsbn());
            statement.setBoolean(7, book.isAvailable());
            statement.setString(8, book.getCategory());
            statement.setInt(9, book.getQuantity());
            statement.setString(10, book.getDescription());
            statement.setLong(11, book.getUserId()); // Assuming user_id is a long



            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while updating a book in the database", e);
        }
    }

    @Override
    public void deleteBook(int bookId) {
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
        return null;
    }

    @Override
    public Book getBookById(int bookId) {
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year"),
                        resultSet.getString("isbn"),
                        resultSet.getBoolean("available"),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("description"),
                        resultSet.getLong("user_id") // Assuming user_id is a long

                );
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving a book from the database", e);
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection connection = dbHandler.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("year"),
                        resultSet.getString("isbn"),
                        resultSet.getBoolean("available"),
                        resultSet.getString("category"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("description"),
                        resultSet.getLong("user_id") // Assuming user_id is a long
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving all books from the database", e);
        }
        return books;
    }

    @Override
    public List<Book> searchBooks(String query) {
        return List.of();
    }

    @Override
    public boolean returnBook(int id) {
        return false;
    }

    @Override
    public boolean borrowBook(int id) {
        return false;
    }
}