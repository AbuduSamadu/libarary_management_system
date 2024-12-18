package abudu.lms.library.controller;


import abudu.lms.library.models.Borrowing;
import abudu.lms.library.models.User;
import abudu.lms.library.repository.BorrowingRepositoryImpl;
import abudu.lms.library.security.AccessControl;
import abudu.lms.library.utils.UserSession;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BorrowController {

    private final BorrowingRepositoryImpl borrowingRepository;

    @FXML
    private TextField authorField;
    @FXML
    private TableView<Borrowing> borrowingsTable;
    @FXML
    private TextField titleField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField userIdField;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker borrowDatePicker;
    @FXML
    private TextArea notesArea;
    @FXML
    private Button homeButton, borrowButton;
    @FXML
    private TableColumn<Borrowing, Integer> idColumn;
    @FXML
    private TableColumn<Borrowing, String> titleColumn, authorColumn, isbnColumn, userIdColumn, notesColumn, borrowDateColumn, actionsColumn;
    @FXML
    private Label statusLabel, totalBorrowingsLabel, activeBorrowingsLabel, completedBorrowingsLabel;

    public BorrowController() {
        this.borrowingRepository = new BorrowingRepositoryImpl();
    }

    @FXML
    private void initialize() {
        handleFetchBorrowings();
        checkUserRole();
        setupTableColumns();
        setupSearchField();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty().asObject().asString());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject().asString());
        borrowDateColumn.setCellValueFactory(cellData -> cellData.getValue().borrowDateProperty());
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        actionsColumn.setCellFactory(col -> new TableCell<Borrowing, String>() {
            private final Button returnButton = new Button("Return");

            {
                returnButton.getStyleClass().add("button-return");

                returnButton.setOnAction(event -> {
                    Borrowing borrowing = getTableView().getItems().get(getIndex());
                    // Handle return action
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, returnButton);
                    setGraphic(buttons);
                }
            }
        });

        borrowingsTable.setRowFactory(tv -> {
            TableRow<Borrowing> row = new TableRow<>();
            row.setPrefHeight(40); // Adjust the height as needed
            return row;
        });
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBorrowings(newValue));
    }

    @FXML
    void handleBorrowBook() {
        if (UserSession.getInstance().getCurrentUser() == null) {
            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to borrow a book.");
            return;
        }

        String title = titleField.getText();
        String author = authorField.getText();
        String isbnStr = isbnField.getText();
        if (!isbnStr.matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid ISBN", "ISBN must be exactly 13 digits.");
            return;
        }
        long isbn = Long.parseLong(isbnStr);
        String userIdStr = userIdField.getText();
        long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID must be a valid integer.");
            return;
        }
        String notes = notesArea.getText();
        String borrowDate = borrowDatePicker.getValue().toString();

        Borrowing borrowing = new Borrowing(0, title, author, isbn, userId, borrowDate, notes);
        try {
            borrowingRepository.addBorrowing(borrowing);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to borrow book.");
        }
    }

    @FXML
    private void handleClearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        userIdField.clear();
        borrowDatePicker.setValue(null);
        notesArea.clear();
    }

    @FXML
    private void handleExport() {
        List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
        if (borrowings.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No borrowings available to export.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Excel", "Excel", "PDF", "Image");
        dialog.setTitle("Export Format");
        dialog.setHeaderText("Select Export Format");
        dialog.setContentText("Choose format:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(format -> {
            switch (format) {
                case "Excel":
                    exportToExcel(borrowings);
                    break;
                case "PDF":
                    exportToPDF(borrowings);
                    break;
                case "Image":
                    exportToImage(borrowings);
                    break;
            }
        });
    }

    private void exportToPDF(List<Borrowing> borrowings) {
        // Implement export to PDF logic
    }

    private void exportToExcel(List<Borrowing> borrowings) {
        // Implement export to Excel logic
    }

    private void exportToImage(List<Borrowing> borrowings) {
        TableView<Borrowing> tableView = new TableView<>();
        tableView.getItems().setAll(borrowings);

        TableColumn<Borrowing, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Borrowing, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Borrowing, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        TableColumn<Borrowing, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty().asObject().asString());

        TableColumn<Borrowing, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject().asString());

        TableColumn<Borrowing, String> borrowDateColumn = new TableColumn<>("Borrow Date");
        borrowDateColumn.setCellValueFactory(cellData -> cellData.getValue().borrowDateProperty());

        TableColumn<Borrowing, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, userIdColumn, borrowDateColumn, notesColumn);

        WritableImage image = tableView.snapshot(null, null);

        File file = new File("borrowings.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Borrowings exported to Image successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to export borrowings to Image.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Borrowings");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            importBorrowingsFromFile(selectedFile);
        }
    }

    private void importBorrowingsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 7) {
                    String title = fields[0];
                    String author = fields[1];
                    long isbn = Long.parseLong(fields[2]);
                    long userId = Long.parseLong(fields[3]);
                    String borrowDate = fields[4];
                    String notes = fields[5];

                    Borrowing borrowing = new Borrowing(0, title, author, isbn, userId, borrowDate, notes);
                    borrowingRepository.addBorrowing(borrowing);
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Borrowings imported successfully.");
            refreshTable();
        } catch (IOException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to import borrowings.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerateReport() {
        // Implement generate report logic
    }

    @FXML
    private void handleRefresh() {
        refreshTable();
    }

    @FXML
    private void handleHomeButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/abudu/lms/library/dashboard.fxml"));
            BorderPane dashboardView = loader.load();
            Scene scene = new Scene(dashboardView);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(BorrowController.class.getName()).log(Level.SEVERE, "Error loading dashboard view", e);
        }
    }

    private void refreshTable() {
        List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
        borrowingsTable.getItems().setAll(borrowings);
        updateStatusLabels(borrowings);
    }

    private void updateStatusLabels(List<Borrowing> borrowings) {
        int totalBorrowings = borrowings.size();
        long activeBorrowings = borrowings.stream().filter(Borrowing::isActive).count();
        long completedBorrowings = totalBorrowings - activeBorrowings;

        totalBorrowingsLabel.setText("Total Borrowings: " + totalBorrowings);
        activeBorrowingsLabel.setText(" | Active: " + activeBorrowings);
        completedBorrowingsLabel.setText(" | Completed: " + completedBorrowings);
    }

    private void filterBorrowings(String query) {
        List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
        if (query == null || query.isEmpty()) {
            borrowingsTable.getItems().setAll(borrowings);
        } else {
            List<Borrowing> filteredBorrowings = borrowings.stream()
                    .filter(borrowing -> borrowing.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            borrowing.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                            String.valueOf(borrowing.getIsbn()).contains(query))
                    .collect(Collectors.toList());
            borrowingsTable.getItems().setAll(filteredBorrowings);
        }
        updateStatusLabels(borrowingsTable.getItems());
    }

    private void checkUserRole() {
        UserSession userSession = UserSession.getInstance();
        User currentUser = userSession.getCurrentUser();
        if (currentUser != null && !AccessControl.hasRole(currentUser, "librarian")) {
            System.out.println("User Role" + currentUser.getRoles());
            borrowButton.setVisible(false);
        }
    }

    @FXML
    private void handleFetchBorrowings() {
        try {
            List<Borrowing> borrowings = borrowingRepository.getAllBorrowings();
            if (borrowings != null && !borrowings.isEmpty()) {
                displayBorrowings(borrowings);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No borrowings found in the database.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch borrowing details.");
        }
    }

    private void displayBorrowings(List<Borrowing> borrowings) {
        borrowingsTable.getItems().clear();
        borrowingsTable.getItems().addAll(borrowings);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}