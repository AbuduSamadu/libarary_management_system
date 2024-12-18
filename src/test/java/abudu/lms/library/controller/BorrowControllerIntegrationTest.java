package abudu.lms.library.controller;

import abudu.lms.library.models.Borrowing;
import abudu.lms.library.models.User;
import abudu.lms.library.repository.BorrowingRepositoryImpl;
import abudu.lms.library.utils.UserSession;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BorrowControllerIntegrationTest {

    @Mock
    private BorrowingRepositoryImpl mockBorrowingRepository;
    @Mock
    private UserSession mockSession;
    private BorrowController controller;

    @BeforeAll
    void initJFX() {
        new JFXPanel(); // Initializes JavaFX environment
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        controller = new BorrowController();
        injectMockDependencies();
        initializeFXMLFields();
    }

    private void injectMockDependencies() throws Exception {
        Field borrowingRepositoryField = BorrowController.class.getDeclaredField("borrowingRepository");
        borrowingRepositoryField.setAccessible(true);
        borrowingRepositoryField.set(controller, mockBorrowingRepository);

        Field userSessionField = UserSession.class.getDeclaredField("instance");
        userSessionField.setAccessible(true);
        userSessionField.set(null, mockSession);
    }

    private void initializeFXMLFields() throws Exception {
        Field borrowingsTableField = BorrowController.class.getDeclaredField("borrowingsTable");
        borrowingsTableField.setAccessible(true);
        borrowingsTableField.set(controller, new TableView<>());

        Field titleFieldField = BorrowController.class.getDeclaredField("titleField");
        titleFieldField.setAccessible(true);
        titleFieldField.set(controller, new TextField());

        Field authorFieldField = BorrowController.class.getDeclaredField("authorField");
        authorFieldField.setAccessible(true);
        authorFieldField.set(controller, new TextField());

        Field isbnFieldField = BorrowController.class.getDeclaredField("isbnField");
        isbnFieldField.setAccessible(true);
        isbnFieldField.set(controller, new TextField());

        Field userIdField = BorrowController.class.getDeclaredField("userIdField");
        userIdField.setAccessible(true);
        userIdField.set(controller, new TextField());

        Field searchField = BorrowController.class.getDeclaredField("searchField");
        searchField.setAccessible(true);
        searchField.set(controller, new TextField());

        Field borrowDatePicker = BorrowController.class.getDeclaredField("borrowDatePicker");
        borrowDatePicker.setAccessible(true);
        borrowDatePicker.set(controller, new DatePicker());

        Field notesAreaField = BorrowController.class.getDeclaredField("notesArea");
        notesAreaField.setAccessible(true);
        notesAreaField.set(controller, new TextArea());
    }

    @Test
    void testHandleBorrowBookSuccess() throws InterruptedException {
        when(mockSession.getCurrentUser()).thenReturn(new User(1, "John", "Doe", "johndoe", "johndoe@example.com", "password", null, null));



        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                controller.handleBorrowBook();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(10, TimeUnit.SECONDS), "Borrow book process should complete");

        verify(mockBorrowingRepository).addBorrowing(any(Borrowing.class));
    }

    @Test
    void testHandleBorrowBookInvalidISBN() throws InterruptedException {
        when(mockSession.getCurrentUser()).thenReturn(new User(1, "John", "Doe", "johndoe", "johndoe@example.com", "password", null, null));



        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                controller.handleBorrowBook();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(10, TimeUnit.SECONDS), "Borrow book process should complete");

        verify(mockBorrowingRepository, never()).addBorrowing(any(Borrowing.class));
    }

    @Test
    void testHandleBorrowBookInvalidUserId() throws InterruptedException {
        when(mockSession.getCurrentUser()).thenReturn(new User(1, "John", "Doe", "johndoe", "johndoe@example.com", "password", null, null));



        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                controller.handleBorrowBook();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(10, TimeUnit.SECONDS), "Borrow book process should complete");

        verify(mockBorrowingRepository, never()).addBorrowing(any(Borrowing.class));
    }

    @Test
    void testHandleBorrowBookNoCurrentUser() throws InterruptedException {
        when(mockSession.getCurrentUser()).thenReturn(null);



        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                controller.handleBorrowBook();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(10, TimeUnit.SECONDS), "Borrow book process should complete");

        verify(mockBorrowingRepository, never()).addBorrowing(any(Borrowing.class));
    }
}