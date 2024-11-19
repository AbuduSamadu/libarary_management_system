package abudu.lms.library.controller;

import abudu.lms.library.database.BookDataHandler;
import abudu.lms.library.models.Book;
import abudu.lms.library.models.BookOperation;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.UUID;

public class BookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private DatePicker yearPicker;

    private final BookDataHandler bookDataHandler;
    private BookOperation currentOperation;
    private Book currentBook;
    private Stage modalStage;

    public BookController() {
        this.bookDataHandler = new BookDataHandler();
    }

    public void setOperation(BookOperation operation, Book book) {
        this.currentOperation = operation;
        this.currentBook = book;
        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            publisherField.setText(book.getPublisher());
            yearPicker.setValue(LocalDate.of(book.getYear(), 1, 1));
        }
    }

    @FXML
    private void handleSave() {
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        int year = yearPicker.getValue().getYear();
        String isbn = UUID.randomUUID().toString(); // Generate a unique ISBN
        int id = bookDataHandler.generateNewId(); // Implement this method to generate a new ID
        boolean available = true;

        Book book = new Book(id, title, author, publisher, year, isbn, available);

        switch (currentOperation) {
            case ADD:
                bookDataHandler.addBook(book);
                break;
            case UPDATE:
                book.setId(currentBook.getId());
                bookDataHandler.updateBook(book);
                break;
            case DELETE:
                bookDataHandler.deleteBook(currentBook.getId());
                break;
            case BORROW:
                // Implement borrow logic
                break;
            case RETURN:
                // Implement return logic
                break;
        }
        modalStage.close();
    }

    @FXML
    private void handleCancel() {
        modalStage.close();
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }
}