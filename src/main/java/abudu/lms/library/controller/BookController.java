package abudu.lms.library.controller;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepositoryImpl;
import abudu.lms.library.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookController {

    public Button homeButton;
    public TableView journalTable;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField isbnField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> filterCategoryComboBox;
    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private TextField filterPublisherField;
    @FXML
    private DatePicker acquisitionDatePicker;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> resourcesTable;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, String> publicationColumn;
    @FXML
    private TableColumn<Book, Integer> issueNumberColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TableColumn<Book, String> availableColumn;
    @FXML
    private TableColumn<Book, String> formatColumn;
    @FXML
    private TableColumn<Book, String> actionsColumn;
    @FXML
    private Label statusLabel;
    @FXML
    private Label totalResourcesLabel;
    @FXML
    private Label availableResourcesLabel;
    @FXML
    private Label checkedOutLabel;
    @FXML
    private VBox resourceBox;

    private final BookRepositoryImpl bookRepository;

    public BookController() {
        this.bookRepository = new BookRepositoryImpl();
    }

    @FXML
    private DatePicker yearPicker;

    @FXML
    private void handleAddResource() {
        // Check if the user is authenticated
//        if (UserSession.getInstance().getCurrentUser() == null) {
//            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to add a book.");
//            return;
//        }

        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryComboBox.getValue();
        String publisher = publisherField.getText();
        int year = yearPicker.getValue().getYear(); // Get the year from DatePicker
        int quantity;
        String isbnStr = isbnField.getText();
        if (!isbnStr.matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid ISBN", "ISBN must be exactly 13 digits.");
            return;
        }
        long isbn = Long.parseLong(isbnStr);
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be a valid integer.");
            return;
        }
        String description = descriptionArea.getText();
//        long userId = UserSession.getInstance().getCurrentUser().getId();// Get the logged in user id
//        if (userId <= 0) {
//            System.out.println(" userId" + userId);
//            showAlert(Alert.AlertType.ERROR, "Invalid User", "The logged in user is invalid.");
//            return;
//        }

        Book book = new Book(0, title, author, publisher, year, (int) isbn, true, category, quantity, description, 15);
        try {
            bookRepository.addBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add book.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList("Books", "Magazine", "Journal"));
        handleFetchBook();
    }

    @FXML
    private void handleClearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        categoryComboBox.setValue(null);
        publisherField.clear();
        quantityField.clear();
        descriptionArea.clear();
    }

    @FXML
    private void handleApplyFilters() {
        // Implement filter logic
    }

    @FXML
    private void handleExport() {
        // Implement export logic
    }

    @FXML
    private void handleImport() {
        // Implement import logic
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
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, "Error loading dashboard view", e);
        }
        // Implement home button logic
    }

    private void refreshTable() {
        List<Book> books = bookRepository.getAllBooks();
        resourcesTable.getItems().setAll(books);
        updateStatusLabels(books);
    }

    private void updateStatusLabels(List<Book> books) {
        int totalResources = books.size();
        long availableResources = books.stream().filter(Book::isAvailable).count();
        long checkedOutResources = totalResources - availableResources;

        totalResourcesLabel.setText("Total Resources: " + totalResources);
        availableResourcesLabel.setText(" | Available: " + availableResources);
        checkedOutLabel.setText(" | Checked Out: " + checkedOutResources);
    }
    @FXML
    private void handleFetchBook() {
        try {
            List<Book> books = bookRepository.getAllBooks();
            if (books != null && !books.isEmpty()) {
                displayBooks(books);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No books found in the database.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch book details.");
        }
    }

    private void displayBooks(List<Book> books) {
        resourcesTable.getItems().clear();
        resourcesTable.getItems().addAll(books);
    }
}