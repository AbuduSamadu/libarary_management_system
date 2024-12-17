package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import abudu.lms.library.models.User;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRegistrationControllerIntegrationTest {

    private UserRegistrationController controller;

    @Mock
    private UserDataHandler mockUserDataHandler;

    @BeforeAll
    public void initJFX() {
        // Initialize JavaFX environment for testing
        new JFXPanel();
    }

    @BeforeEach
    public void setUp() {
        // Initialize Mockito mocks
        MockitoAnnotations.openMocks(this);

        // Create controller and inject mock UserDataHandler
        controller = new UserRegistrationController();
        try {
            // Use reflection to set the private userDataHandler field
            Field userDataHandlerField = UserRegistrationController.class.getDeclaredField("userDataHandler");
            userDataHandlerField.setAccessible(true);
            userDataHandlerField.set(controller, mockUserDataHandler);

            // Initialize FXML fields with reflection
            initializeFXMLFields();
        } catch (Exception e) {
            fail("Could not set up test environment: " + e.getMessage());
        }
    }

    private void initializeFXMLFields() throws Exception {
        // Use reflection to create and set mock FXML fields
        Field[] fields = UserRegistrationController.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == TextField.class) {
                field.set(controller, new TextField());
            } else if (field.getType() == ComboBox.class) {
                ComboBox<Role> comboBox = new ComboBox<>();
                // Populate combo box with roles
                for (ERole eRole : ERole.values()) {
                    comboBox.getItems().add(new Role(0, eRole));
                }
                field.set(controller, comboBox);
            }
        }
    }

    @Test
    public void testSuccessfulUserRegistration() {
        // Arrange
        Platform.runLater(() -> {
            // Setup input fields
            Objects.requireNonNull(findTextField("firstNameField")).setText("John");
            Objects.requireNonNull(findTextField("lastNameField")).setText("Doe");
            Objects.requireNonNull(findTextField("usernameField")).setText("johndoe");
            Objects.requireNonNull(findTextField("emailField")).setText("john.doe@example.com");
            Objects.requireNonNull(findTextField("passwordField")).setText("StrongPassword123!");

            // Select a role
            ComboBox<Role> roleComboBox = findComboBox();
            assert roleComboBox != null;
            roleComboBox.setValue(new Role(0, ERole.ROLE_USER));

            // Mock UserDataHandler methods
            when(mockUserDataHandler.isValidInput(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(true);
            when(mockUserDataHandler.getUserByEmail(anyString()))
                    .thenReturn(null);
            when(mockUserDataHandler.addUser(
                    anyString(), anyString(), anyString(), anyString(),
                    anyString(), any(LocalDateTime.class), any(Set.class))
            ).thenReturn(true);

            // Act
            controller.registerUser();

            // Assert
            verify(mockUserDataHandler).addUser(
                    eq("John"), eq("Doe"), eq("johndoe"),
                    eq("john.doe@example.com"),
                    argThat(password -> BCrypt.checkpw("StrongPassword123!", password)),
                    any(LocalDateTime.class),
                    argThat(roles -> roles.iterator().next().getName() == ERole.ROLE_USER)
            );
        });
    }


    public void testInvalidInputValidation(
            String firstName, String lastName, String username,
            String email, String password, boolean expectedResult
    ) {
        Platform.runLater(() -> {
            // Mock input validation
            when(mockUserDataHandler.isValidInput(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(false);

            // Prepare input
            Objects.requireNonNull(findTextField("firstNameField")).setText(firstName);
            Objects.requireNonNull(findTextField("lastNameField")).setText(lastName);
            findTextField("usernameField").setText(username);
            findTextField("emailField").setText(email);
            findTextField("passwordField").setText(password);

            // Select a role
            ComboBox<Role> roleComboBox = findComboBox();
            roleComboBox.setValue(new Role(0, ERole.ROLE_USER));

            // Capture method invocation
            controller.registerUser();

            // Verify validation method was called
            verify(mockUserDataHandler).isValidInput(
                    eq(firstName), eq(lastName), eq(username), eq(email), eq(password)
            );
        });
    }

    @Test
    public void testDuplicateEmailRegistration() {
        Platform.runLater(() -> {
            // Prepare input
            Objects.requireNonNull(findTextField("firstNameField")).setText("John");
            Objects.requireNonNull(findTextField("lastNameField")).setText("Doe");
            Objects.requireNonNull(findTextField("usernameField")).setText("johndoe");
            Objects.requireNonNull(findTextField("emailField")).setText("john.doe@example.com");
            Objects.requireNonNull(findTextField("passwordField")).setText("StrongPassword123!");

            // Select a role
            ComboBox<Role> roleComboBox = findComboBox();
            assert roleComboBox != null;
            roleComboBox.setValue(new Role(0, ERole.ROLE_USER));

            // Mock UserDataHandler methods
            when(mockUserDataHandler.isValidInput(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(true);
            // Simulate existing user
            when(mockUserDataHandler.getUserByEmail("john.doe@example.com"))
                    .thenReturn(mock(User.class));

            // Act
            controller.registerUser();

            // Verify no user addition occurred
            verify(mockUserDataHandler, never()).addUser(
                    anyString(), anyString(), anyString(),
                    anyString(), anyString(), any(LocalDateTime.class), any(Set.class)
            );
        });
    }

    @Test
    public void testNoRoleSelected() {
        Platform.runLater(() -> {
            // Prepare input without selecting a role
            Objects.requireNonNull(findTextField("firstNameField")).setText("John");
            Objects.requireNonNull(findTextField("lastNameField")).setText("Doe");
            Objects.requireNonNull(findTextField("usernameField")).setText("johndoe");
            Objects.requireNonNull(findTextField("emailField")).setText("john.doe@example.com");
            Objects.requireNonNull(findTextField("passwordField")).setText("StrongPassword123!");

            // Intentionally do not set a role
            ComboBox<Role> roleComboBox = findComboBox();
            assert roleComboBox != null;
            roleComboBox.setValue(null);

            // Act
            controller.registerUser();

            // Verify no user addition occurred
            verify(mockUserDataHandler, never()).addUser(
                    anyString(), anyString(), anyString(),
                    anyString(), anyString(), any(LocalDateTime.class), any(Set.class)
            );
        });
    }

    @Test
    public void testPasswordHashing() {
        Platform.runLater(() -> {
            String rawPassword = "TestPassword123!";

            // Prepare input
            Objects.requireNonNull(findTextField("firstNameField")).setText("John");
            Objects.requireNonNull(findTextField("lastNameField")).setText("Doe");
            Objects.requireNonNull(findTextField("usernameField")).setText("johndoe");
            Objects.requireNonNull(findTextField("emailField")).setText("john.doe@example.com");
            Objects.requireNonNull(findTextField("passwordField")).setText(rawPassword);

            // Select a role
            ComboBox<Role> roleComboBox = findComboBox();
            assert roleComboBox != null;
            roleComboBox.setValue(new Role(0, ERole.ROLE_USER));

            // Mock UserDataHandler methods
            when(mockUserDataHandler.isValidInput(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(true);
            when(mockUserDataHandler.getUserByEmail(anyString()))
                    .thenReturn(null);
            when(mockUserDataHandler.addUser(
                    anyString(), anyString(), anyString(), anyString(),
                    anyString(), any(LocalDateTime.class), any(Set.class))
            ).thenReturn(true);

            // Act
            controller.registerUser();

            // Verify password is hashed and different from original
            verify(mockUserDataHandler).addUser(
                    anyString(), anyString(), anyString(),
                    anyString(),
                    argThat(hashedPassword -> {
                        // Verify the hashed password is different from the original
                        assertNotEquals(rawPassword, hashedPassword);
                        // Verify BCrypt can validate the original password against the hash
                        return BCrypt.checkpw(rawPassword, hashedPassword);
                    }),
                    any(LocalDateTime.class),
                    any(Set.class)
            );
        });
    }

    // Helper methods to find FXML components using reflection
    private TextField findTextField(String fieldName) {
        try {
            Field field = UserRegistrationController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (TextField) field.get(controller);
        } catch (Exception e) {
            fail("Could not find TextField: " + fieldName);
            return null;
        }
    }

    private ComboBox<Role> findComboBox() {
        try {
            Field field = UserRegistrationController.class.getDeclaredField("roleComboBox");
            field.setAccessible(true);
            return (ComboBox<Role>) field.get(controller);
        } catch (Exception e) {
            fail("Could not find ComboBox: " + "roleComboBox");
            return null;
        }
    }
}