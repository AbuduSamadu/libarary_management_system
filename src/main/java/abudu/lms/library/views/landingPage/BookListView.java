package abudu.lms.library.views.landingPage;

import abudu.lms.library.database.BookDataHandler;
import abudu.lms.library.models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BookListView {

    private final BookDataHandler bookDataHandler;

    public BookListView() {
        this.bookDataHandler = new BookDataHandler();
    }

    public void show(Stage primaryStage) {
        VBox vbox = createBookListView();
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Available Books");
        primaryStage.show();
    }

    public VBox createBookListView() {
        TableView<Book> table = new TableView<>();
        ObservableList<Book> data = FXCollections.observableArrayList(bookDataHandler.getAllBooks());

        // Define columns
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, Void> borrowColumn = new TableColumn<>("Borrow");
        borrowColumn.setCellFactory(createBorrowButtonCellFactory(table, data));

        TableColumn<Book, Void> returnColumn = new TableColumn<>("Return");
        returnColumn.setCellFactory(createReturnButtonCellFactory(table, data));

        // Add columns to the table
        table.setItems(data);
        table.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, borrowColumn, returnColumn);

        // Handle empty table
        table.setPlaceholder(new Label("No books available."));

        VBox vbox = new VBox(table);
        vbox.setPadding(new Insets(20));
        return vbox;
    }

    private Callback<TableColumn<Book, Void>, TableCell<Book, Void>> createBorrowButtonCellFactory(
            TableView<Book> table, ObservableList<Book> data) {
        return param -> new TableCell<>() {
            private final Button borrowButton = new Button("Borrow");

            {
                borrowButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    try {
                        if (bookDataHandler.borrowBook(book.getId())) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Book borrowed successfully.");
                            refreshTableData(data);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Book is already borrowed.");
                        }
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to borrow book: " + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : borrowButton);
            }
        };
    }

    private Callback<TableColumn<Book, Void>, TableCell<Book, Void>> createReturnButtonCellFactory(
            TableView<Book> table, ObservableList<Book> data) {
        return param -> new TableCell<>() {
            private final Button returnButton = new Button("Return");

            {
                returnButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    try {
                        if (bookDataHandler.returnBook(book.getId())) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully.");
                            refreshTableData(data);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Book is not currently borrowed.");
                        }
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to return book: " + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : returnButton);
            }
        };
    }

    private void refreshTableData(ObservableList<Book> data) {
        data.setAll(bookDataHandler.getAllBooks());
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
