package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String username = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Encrypt the password before storing
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String result = userDataHandler.registerUser(username, email, hashedPassword);
        showAlert(result.equals("User registered successfully") ? AlertType.INFORMATION : AlertType.ERROR, result, result);
        if (result.equals("User registered successfully")) {
            clearFields();
        }
    }

    @FXML
    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/abudu/lms/library/login.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root, 320, 240));
        } catch (IOException e) {
            Logger.getLogger(UserRegistrationController.class.getName()).log(Level.SEVERE, "An error occurred while trying to load the login screen", e);
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