package abudu.lms.library.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Button hamburger;

    @FXML
    private MenuItem handleMenuSettings;

    @FXML
    private MenuItem handleMenuClose;

    @FXML
    private MenuItem handleMenuAddBook;

    @FXML
    private MenuItem handleMenuAddMember;

    @FXML
    private MenuItem handleMenuViewBook;

    @FXML
    private MenuItem handleMenuViewMemberList;

    @FXML
    private MenuItem handleIssuedList;

    @FXML
    private MenuItem handleMenuFullScreen;

    @FXML
    private MenuItem handleMenuOverdueNotification;

    @FXML
    private MenuItem handleAboutMenu;

    @FXML
    private void handleMenuSettings() {
        showAlert(AlertType.INFORMATION, "Settings", "Settings menu clicked");
    }

    @FXML
    private void handleMenuClose() {
        System.exit(0);
    }

    @FXML
    private void handleMenuAddBook() {
        showAlert(AlertType.INFORMATION, "Add Book", "Add Book menu clicked");
    }

    @FXML
    private void handleMenuAddMember() {
        showAlert(AlertType.INFORMATION, "Add Member", "Add Member menu clicked");
    }

    @FXML
    private void handleMenuViewBook() {
        showAlert(AlertType.INFORMATION, "View Book", "View Book menu clicked");
    }

    @FXML
    private void handleMenuViewMemberList() {
        showAlert(AlertType.INFORMATION, "View Member List", "View Member List menu clicked");
    }

    @FXML
    private void handleIssuedList() {
        showAlert(AlertType.INFORMATION, "Issued Book List", "Issued Book List menu clicked");
    }

    @FXML
    private void handleMenuFullScreen() {
        showAlert(AlertType.INFORMATION, "Full Screen", "Full Screen menu clicked");
    }

    @FXML
    private void handleMenuOverdueNotification() {
        showAlert(AlertType.INFORMATION, "Overdue Notification", "Overdue Notification menu clicked");
    }

    @FXML
    private void handleAboutMenu() {
        showAlert(AlertType.INFORMATION, "About", "About menu clicked");
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public void loadBookInfo(ActionEvent actionEvent) {
    }

    public void loadMemberInfo(ActionEvent actionEvent) {
    }

    public void loadIssueOperation(ActionEvent actionEvent) {
    }

    public void loadBookInfo2(ActionEvent actionEvent) {
    }

    public void loadRenewOp(ActionEvent actionEvent) {
    }

    public void loadSubmissionOp(ActionEvent actionEvent) {
    }

    public void handleIssueButtonKeyPress(KeyEvent keyEvent) {

    }
}