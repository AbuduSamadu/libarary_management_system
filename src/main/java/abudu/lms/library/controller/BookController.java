package abudu.lms.library.controller;

import abudu.lms.library.models.Book;
import abudu.lms.library.models.User;
import abudu.lms.library.repository.BookRepositoryImpl;
import abudu.lms.library.security.AccessControl;
import abudu.lms.library.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BookController {

    @FXML
    private Button homeButton, addBookButton;
    @FXML
    private TableView<Book> resourcesTable;
    @FXML
    private TableView journalTable;
    @FXML
    private TextField titleField, authorField, isbnField, publisherField, quantityField, filterPublisherField, searchField;
    @FXML
    private ComboBox<String> categoryComboBox, filterCategoryComboBox, filterStatusComboBox;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker yearPicker, acquisitionDatePicker;
    @FXML
    private TableColumn<Book, Integer> idColumn, quantityColumn;
    @FXML
    private TableColumn<Book, String> titleColumn, authorColumn, isbnColumn, categoryColumn, publicationColumn, descriptionColumn, availableColumn, actionsColumn, formatColumn;
    @FXML
    private Label statusLabel, totalResourcesLabel, availableResourcesLabel, checkedOutLabel;
    @FXML
    private VBox resourceBox;

    private final BookRepositoryImpl bookRepository;

    public BookController() {
        this.bookRepository = new BookRepositoryImpl();
    }

    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList("Books", "Magazine", "Journal"));
        handleFetchBook();
        checkUserRole();
        setupTableColumns();
        setupSearchField();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty().asObject().asString());
        publicationColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        availableColumn.setCellValueFactory(cellData -> cellData.getValue().availableProperty().asObject().asString());
        actionsColumn.setCellValueFactory(cellData -> cellData.getValue().actionsProperty());

        actionsColumn.setCellFactory(col -> new TableCell<Book, String>() {
            private final Button reserveButton = new Button("Reserve");
            private final Button borrowButton = new Button("Borrow");

            {
                reserveButton.getStyleClass().add("button-reserve");
                borrowButton.getStyleClass().add("button-borrow");

                reserveButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    // Handle reserve action
                    handleReserveBook(book);
                });

                borrowButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleBorrowBook(book);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, reserveButton, borrowButton);
                    setGraphic(buttons);
                }
            }
        });

        resourcesTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setPrefHeight(40); // Adjust the height as needed
            return row;
        });
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));
    }

    @FXML
    private void handleAddResource() {
        if (UserSession.getInstance().getCurrentUser() == null) {
            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to add a book.");
            return;
        }

        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryComboBox.getValue();
        String publisher = publisherField.getText();
        int year = yearPicker.getValue().getYear();
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
        long userId = UserSession.getInstance().getCurrentUser().getId();
        if (userId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid User", "The logged in user is invalid.");
            return;
        }

        Book book = new Book(0, title, author, publisher, year, (int) isbn, true, category, quantity, description, userId);
        try {
            bookRepository.addBook(book);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add book.");
        }
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
        List<Book> books = bookRepository.getAllBooks();
        if (books.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No books available to export.");
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
                    exportToExcel(books);
                    break;
                case "PDF":
                    exportToPDF(books);
                    break;
                case "Image":
                    exportToImage(books);
                    break;
            }
        });
    }

    private void exportToPDF(List<Book> books) {
        // Implement export to PDF logic
    }

    private void exportToExcel(List<Book> books) {
        // Implement export to Excel logic
    }

    private void exportToImage(List<Book> books) {
        TableView<Book> tableView = new TableView<>();
        tableView.getItems().setAll(books);

        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty().asObject().asString());

        TableColumn<Book, String> publisherColumn = new TableColumn<>("Publisher");
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        TableColumn<Book, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Book, Boolean> availableColumn = new TableColumn<>("Available");
        availableColumn.setCellValueFactory(cellData -> cellData.getValue().availableProperty().asObject());

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, publisherColumn, categoryColumn, quantityColumn, descriptionColumn, availableColumn);

        WritableImage image = tableView.snapshot(null, null);

        File file = new File("books.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Books exported to Image successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to export books to Image.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Books");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            importBooksFromFile(selectedFile);
        }
    }

    private void importBooksFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 10) {
                    String title = fields[0];
                    String author = fields[1];
                    String publisher = fields[2];
                    int year = Integer.parseInt(fields[3]);
                    int isbn = Integer.parseInt(fields[4]);
                    boolean available = Boolean.parseBoolean(fields[5]);
                    String category = fields[6];
                    int quantity = Integer.parseInt(fields[7]);
                    String description = fields[8];
                    long userId = Long.parseLong(fields[9]);

                    Book book = new Book(0, title, author, publisher, year, isbn, available, category, quantity, description, userId);
                    bookRepository.addBook(book);
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Books imported successfully.");
            refreshTable();
        } catch (IOException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to import books.");
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
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, "Error loading dashboard view", e);
        }
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

    private void filterBooks(String query) {
        List<Book> books = bookRepository.getAllBooks();
        if (query == null || query.isEmpty()) {
            resourcesTable.getItems().setAll(books);
        } else {
            List<Book> filteredBooks = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(query.toLowerCase()) ||
                            String.valueOf(book.getIsbn()).contains(query))
                    .collect(Collectors.toList());
            resourcesTable.getItems().setAll(filteredBooks);
        }
        updateStatusLabels(resourcesTable.getItems());
    }

    private void checkUserRole() {
        UserSession userSession = UserSession.getInstance();
        User currentUser = userSession.getCurrentUser();
//        if (currentUser != null && !AccessControl.hasRole(currentUser, "librarian")) {
//            addBookButton.setVisible(false);
//        }
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBorrowBook(Book book) {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to borrow a book.");
            return;
        }

        if (!book.isAvailable()) {
            showAlert(Alert.AlertType.ERROR, "Unavailable", "This book is currently unavailable.");
            return;
        }

        long userId = currentUser.getId();
        if (userId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid User", "The logged in user is invalid.");
            return;
        }

        try {
            bookRepository.borrowBook(book.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to borrow book.");
        }
    }

    @FXML
    private void handleReturnBook(Book book) {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to return a book.");
            return;
        }

        long userId = currentUser.getId();
        if (userId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid User", "The logged in user is invalid.");
            return;
        }

        try {
            bookRepository.returnBook(book.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return book.");
        }
    }

    @FXML
    private void handleReserveBook(Book book) {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Authentication Required", "You must be logged in to reserve a book.");
            return;
        }

        long userId = currentUser.getId();
        if (userId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid User", "The logged in user is invalid.");
            return;
        }
        try {
            bookRepository.reserveBook(book.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book reserved successfully.");
            refreshTable();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to reserve book.");
        }
    }


}