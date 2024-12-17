package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.User;
import abudu.lms.library.utils.UserSession;
import abudu.lms.library.views.landingPage.Dashboard;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserLoginControllerIntegrationTest {

    private UserLoginController controller;

    @Mock
    private UserDataHandler mockUserDataHandler;

    @Mock
    private UserSession mockUserSession;

    @Mock
    private Dashboard mockDashboard;

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
        controller = new UserLoginController();

        // Use reflection to inject mocked dependencies
        injectMockDependencies();

        // Initialize FXML fields
        initializeFXMLFields();
    }

    private void injectMockDependencies() throws Exception {
        // Inject mock UserDataHandler
        Field userDataHandlerField = UserLoginController.class.getDeclaredField("userDataHandler");
        userDataHandlerField.setAccessible(true);
        userDataHandlerField.set(controller, mockUserDataHandler);
    }

    private void initializeFXMLFields() throws Exception {
        // Use reflection to create and set mock FXML fields
        Field emailField = UserLoginController.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        emailField.set(controller, new TextField());

        Field passwordField = UserLoginController.class.getDeclaredField("passwordField");
        passwordField.setAccessible(true);
        passwordField.set(controller, new PasswordField());
    }

    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        // Prepare test data
        String email = "valid@example.com";
        String rawPassword = "correctPassword";
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        // Create a mock User
        User mockUser = mock(User.class);
        when(mockUser.getPassword()).thenReturn(hashedPassword);

        // Setup mocks
        when(mockUserDataHandler.isValidEmail(email)).thenReturn(true);
        when(mockUserDataHandler.getUserByEmail(email)).thenReturn(mockUser);

        // Prepare synchronization
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // Set input fields
            ((TextField) getPrivateField("emailField")).setText(email);
            ((PasswordField) getPrivateField("passwordField")).setText(rawPassword);

            // Add listener to verify login success
            controller.loginUser();

            latch.countDown();
        });

        // Wait for JavaFX thread to complete
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Login process should complete");

        // Verify interactions
        verify(mockUserDataHandler).getUserByEmail(email);
        verify(mockUser).getPassword();

    }

    public void testInvalidLoginInputs(String email, String password, boolean expectedValidation) {
        Platform.runLater(() -> {
            // Set input fields
            ((TextField) Objects.requireNonNull(getPrivateField("emailField"))).setText(email);
            ((PasswordField) Objects.requireNonNull(getPrivateField("passwordField"))).setText(password);

            // Mock email validation
            when(mockUserDataHandler.isValidEmail(anyString()))
                    .thenReturn(email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$"));

            // Perform login
            controller.loginUser();

            // Verify email validation
            verify(mockUserDataHandler).isValidEmail(email);
        });
    }

    @Test
    public void testLoginWithNonExistentUser() {
        // Prepare test data
        String email = "nonexistent@example.com";
        String password = "somePassword";

        Platform.runLater(() -> {
            // Set input fields
            ((TextField) Objects.requireNonNull(getPrivateField("emailField"))).setText(email);
            ((PasswordField) Objects.requireNonNull(getPrivateField("passwordField"))).setText(password);

            // Mock dependencies
            when(mockUserDataHandler.isValidEmail(email)).thenReturn(true);
            when(mockUserDataHandler.getUserByEmail(email)).thenReturn(null);

            // Perform login
            controller.loginUser();

            // Verify user lookup
            verify(mockUserDataHandler).getUserByEmail(email);
        });
    }

    @Test
    public void testLoginWithIncorrectPassword() {
        // Prepare test data
        String email = "user@example.com";
        String correctPassword = "correctPassword";
        String incorrectPassword = "wrongPassword";
        String hashedPassword = BCrypt.hashpw(correctPassword, BCrypt.gensalt());

        // Create a mock User
        User mockUser = mock(User.class);
        when(mockUser.getPassword()).thenReturn(hashedPassword);

        Platform.runLater(() -> {
            // Set input fields with incorrect password
            ((TextField) Objects.requireNonNull(getPrivateField("emailField"))).setText(email);
            ((PasswordField) Objects.requireNonNull(getPrivateField("passwordField"))).setText(incorrectPassword);

            // Mock dependencies
            when(mockUserDataHandler.isValidEmail(email)).thenReturn(true);
            when(mockUserDataHandler.getUserByEmail(email)).thenReturn(mockUser);

            // Perform login
            controller.loginUser();

            // Verify user lookup and password check
            verify(mockUserDataHandler).getUserByEmail(email);
            verify(mockUser).getPassword();
        });
    }

    @Test
    public void testEmailValidation() {
        String[][] testCases = {
                {"valid@example.com", "true"},
                {"invalid-email", "false"},
                {"user@domain.co.uk", "true"},
                {"user+tag@example.org", "true"},
                {"", "false"}
        };

        for (String[] testCase : testCases) {
            boolean expectedResult = Boolean.parseBoolean(testCase[1]);

            assertEquals(isValidEmail(testCase[0]), expectedResult, "Email validation should match expected result");
        }
    }

    // Helper method to get private fields using reflection
    private Object getPrivateField(String fieldName) {
        try {
            Field field = UserLoginController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(controller);
        } catch (Exception e) {
            fail("Could not access field: " + fieldName);
            return null;
        }
    }

    // Utility method to check email validation (extracted from controller logic)
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}