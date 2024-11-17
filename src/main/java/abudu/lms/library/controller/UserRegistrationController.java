package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        roleComboBox.getItems().addAll(Role.values());
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
            showAlert(AlertType.ERROR, "Error", "Please select a role.");
            return;
        }

        // Encrypt the password before storing
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a set of roles and add the selected role
        Set<Role> roles = new HashSet<>();
        roles.add(selectedRole);

        String result = userDataHandler.registerUser(firstName, lastName, username, email, hashedPassword, roles);
        showAlert(result.equals("User registered successfully") ? AlertType.INFORMATION : AlertType.ERROR, result, result);
        if (result.equals("User registered successfully")) {
            clearFields();
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