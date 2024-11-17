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

        if (!isValidEmail(email)) {
            showAlert(AlertType.WARNING, "Warning", "Please enter a valid email address.");
            return;
        }

        if (password.isEmpty()) {
            showAlert(AlertType.WARNING, "Warning", "Password cannot be empty.");
            return;
        }

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() {
                User user = userDataHandler.getUserByEmail(email);
                if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                    updateMessage("Login successful!");
                    loadDashboard();
                } else {
                    updateMessage("Invalid email or password.");
                }
                return null;
            }
        };

        loginTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                showAlert(newValue.equals("Login successful!") ? AlertType.INFORMATION : AlertType.ERROR,
                        "Login Status", newValue);
            }
        });

        new Thread(loginTask).start();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private void loadDashboard() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/abudu/lms/library/dashboard.fxml")));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, "Error loading dashboard", e);
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/abudu/lms/library/register.fxml")));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            Logger.getLogger(UserLoginController.class.getName()).log(Level.SEVERE, "An error occurred while trying to load the registration screen", e);
        }
    }
}
