package abudu.lms.library.controller;

import abudu.lms.library.database.UserDataHandler;
import abudu.lms.library.models.Reservation;
import abudu.lms.library.models.User;
import abudu.lms.library.repository.ReservationRepositoryImpl;
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
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReserveController {

    @FXML
    private Button homeButton, reserveButton;
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TextField titleField, authorField, isbnField, userIdField, searchField;
    @FXML
    private DatePicker reservationDatePicker;
    @FXML
    private TextArea notesArea;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, String> titleColumn, authorColumn, isbnColumn, userIdColumn, notesColumn, reservationDateColumn, actionsColumn;
    @FXML
    private Label statusLabel, totalReservationsLabel, activeReservationsLabel, completedReservationsLabel;

    private final ReservationRepositoryImpl reservationRepository;

    public ReserveController() {
        this.reservationRepository = new ReservationRepositoryImpl();
    }

    @FXML
    private void initialize() {
        handleFetchReservations();
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
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        actionsColumn.setCellFactory(col -> new TableCell<Reservation, String>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.getStyleClass().add("button-cancel");

                cancelButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    // Handle cancel action
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, cancelButton);
                    setGraphic(buttons);
                }
            }
        });

        reservationsTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setPrefHeight(40); // Adjust the height as needed
            return row;
        });
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterReservations(newValue));
    }

    @FXML
    private void handleReserveBook() {
        if (UserSession.getInstance().getCurrentUser() == null) {
            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to reserve a book.");
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
        String reservationDate = reservationDatePicker.getValue().toString();

        Reservation reservation = new Reservation(0, title, author, isbn, userId, reservationDate, notes);
        try {
            reservationRepository.addReservation(reservation);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book reserved successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to reserve book.");
        }
    }

    @FXML
    private void handleClearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        userIdField.clear();
        reservationDatePicker.setValue(null);
        notesArea.clear();
    }

    @FXML
    private void handleExport() {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        if (reservations.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No reservations available to export.");
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
                    exportToExcel(reservations);
                    break;
                case "PDF":
                    exportToPDF(reservations);
                    break;
                case "Image":
                    exportToImage(reservations);
                    break;
            }
        });
    }

    private void exportToPDF(List<Reservation> reservations) {
        // Implement export to PDF logic
    }

    private void exportToExcel(List<Reservation> reservations) {
        // Implement export to Excel logic
    }

    private void exportToImage(List<Reservation> reservations) {
        TableView<Reservation> tableView = new TableView<>();
        tableView.getItems().setAll(reservations);

        TableColumn<Reservation, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Reservation, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Reservation, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        TableColumn<Reservation, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty().asObject().asString());

        TableColumn<Reservation, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject().asString());

        TableColumn<Reservation, String> reservationDateColumn = new TableColumn<>("Reservation Date");
        reservationDateColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());

        TableColumn<Reservation, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, userIdColumn, reservationDateColumn, notesColumn);

        WritableImage image = tableView.snapshot(null, null);

        File file = new File("reservations.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reservations exported to Image successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to export reservations to Image.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Reservations");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            importReservationsFromFile(selectedFile);
        }
    }

    private void importReservationsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 7) {
                    String title = fields[0];
                    String author = fields[1];
                    long isbn = Long.parseLong(fields[2]);
                    long userId = Long.parseLong(fields[3]);
                    String reservationDate = fields[4];
                    String notes = fields[5];

                    Reservation reservation = new Reservation(0, title, author, isbn, userId, reservationDate, notes);
                    reservationRepository.addReservation(reservation);
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reservations imported successfully.");
            refreshTable();
        } catch (IOException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to import reservations.");
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
            Logger.getLogger(ReserveController.class.getName()).log(Level.SEVERE, "Error loading dashboard view", e);
        }
    }

    private void refreshTable() {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        reservationsTable.getItems().setAll(reservations);
        updateStatusLabels(reservations);
    }

    private void updateStatusLabels(List<Reservation> reservations) {
        int totalReservations = reservations.size();
        long activeReservations = reservations.stream().filter(Reservation::isActive).count();
        long completedReservations = totalReservations - activeReservations;

        totalReservationsLabel.setText("Total Reservations: " + totalReservations);
        activeReservationsLabel.setText(" | Active: " + activeReservations);
        completedReservationsLabel.setText(" | Completed: " + completedReservations);
    }

    private void filterReservations(String query) {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        if (query == null || query.isEmpty()) {
            reservationsTable.getItems().setAll(reservations);
        } else {
            List<Reservation> filteredReservations = reservations.stream()
                    .filter(reservation -> reservation.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            reservation.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                            String.valueOf(reservation.getIsbn()).contains(query))
                    .collect(Collectors.toList());
            reservationsTable.getItems().setAll(filteredReservations);
        }
        updateStatusLabels(reservationsTable.getItems());
    }

    private void checkUserRole() {
        UserSession userSession = UserSession.getInstance();
        User currentUser = userSession.getCurrentUser();
        System.out.print("UserRole" + currentUser.getRoles());

        if (currentUser.getRoles() != null && !AccessControl.hasRole(currentUser, "librarian")) {
            reserveButton.setVisible(false);
        }
    }

    @FXML
    private void handleFetchReservations() {
        try {
            List<Reservation> reservations = reservationRepository.getAllReservations();
            if (reservations != null && !reservations.isEmpty()) {
                displayReservations(reservations);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No reservations found in the database.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch reservation details.");
        }
    }

    private void displayReservations(List<Reservation> reservations) {
        reservationsTable.getItems().clear();
        reservationsTable.getItems().addAll(reservations);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}