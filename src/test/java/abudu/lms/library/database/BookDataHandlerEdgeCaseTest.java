package abudu.lms.library.database;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookDataHandlerEdgeCaseTest {

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

    // Null Book Handling
//    @Test
//    void testAddNullBook() {
//        // Arrange
//        Book nullBook = null;
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> {
//            bookDataHandler.addBook(nullBook);
//        });
//    }


    // Book Deletion Edge Cases
    @Test
    void testDeleteNonExistentBook() {
        // Arrange
        int nonExistentBookId = 9999;
        doNothing().when(mockBookRepository).deleteBook(nonExistentBookId);

        // Act & Assert
        assertDoesNotThrow(() -> {
            bookDataHandler.deleteBook(nonExistentBookId);
        });
        verify(mockBookRepository).deleteBook(nonExistentBookId);
    }

    // Book Return and Borrow Edge Cases
    @Test
    void testReturnAlreadyReturnedBook() {
        // Arrange
        int bookId = 1;
        when(mockBookRepository.returnBook(bookId)).thenReturn(false);

        // Act
        boolean returned = bookDataHandler.returnBook(bookId);

        // Assert
        assertFalse(returned);
        verify(mockBookRepository).returnBook(bookId);
    }

    @Test
    void testBorrowAlreadyBorrowedBook() {
        // Arrange
        int bookId = 1;
        when(mockBookRepository.borrowBook(bookId)).thenReturn(true);

        // Act
        boolean borrowed = bookDataHandler.borrowBook(bookId);

        // Assert
        assertTrue(borrowed);
        verify(mockBookRepository).borrowBook(bookId);
    }

//    // Generate ID Edge Cases
//    @Test
//    void testGenerateIdWithNullBookList() {
//        // Arrange
//        when(mockBookRepository.getAllBooks()).thenReturn(null);
//
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> {
//            bookDataHandler.generateNewId();
//        });
//    }


    // Update Book Edge Cases
    @Test
    void testUpdateNonExistentBook() {
        // Arrange
        Book nonExistentBook = new Book(9999, "Non-Existent", "Unknown", "Unknown", 2024, 1234567890, true, "Programming", 10, "Java Programming Book", 1);
        doNothing().when(mockBookRepository).updateBook(nonExistentBook);

        // Act & Assert
        assertDoesNotThrow(() -> {
            bookDataHandler.updateBook(nonExistentBook);
        });
        verify(mockBookRepository).updateBook(nonExistentBook);
    }

    // Boundary Condition Tests
    @Test
    void testGetAllBooksEmptyList() {
        // Arrange
        when(mockBookRepository.getAllBooks()).thenReturn(Collections.emptyList());

        // Act
        List<Book> books = bookDataHandler.getAllBooks();

        // Assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
        verify(mockBookRepository).getAllBooks();
    }

    // Repository Failure Scenarios
    @Test
    void testRepositoryFailureDuringBookAddition() {
        // Arrange
        Book book = new Book(1, "Test Book", "Test Author", "Test Genre", 2024, 1234567890, true, "Programming", 10, "Java Programming Book", 1);
        doThrow(new RuntimeException("Repository error")).when(mockBookRepository).addBook(book);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            bookDataHandler.addBook(book);
        });
    }
}