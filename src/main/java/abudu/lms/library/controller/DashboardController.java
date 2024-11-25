package abudu.lms.library.controller;

import abudu.lms.library.models.User;
import abudu.lms.library.utils.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {

    @FXML
    private PieChart resourceChart;
    @FXML
    private BarChart<String, Number> transactionBarChart;
    @FXML
    private Button reportsButton;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private BorderPane dashboard;

    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label totalUsersLabel;
    @FXML
    private Label issuedBooksLabel;
    @FXML
    private Label overdueReturnsLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button homeButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button contactButton;
    @FXML
    private TextField globalSearchField;
    @FXML
    private ImageView logo;
    @FXML
    private Button sidebarHomeButton;
    @FXML
    private Button booksButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button settingsButton;
    @FXML
    private VBox mainBoard;
    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        UserSession userSession = UserSession.getInstance();
        User currentUser = userSession.getCurrentUser();
        if (currentUser != null) {
            setUsernameLabel(currentUser.getName());
            setLogoutButtonText("Logout");
        } else {
            setUsernameLabel("Guest");
            setLogoutButtonText("Login");
        }
    }

    public void setUsernameLabel(String username) {
        usernameLabel.setText("Welcome, " + username);
    }

    public void setLogoutButtonText(String text) {
        logoutButton.setText(text);
    }

    @FXML
    private void handleHomeButtonClick() {
        // Handle home button click
    }

    @FXML
    private void handleAboutButtonClick() {
        // Handle about button click
    }

    @FXML
    private void handleContactButtonClick() {
        // Handle contact button click
    }

    @FXML
    private void handleBooksButtonClick() {
        mainBoard.getChildren().clear();
        try {
            Stage stage = (Stage) dashboard.getScene().getWindow();
            double height = stage.getHeight();
            double width = stage.getWidth();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/abudu/lms/library/book.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error loading book view", e);
        }
    }

    @FXML
    private void handleUsersButtonClick() {
        // Handle users button click
    }

    @FXML
    private void handleSettingsButtonClick() {
        // Handle settings button click
    }

    public void handleDarkModeToggle(ActionEvent actionEvent) {
        // Handle dark mode toggle
    }

    public void handleReportsButtonClick(ActionEvent actionEvent) {
        // Handle reports button click
    }

    public void handleLogoutClick(ActionEvent actionEvent) {
        // Handle logout click
    }

    @FXML
    private void handleLogoutButtonClick(ActionEvent actionEvent) {
        if ("Logout".equals(logoutButton.getText())) {
            UserSession.getInstance().clearSession();
            setUsernameLabel("Guest");
            setLogoutButtonText("Login");
        } else {
            showLoginDialog();
        }
    }

    private void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/abudu/lms/library/login.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(logoutButton.getScene().getWindow());

            // Set the desired width and height
            dialogStage.setWidth(400);
            dialogStage.setHeight(300);

            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            UserLoginController controller = loader.getController();

            dialogStage.showAndWait();

            // After login dialog is closed, update the username and button text
            User currentUser = UserSession.getInstance().getCurrentUser();
            if (currentUser != null) {
                setUsernameLabel(currentUser.getName());
                setLogoutButtonText("Logout");
            } else {
                setUsernameLabel("Guest");
                setLogoutButtonText("Login");
            }

        } catch (IOException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error loading login dialog", e);
        }
    }
}