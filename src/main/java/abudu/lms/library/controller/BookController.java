package abudu.lms.library.controller;

import abudu.lms.library.database.BookDataHandler;
import abudu.lms.library.models.Book;
import abudu.lms.library.models.BookOperation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField idField;

    private final BookDataHandler bookDataHandler;
    private BookOperation currentOperation;
    private Book currentBook;

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
            yearField.setText(String.valueOf(book.getYear()));
        }
    }

    @FXML
    private void handleSave() {
        int id = Integer.parseInt(idField.getText());
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        int year = Integer.parseInt(yearField.getText());
        String isbn = isbnField.getText();
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
    }

    @FXML
    private void handleCancel() {
        // Implement cancel logic
    }
}
