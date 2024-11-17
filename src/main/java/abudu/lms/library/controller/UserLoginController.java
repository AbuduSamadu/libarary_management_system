package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Email and password cannot be empty.");
            return;
        }

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    User user = userDataHandler.getUserByEmail(email);
                    if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                        updateMessage("Login successful!");
                        // Proceed to next functionality (e.g., load dashboard scene)
                    } else {
                        updateMessage("Invalid credentials.");
                    }
                } catch (SQLException e) {
                    Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, "Database error during login", e);
                    updateMessage("An error occurred while trying to log in.");
                }
                return null;
            }
        };

        loginTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Login successful!")) {
                showAlert(AlertType.INFORMATION, "Success", newValue);
            } else {
                showAlert(AlertType.ERROR, "Error", newValue);
            }
        });

        new Thread(loginTask).start();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @FXML
    public void showRegister() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/abudu/lms/library/register.fxml")));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, "An error occurred while trying to load the registration screen", e);
        }
    }
}
