package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.mindrot.jbcrypt.BCrypt;

public class UserRegistrationController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    private final UserDataHandler userDataHandler;

    public UserRegistrationController() {
        this.userDataHandler = new UserDataHandler();
    }

    @FXML
    public void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Encrypt the password before storing
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (userDataHandler.registerUser(name, email, hashedPassword)) {
            showAlert(AlertType.INFORMATION, "Success", "User registered successfully!");
            clearFields();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to register the user. Please check the input fields.");
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}