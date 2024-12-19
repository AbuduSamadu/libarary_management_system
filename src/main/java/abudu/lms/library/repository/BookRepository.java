package abudu.lms.library.repository;

import abudu.lms.library.database.DatabaseHandler;
import abudu.lms.library.models.Book;
import abudu.lms.library.models.BookLinkedList;

import java.util.List;

/**
 * This interface defines the methods that must be implemented by a class that will interact with the book repository.
 */


public interface BookRepository {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);

    Book getBookById(int bookId);

    List<Book> getAllBooks();

    boolean returnBook(int id);
;

    boolean borrowBook(int bookId);



    int countBooks();

    void reserveBook(int id);

    void setDatabaseHandler(DatabaseHandler dbHandler);

    void setBookList(BookLinkedList bookList);
}