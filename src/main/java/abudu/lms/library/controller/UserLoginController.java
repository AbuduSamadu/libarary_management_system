package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class UserLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserDataHandler userDataHandler;

    public UserLoginController() {
        this.userDataHandler = new UserDataHandler();
    }

    @FXML
    public void loginUser() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            User user = userDataHandler.getUserByEmail(email);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                showAlert(AlertType.INFORMATION, "Success", "Login successful!");
                // Proceed to the next scene or functionality
            } else {
                showAlert(AlertType.ERROR, "Error", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An error occurred while trying to log in.");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}