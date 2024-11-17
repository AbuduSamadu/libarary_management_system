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

    /**
     * Add a new book to the database.
     *
     * @param book The book to be added.
     * @return true if the book was added successfully, false otherwise.
     */

    @Override
    public boolean addBook(Book book) {
        String query = "INSERT INTO books (id,title,author, publihser, year, isbn, avilable ) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while adding a book to the database", e);
            return false;
        }
    }


    /**
     * Update a book in the database.
     *
     * @param book The book to be updated.
     * @return true if the book was updated successfully, false otherwise.
     */

    @Override
    public boolean updateBook(Book book) {
        String query = "UPDATE books SET title = ?, author = ?, isbn = ?, publishedDate = ? WHERE id = ?";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getYear());
            statement.setString(6, book.getIsbn());
            statement.setBoolean(7, book.isAvailable());
            statement.setInt(5, book.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while updating a book in the database", e);
            return false;
        }
    }

    /**
     * Delete a book from the database.
     *
     * @param bookId The ID of the book to be deleted.
     * @return true if the book was deleted successfully, false otherwise.
     */

    @Override
    public boolean deleteBook(int bookId) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = dbHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while deleting a book from the database", e);
            return false;
        }
    }

    /**
     * Get a book from the database by its ISBN.
     *
     * @param bookIsbn The ISBN of the book to be retrieved.
     * @return The book with the specified ISBN, or null if the book is not found.
     */

    @Override
    public Book getBookByIsbn(int bookIsbn) {
        return null;
    }

    /**
     * Get a book from the database by its ID.
     *
     * @param bookId The ID of the book to be retrieved.
     * @return The book with the specified ID, or null if the book is not found.
     */

    @Override
    public Book getBookById(int bookId) {
        String query = "SELECT * FROM books WHERE  = ?";
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
                        resultSet.getBoolean("available")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving a book from the database", e);
        }
        return null;
    }

    /**
     * Get all books from the database.
     *
     * @return A list of all books in the database.
     */

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
                        resultSet.getBoolean("available")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving all books from the database", e);
        }
        return books;
    }

    /**
     * Search for books in the database.
     *
     * @param query The search query.
     * @return A list of books that match the search query.
     */

    @Override
    public List<Book> searchBooks(String query) {
        return List.of();
    }
}