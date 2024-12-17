package abudu.lms.library.database;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookDataHandlerTest {
    private BookDataHandler bookDataHandler;

    @Mock
    private BookRepository mockBookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookDataHandler = new BookDataHandler(mockBookRepository) {
            @Override
            public BookRepository getBookRepository() {
                return null;
            }
        };
    }

    @Test
    void testAddBook() {
        Book book = new Book(1, "Java Programming 3", "John Doe 3", "John Doe 3", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 3", 1);
        bookDataHandler.addBook(book);
        verify(mockBookRepository).addBook(book);
    }

    @Test
    void testUpdateBook() {
        Book book = new Book(1, "Java Programming 3", "John Doe 3", "John Doe 3", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 3", 1);
        bookDataHandler.updateBook(book);
        verify(mockBookRepository).updateBook(book);
    }

    @Test
    void testDeleteBook() {
        int bookId = 1;
        bookDataHandler.deleteBook(bookId);
        verify(mockBookRepository).deleteBook(bookId);
    }

    @Test
    void testGetBookById() {
        int bookId = 1;
        Book expectedBook = new Book(bookId, "Java Programming 3", "John Doe 3", "John Doe 3", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 3", 1);
        when(mockBookRepository.getBookById(bookId)).thenReturn(expectedBook);

        Book actualBook = bookDataHandler.getBookById(bookId);

        assertNotNull(actualBook);
        assertEquals(expectedBook, actualBook);
        verify(mockBookRepository, times(1)).getBookById(bookId);
    }

    @Test
    void testGetAllBooks() {
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book(1, "Java Programming 3", "John Doe 3", "John Doe 3", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 3", 1));
        expectedBooks.add(new Book(2, "Java Programming 4", "John Doe 4", "John Doe 4", 2021, 1234567890, true, "Programming", 10, "Java Programming Book 4", 1));

        when(mockBookRepository.getAllBooks()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookDataHandler.getAllBooks();

        assertNotNull(actualBooks);
        assertEquals(expectedBooks, actualBooks);
        verify(mockBookRepository, times(1)).getAllBooks();
    }

    @Test
    void testReturnBook() {
        int id = 1;
        when(mockBookRepository.returnBook(id)).thenReturn(true);

        boolean returned = bookDataHandler.returnBook(id);

        assertTrue(returned);
        verify(mockBookRepository, times(1)).returnBook(id);
    }

    @Test
    void testBorrowBook() {
        int id = 1;
        when(mockBookRepository.borrowBook(id)).thenReturn(true);

        boolean borrowed = bookDataHandler.borrowBook(id);

        assertTrue(borrowed);
        verify(mockBookRepository, times(1)).borrowBook(id);
    }



}