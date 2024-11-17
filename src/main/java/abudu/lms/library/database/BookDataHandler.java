package abudu.lms.library.database;

import java.util.List;

import abudu.lms.library.models.Book;

import java.util.List;

import abudu.lms.library.repository.BookRepository;
import abudu.lms.library.repository.BookRepositoryImpl;

public class BookDataHandler {

    private final BookRepository bookRepository;

    public BookDataHandler() {
        this.bookRepository = new BookRepository() {
            @Override
            public boolean addBook(Book book) {
                return false;
            }

            @Override
            public boolean updateBook(Book book) {
                return false;
            }

            @Override
            public boolean deleteBook(int bookId) {
                return false;
            }

            @Override
            public Book getBookByIsbn(int bookIsbn) {
                return null;
            }

            @Override
            public Book getBookById(int bookId) {
                return null;
            }

            @Override
            public List<Book> getAllBooks() {
                return List.of();
            }

            @Override
            public List<Book> searchBooks(String query) {
                return List.of();
            }
        };
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
