package abudu.lms.library.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.ERole;
import abudu.lms.library.models.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserRegistrationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private ComboBox<Role> roleComboBox;

    private final UserDataHandler userDataHandler;

    public UserRegistrationController() {
        this.userDataHandler = new UserDataHandler();
    }

    @FXML
    public void initialize() {
        // Populate roleComboBox with available roles
        for (ERole eRole : ERole.values()) {
            roleComboBox.getItems().add(new Role(0, eRole));
        }
    }

    @FXML
    public void registerUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        Role selectedRole = roleComboBox.getValue();

        if (selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a role.");
            return;
        }

        Set<Role> roles = new HashSet<>();
        roles.add(selectedRole);

        // Register the user and handle feedback
        String registrationResult = registerUser(firstName, lastName, username, email, password, roles);
        if (registrationResult.equals("User registered successfully.")) {
            showAlert(Alert.AlertType.INFORMATION, "Success", registrationResult);
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", registrationResult);
        }
    }

    private String registerUser(String firstName, String lastName, String username, String email, String password, Set<Role> roles) {
        // Validate input
        if (!validateInput(firstName, lastName, username, email, password)) {
            return "Invalid input: Ensure all fields are filled correctly.";
        }

        // Check email existence
        if (userDataHandler.getUserByEmail(email) != null) {
            return "Email already exists.";
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Add user
        boolean isSuccess = userDataHandler.addUser(firstName, lastName, username, email, hashedPassword, LocalDateTime.now(), roles);
        return isSuccess ? "User registered successfully." : "Registration failed.";
    }

    private boolean validateInput(String firstName, String lastName, String username, String email, String password) {
        if (firstName == null || firstName.trim().isEmpty()) return false;
        if (lastName == null || lastName.trim().isEmpty()) return false;
        if (username == null || username.trim().isEmpty()) return false;
        if (email == null || !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) return false;
        return password != null && !password.trim().isEmpty();
    }

    @FXML
    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/abudu/lms/library/login.fxml"));
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 600));
        } catch (IOException e) {
            Logger.getLogger(UserRegistrationController.class.getName()).log(Level.SEVERE, "An error occurred while trying to load the login screen", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load login screen.");
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
