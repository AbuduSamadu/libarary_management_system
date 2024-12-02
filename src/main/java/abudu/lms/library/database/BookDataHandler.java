package abudu.lms.library.database;

import java.util.List;

import abudu.lms.library.models.Book;

import abudu.lms.library.repository.BookRepository;

public class BookDataHandler {

    private final BookRepository bookRepository;

    public BookDataHandler() {
        this.bookRepository = new BookRepository() {
            @Override
            public void addBook(Book book) {
            }

            @Override
            public void updateBook(Book book) {
            }

            @Override
            public void deleteBook(int bookId) {
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
            public boolean returnBook(int id) {
                return false;
            }

            @Override
            public boolean borrowBook(int bookId) {
                return false;
            }

            @Override
            public int countBooks() {
                return 0;
            }

            @Override
            public void reserveBook(int id) {

            }


        };
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
        return bookRepository.borrowBook(id );
    }

    public int generateNewId() {
        List<Book> books = bookRepository.getAllBooks();
        return books.size() + 1;
    }
}
