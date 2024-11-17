package abudu.lms.library.database;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepository;
import abudu.lms.library.repository.BookRepositoryImpl;

import java.util.List;

/**
 * This class is responsible for handling the data of the book repository.
 */

public class BookDataHandler {
    private final BookRepository bookRepository;

    public BookDataHandler() {
        this.bookRepository = new BookRepositoryImpl();
    }

    public boolean addBook(Book book) {
        return bookRepository.addBook(book);
    }

    public boolean updateBook(Book book) {
        return bookRepository.updateBook(book);
    }

    public boolean deleteBook(int bookId) {
        return bookRepository.deleteBook(bookId);
    }

    public Book getBookById(int bookId) {
        return bookRepository.getBookById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }
}