package abudu.lms.library.services;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DashboardServiceIntegratingTest {
    private DashboardService dashboardService;
    private UserDataHandler mockUserDataHandler;
    private BookRepository mockBookRepository;

    @BeforeEach
    void setUp() {
        mockUserDataHandler = mock(UserDataHandler.class);
        mockBookRepository = mock(BookRepository.class);
        dashboardService = new DashboardService(mockUserDataHandler, mockBookRepository);
    }

    @Test
    void testGetTotalUsers() {
        when(mockUserDataHandler.countUsers()).thenReturn(10);

        int totalUsers = dashboardService.getTotalUsers();

        assertEquals(10, totalUsers);
        verify(mockUserDataHandler, times(1)).countUsers();
    }

    @Test
    void testGetTotalBooks() {
        when(mockBookRepository.countBooks()).thenReturn(20);

        int totalBooks = dashboardService.getTotalBooks();

        assertEquals(20, totalBooks);
        verify(mockBookRepository, times(1)).countBooks();
    }

    @Test
    void testGetTotalUsersWithZeroUsers() {
        when(mockUserDataHandler.countUsers()).thenReturn(0);

        int totalUsers = dashboardService.getTotalUsers();

        assertEquals(0, totalUsers);
        verify(mockUserDataHandler, times(1)).countUsers();
    }

    @Test
    void testGetTotalBooksWithZeroBooks() {
        when(mockBookRepository.countBooks()).thenReturn(0);

        int totalBooks = dashboardService.getTotalBooks();

        assertEquals(0, totalBooks);
        verify(mockBookRepository, times(1)).countBooks();
    }

    @Test
    void testGetTotalUsersWithException() {
        when(mockUserDataHandler.countUsers()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dashboardService.getTotalUsers();
        });

        assertEquals("Database error", exception.getMessage());
        verify(mockUserDataHandler, times(1)).countUsers();
    }

    @Test
    void testGetTotalBooksWithException() {
        when(mockBookRepository.countBooks()).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dashboardService.getTotalBooks();
        });

        assertEquals("Database error", exception.getMessage());
        verify(mockBookRepository, times(1)).countBooks();
    }
}