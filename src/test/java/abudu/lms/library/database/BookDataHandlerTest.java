package abudu.lms.library.database;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

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


}