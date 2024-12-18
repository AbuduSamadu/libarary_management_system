package abudu.lms.library.controller;

import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Reservation;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;
import abudu.lms.library.repository.ReservationRepositoryImpl;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReserveControllerIntegrationTest {

    private ReserveController controller;

    @Mock
    private ReservationRepositoryImpl mockReservationRepository;

    @Mock
    private UserSession mockUserSession;

    @BeforeAll
    public void initJFX() {
        // Initialize JavaFX environment for testing
        new JFXPanel();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize Mockito mocks
        MockitoAnnotations.openMocks(this);

        // Create controller and inject mock dependencies
        controller = new ReserveController();

        // Use reflection to inject mocked dependencies
        injectMockDependencies();

        // Initialize FXML fields
        initializeFXMLFields();
    }

    private void injectMockDependencies() throws Exception {
        // Inject mock ReservationRepository
        Field reservationRepositoryField = ReserveController.class.getDeclaredField("reservationRepository");
        reservationRepositoryField.setAccessible(true);
        reservationRepositoryField.set(controller, mockReservationRepository);
    }

    private void initializeFXMLFields() throws Exception {
        // Use reflection to create and set mock FXML fields
        Field reservationsTableField = ReserveController.class.getDeclaredField("reservationsTable");
        reservationsTableField.setAccessible(true);
        reservationsTableField.set(controller, new TableView<>());

        Field titleField = ReserveController.class.getDeclaredField("titleField");
        titleField.setAccessible(true);
        titleField.set(controller, new TextField());

        Field authorField = ReserveController.class.getDeclaredField("authorField");
        authorField.setAccessible(true);
        authorField.set(controller, new TextField());

        Field isbnField = ReserveController.class.getDeclaredField("isbnField");
        isbnField.setAccessible(true);
        isbnField.set(controller, new TextField());

        Field userIdField = ReserveController.class.getDeclaredField("userIdField");
        userIdField.setAccessible(true);
        userIdField.set(controller, new TextField());

        Field searchField = ReserveController.class.getDeclaredField("searchField");
        searchField.setAccessible(true);
        searchField.set(controller, new TextField());

        Field reservationDatePicker = ReserveController.class.getDeclaredField("reservationDatePicker");
        reservationDatePicker.setAccessible(true);
        reservationDatePicker.set(controller, new DatePicker());

        Field notesArea = ReserveController.class.getDeclaredField("notesArea");
        notesArea.setAccessible(true);
        notesArea.set(controller, new TextArea());
    }

    @Test
    public void testReserveBookSuccess() throws InterruptedException {
        // Prepare test data
        String title = "Test Book";
        String author = "Test Author";
        String isbn = "1234567890123";
        String userId = "1";
        String reservationDate = "2024-12-18";
        String notes = "Test Notes";

        // Mock user session
        UserSession mockSession = mock(UserSession.class);
        when(mockSession.getCurrentUser()).thenReturn(new User(1, "joe", "Doe", "JoeD", "johndoe@example.com", "Kwm@123", LocalDateTime.now(), new HashSet<Role>() {{
            add(new Role(1, ERole.Librarian));
        }}));
        UserSession.setInstance(mockSession);

        // Prepare synchronization
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // Set input fields
            ((TextField) Objects.requireNonNull(getPrivateField("titleField"))).setText(title);
            ((TextField) Objects.requireNonNull(getPrivateField("authorField"))).setText(author);
            ((TextField) Objects.requireNonNull(getPrivateField("isbnField"))).setText(isbn);
            ((TextField) Objects.requireNonNull(getPrivateField("userIdField"))).setText(userId);
            ((DatePicker) Objects.requireNonNull(getPrivateField("reservationDatePicker"))).setValue(LocalDate.parse(reservationDate));
            ((TextArea) Objects.requireNonNull(getPrivateField("notesArea"))).setText(notes);

            // Perform reserve book action
            controller.handleReserveBook();

            latch.countDown();
        });

        // Wait for JavaFX thread to complete
        assertTrue(latch.await(100, TimeUnit.SECONDS), "Reserve book process should complete");

        // Verify interactions
        verify(mockReservationRepository).addReservation(any(Reservation.class));
    }

    @Test
    public void testReserveBookInvalidISBN() throws InterruptedException {
        // Prepare test data
        String invalidISBN = "123";

        // Mock user session
        UserSession mockSession = mock(UserSession.class);
        when(mockSession.getCurrentUser()).thenReturn(new User(1, "joe", "Doe", "JoeD", "johndoe@example.com", "Kwm@123", LocalDateTime.now(), new HashSet<Role>() {{
            add(new Role(1, ERole.Librarian));
        }}));
        UserSession.setInstance(mockSession);

        // Prepare synchronization
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // Set input fields
            ((TextField) Objects.requireNonNull(getPrivateField("isbnField"))).setText(invalidISBN);

            // Perform reserve book action
            controller.handleReserveBook();

            latch.countDown();
        });

        // Wait for JavaFX thread to complete
        assertTrue(latch.await(100, TimeUnit.SECONDS), "Reserve book process should complete");

        // Verify no interaction with repository
        verify(mockReservationRepository, never()).addReservation(any(Reservation.class));
    }

    // Helper method to get private fields using reflection
    private Object getPrivateField(String fieldName) {
        try {
            Field field = ReserveController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (Exception e) {
            fail("Could not access field: " + fieldName);
            return null;
        }
    }
}