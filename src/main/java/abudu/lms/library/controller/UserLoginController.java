package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.User;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                showAlert(AlertType.ERROR, "Error", "Invalid credentials.");
            }
        } catch (SQLException e) {
            Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, "An error occurred while trying to log in", e);
            showAlert(AlertType.ERROR, "Error", "An error occurred while trying to log in.");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @FXML
    public void showRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/abudu/lms/library/register.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root, 320, 240));
        } catch (IOException e) {
            Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, "An error occurred while trying to load the registration screen", e);
        }
    }


}