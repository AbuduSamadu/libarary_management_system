package abudu.lms.library.repository;

import abudu.lms.library.models.Book;

import java.util.List;

/**
 * This interface defines the methods that must be implemented by a class that will interact with the book repository.
 */


public interface BookRepository {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book getBookByIsbn(int bookIsbn);

    Book getBookById(int bookId);

    List<Book> getAllBooks();
    List<Book> searchBooks(String query);

    boolean returnBook(int id);

    boolean borrowBook(int id);
}