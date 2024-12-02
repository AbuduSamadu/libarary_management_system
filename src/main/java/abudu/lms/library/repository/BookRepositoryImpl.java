package abudu.lms.library.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import abudu.lms.library.database.DatabaseHandler;
import abudu.lms.library.models.Book;
import abudu.lms.library.models.BookLinkedList;
import abudu.lms.library.models.BookNode;

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
        bookList.addBook(book);
        String sql = "INSERT INTO books (title, author, publisher, year, isbn, available, category, quantity, description, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
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
    public boolean returnBook(int id) {
        // Implement this method if needed
        return false;
    }



    @Override
    public boolean borrowBook(int bookId) {
        Book book = getBookById(bookId);
        if (book == null || !book.isAvailable()) {
            return false;
        }

        int newQuantity = book.getQuantity() - 1;
        boolean isAvailable = newQuantity > 0;
        book.setQuantity(newQuantity);
        book.setAvailable(isAvailable);
        updateBook(book);

        String query = "UPDATE books SET available = false WHERE id = ?";
        String insertBorrowing = "INSERT INTO borrowings (title, author, isbn, user_id, borrow_date, notes, active) VALUES (?, ?, ?, ?, CURRENT_DATE, '', true)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(query);
             PreparedStatement insertStmt = conn.prepareStatement(insertBorrowing)) {

            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();

            insertStmt.setString(1, book.getTitle());
            insertStmt.setString(2, book.getAuthor());
            insertStmt.setLong(3, book.getIsbn());
            insertStmt.setLong(4, book.getUserId());
            insertStmt.executeUpdate();

            book.setAvailable(false);
            return true;
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while borrowing a book", e);
            return false;
        }
    }

    @Override
    public int countBooks() {
        String query = "SELECT COUNT(*) FROM books";
        try (Connection connection = dbHandler.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while counting books in the database", e);
        }
        return 0;
    }

    @Override
    public void reserveBook(int id) {

        Book book = getBookById(id);
        if (book == null || !book.isAvailable()) {
            return;
        }
        int newQuantity = book.getQuantity() - 1;
        book.setQuantity(newQuantity);
        book.setAvailable(newQuantity > 0);
        updateBook(book);

        // Implement this method if needed
        String query = "UPDATE books SET reserved = true WHERE id = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(BookRepositoryImpl.class.getName()).log(Level.SEVERE, "An error occurred while reserving a book", e);
        }
    }
}