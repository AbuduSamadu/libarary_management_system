package abudu.lms.library.controller;

import abudu.lms.library.database.BookDataHandler;
import abudu.lms.library.models.Book;
import abudu.lms.library.utils.ISBNGenerator;
import abudu.lms.library.utils.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

public class BookController {

    public AnchorPane mainContainer;
    public Button saveButton;
    public Button cancelButton;
    @FXML
    private TextField titleField;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField authorField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField isbnField;

    private final BookDataHandler bookDataHandler;

    public BookController() {
        this.bookDataHandler = new BookDataHandler();
    }

    @FXML
    public void addBook() {
        if (!UserSession.isAuthenticated()) {
            showAlert(AlertType.ERROR, "Authentication Error", "User is not authenticated");
            return;
        }


        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        int year = Integer.parseInt(yearField.getText());
        String isbn = ISBNGenerator.generateISBN(); // Generate ISBN using ISBNGenerator

        Book book = new Book(0, title, author, publisher, year, isbn, true);
        boolean success = bookDataHandler.addBook(book);

        showAlert(success ? AlertType.INFORMATION : AlertType.ERROR, "Add Book", success ? "Book added successfully" : "Failed to add book");
        if (success) {
            clearFields();
        }
    }

    @FXML
    public void updateBook() {
        int id = Integer.parseInt(isbnField.getText()); // Assuming ISBN is used as ID for simplicity
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        int year = Integer.parseInt(yearField.getText());
        String isbn = isbnField.getText();

        Book book = new Book(id, title, author, publisher, year, isbn, true);
        boolean success = bookDataHandler.updateBook(book);

        showAlert(success ? AlertType.INFORMATION : AlertType.ERROR, "Update Book", success ? "Book updated successfully" : "Failed to update book");
    }

    @FXML
    public void deleteBook() {
        int id = Integer.parseInt(isbnField.getText()); // Assuming ISBN is used as ID for simplicity
        boolean success = bookDataHandler.deleteBook(id);

        showAlert(success ? AlertType.INFORMATION : AlertType.ERROR, "Delete Book", success ? "Book deleted successfully" : "Failed to delete book");
        if (success) {
            clearFields();
        }
    }

    @FXML
    public void getBookById() {
        int id = Integer.parseInt(isbnField.getText()); // Assuming ISBN is used as ID for simplicity
        Book book = bookDataHandler.getBookById(id);

        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            publisherField.setText(book.getPublisher());
            yearField.setText(String.valueOf(book.getYear()));
            isbnField.setText(book.getIsbn());
        } else {
            showAlert(AlertType.ERROR, "Get Book", "Book not found");
        }
    }

    @FXML
    public void getAllBooks() {
        List<Book> books = bookDataHandler.getAllBooks();
        // Display books in your UI
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        yearField.clear();
        isbnField.clear();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}