package abudu.lms.library.database;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepository;

import java.util.List;

public abstract class BookDataHandler {

    private final BookRepository bookRepository;

    public BookDataHandler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public void addBook(Book book) {
        bookRepository.addBook(book);
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }

    public void deleteBook(int bookId) {
        bookRepository.deleteBook(bookId);
    }

    public Book getBookById(int bookId) {
        return bookRepository.getBookById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }


    public boolean returnBook(int id) {
        return bookRepository.returnBook(id);
    }

    public boolean borrowBook(int id) {
        return bookRepository.borrowBook(id);
    }

    public void generateNewId() {
        List<Book> books = bookRepository.getAllBooks();
        if (books == null) {
            throw new NullPointerException("Book list is null");
        }

    }

    public abstract BookRepository getBookRepository();
}
