package abudu.lms.library.controller;

import java.io.IOException;
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

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (password.length() < 8) {
            showAlert(AlertType.ERROR, "Weak Password", "Password must be at least 8 characters long.");
            return;
        }

        if (selectedRole == null) {
            showAlert(AlertType.ERROR, "Role Missing", "Please select a role.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Set<Role> roles = new HashSet<>();
        roles.add(selectedRole);

        try {
            String result = userDataHandler.registerUser(firstName, lastName, username, email, hashedPassword, roles);
            if (result.equals("User registered successfully")) {
                showAlert(AlertType.INFORMATION, "Success", result);
                clearFields();
            } else {
                showAlert(AlertType.ERROR, "Registration Failed", result);
            }
        } catch (Exception e) {
            Logger.getLogger(UserRegistrationController.class.getName())
                    .log(Level.SEVERE, "An error occurred during user registration", e);
            showAlert(AlertType.ERROR, "Error", "An unexpected error occurred. Please try again.");
        }
    }


    @FXML
    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/abudu/lms/library/login.fxml"));
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.setScene(new Scene(root, 320, 240));
        } catch (IOException e) {
            Logger.getLogger(UserRegistrationController.class.getName()).log(Level.SEVERE, "An error occurred while trying to load the login screen", e);
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

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}