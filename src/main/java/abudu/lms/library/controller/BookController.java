package abudu.lms.library.controller;

import abudu.lms.library.models.Book;
import abudu.lms.library.repository.BookRepositoryImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class BookController {

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
    private void handleAddResource() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String category = categoryComboBox.getValue();
        String publisher = publisherField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String description = descriptionArea.getText();

        Book book = new Book(0, title, author, publisher, 2023, isbn, true, category, quantity, description);
        bookRepository.addBook(book);
        refreshTable();
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
}