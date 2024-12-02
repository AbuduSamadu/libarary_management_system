package abudu.lms.library.controller;

import abudu.lms.library.models.User;
import abudu.lms.library.services.DashboardService;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {

    public Button reserveButton;
    public Button borrowButton;
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

    private DashboardService dashboardService;

    public DashboardController() {
    }

    // Setter for DashboardService
    public void setDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
        updateDashboardData();

    }

    private void updateDashboardData() {
        int totalUsers = dashboardService.getTotalUsers();
        int totalBooks = dashboardService.getTotalBooks();

        totalUsersLabel.setText(String.valueOf(totalUsers));
        totalBooksLabel.setText(String.valueOf(totalBooks));
    }

    @FXML
    private void initialize() {
      updateUserSessionUI();
    }


    @FXML
    private void handleHomeButtonClick() {
        // Handle home button click
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
    private void handleSettingsButtonClick() {
        // Handle settings button click
    }
    public void setUsernameLabel(String username) {
        usernameLabel.setText("Welcome, " + username);
    }

    public void setLogoutButtonText(String text) {
        logoutButton.setText(text);
    }

    @FXML
    private void handleLogoutButtonClick(ActionEvent actionEvent) {
        if ("Logout".equals(logoutButton.getText())) {
            UserSession.getInstance().clearSession();
            updateUserSessionUI();
        } else {
            showLoginDialog();
        }
    }


    private void showLoginDialog() {
        try {
         loadScene("/abudu/lms/library/login.fxml", "Login", 400, 300);

            // After login dialog is closed, update the username and button text
           updateUserSessionUI();

        } catch (IOException e) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Error loading login dialog", e);
        }
    }

    private void updateUserSessionUI() {
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



    public void handleReserveButtonClick(ActionEvent actionEvent) throws IOException {
        loadScene("/abudu/lms/library/reserve.fxml", "Reserve Book",1080, 720);
    }

    public void handleBorrowButtonClick(ActionEvent actionEvent) throws IOException {
        loadScene("/abudu/lms/library/borrow.fxml", "Borrow Book",1080, 720);
    }



    private void loadScene(String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) dashboard.getScene().getWindow();
        double height = stage.getHeight();
        double width = stage.getWidth();
        loadScene(fxmlPath, title, width, height);
    }

    private void loadScene(String fxmlPath, String title, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) dashboard.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
