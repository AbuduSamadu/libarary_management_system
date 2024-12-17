package abudu.lms.library.controller;

import abudu.lms.library.models.Book;
import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;
import abudu.lms.library.repository.BookRepositoryImpl;
import abudu.lms.library.utils.UserSession;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerIntegrationTest {

    private BookController controller;

    @Mock
    private BookRepositoryImpl mockBookRepository;

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
        controller = new BookController();

        // Use reflection to inject mocked dependencies
        injectMockDependencies();

        // Initialize FXML fields
        initializeFXMLFields();
    }

    private void injectMockDependencies() throws Exception {
        // Inject mock BookRepository
        Field bookRepositoryField = BookController.class.getDeclaredField("bookRepository");
        bookRepositoryField.setAccessible(true);
        bookRepositoryField.set(controller, mockBookRepository);
    }

    private void initializeFXMLFields() throws Exception {
        // Use reflection to create and set mock FXML fields
        Field resourcesTableField = BookController.class.getDeclaredField("resourcesTable");
        resourcesTableField.setAccessible(true);
        resourcesTableField.set(controller, new TableView<>());

        Field titleField = BookController.class.getDeclaredField("titleField");
        titleField.setAccessible(true);
        titleField.set(controller, new TextField());

        Field authorField = BookController.class.getDeclaredField("authorField");
        authorField.setAccessible(true);
        authorField.set(controller, new TextField());

        Field isbnField = BookController.class.getDeclaredField("isbnField");
        isbnField.setAccessible(true);
        isbnField.set(controller, new TextField());

        Field publisherField = BookController.class.getDeclaredField("publisherField");
        publisherField.setAccessible(true);
        publisherField.set(controller, new TextField());

        Field quantityField = BookController.class.getDeclaredField("quantityField");
        quantityField.setAccessible(true);
        quantityField.set(controller, new TextField());

        Field descriptionArea = BookController.class.getDeclaredField("descriptionArea");
        descriptionArea.setAccessible(true);
        descriptionArea.set(controller, new TextArea());

        Field categoryComboBox = BookController.class.getDeclaredField("categoryComboBox");
        categoryComboBox.setAccessible(true);
        categoryComboBox.set(controller, new ComboBox<>());
    }

    @Test
    public void testAddBookSuccess() throws InterruptedException {
        // Prepare test data
        String title = "Test Book";
        String author = "Test Author";
        String category = "Books";
        String publisher = "Test Publisher";
        int year = 2023;
        String isbn = "1234567890123";
        int quantity = 10;
        String description = "Test Description";

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
            ((ComboBox<String>) getPrivateField("categoryComboBox")).setValue(category);
            ((TextField) Objects.requireNonNull(getPrivateField("publisherField"))).setText(publisher);
            ((TextField) Objects.requireNonNull(getPrivateField("isbnField"))).setText(isbn);
            ((TextField) Objects.requireNonNull(getPrivateField("quantityField"))).setText(String.valueOf(quantity));
            ((TextArea) Objects.requireNonNull(getPrivateField("descriptionArea"))).setText(description);

            // Perform add book action
            controller.handleAddResource();

            latch.countDown();
        });

        // Wait for JavaFX thread to complete
        assertTrue(latch.await(100, TimeUnit.SECONDS), "Add book process should complete");

        // Verify interactions
        verify(mockBookRepository).addBook(any(Book.class));
    }

    @Test
    public void testAddBookInvalidISBN() throws InterruptedException {
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

            // Perform add book action
            controller.handleAddResource();

            latch.countDown();
        });

        // Wait for JavaFX thread to complete
        assertTrue(latch.await(100, TimeUnit.SECONDS), "Add book process should complete");

        // Verify no interaction with repository
        verify(mockBookRepository, never()).addBook(any(Book.class));
    }

    // Helper method to get private fields using reflection
    private Object getPrivateField(String fieldName) {
        try {
            Field field = BookController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (Exception e) {
            fail("Could not access field: " + fieldName);
            return null;
        }
    }
}