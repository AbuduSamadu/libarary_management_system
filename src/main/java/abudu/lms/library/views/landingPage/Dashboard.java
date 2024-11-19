package abudu.lms.library.views.landingPage;

import java.io.IOException;

import abudu.lms.library.controller.BookController;
import abudu.lms.library.controller.ModalController;
import abudu.lms.library.models.BookOperation;
import abudu.lms.library.utils.UserSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dashboard extends Application {

    private final UserSession userSession;

    public Dashboard() {
        this.userSession = UserSession.getInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        // Create the main layout
        BorderPane root = new BorderPane();

        // Create the navbar
        HBox navbar = createNavbar();
        root.setTop(navbar);

        // Create the sidebar
        VBox sidebar = createSidebar(primaryStage);
        root.setLeft(sidebar);

        // Render the BookListView on the main dashboard
        BookListView bookListView = new BookListView();
        VBox mainBoard = bookListView.createBookListView();
        mainBoard.setPadding(new Insets(20));
        root.setCenter(mainBoard);

        // Create a scene and add the layout to it
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard");
        primaryStage.show();
    }

    private HBox createNavbar() {
        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(10));
        navbar.setStyle("-fx-background-color: #34495e;");
        navbar.setAlignment(Pos.CENTER);

        Button homeButton = new Button("Home");
        Button aboutButton = new Button("About");
        Button contactButton = new Button("Contact");

        Label usernameLabel = new Label("Welcome, " + userSession.getUsername());
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        navbar.getChildren().addAll(homeButton, aboutButton, contactButton);
        return navbar;
    }

    private VBox createSidebar(Stage primaryStage) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        // Add a logo at the top
        ImageView logo = new ImageView(new Image("file:src/main/resources/abudu/lms/library/logo.svg"));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);
        sidebar.getChildren().add(logo);

        // Create navigation buttons
        Button homeButton = createSidebarButton("Home");
        Button booksButton = createSidebarButton("Books");
        Button usersButton = createSidebarButton("Users");
        Button settingsButton = createSidebarButton("Settings");

        // Create dropdown menu for Books button
        VBox booksDropdown = createBooksDropdown(primaryStage);
        booksDropdown.setVisible(false);

        // Toggle dropdown menu visibility on click
        booksButton.setOnAction(e -> booksDropdown.setVisible(!booksDropdown.isVisible()));

        // Add buttons and dropdown to the sidebar
        sidebar.getChildren().addAll(homeButton, booksButton, usersButton, booksDropdown, settingsButton);
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Set action for the Users button
        usersButton.setOnAction(e -> loadLoginPage(primaryStage));

        return sidebar;
    }

    private VBox createBooksDropdown(Stage primaryStage) {
        VBox dropdown = new VBox(10);
        dropdown.setPadding(new Insets(10));
        dropdown.setStyle("-fx-background-color: #34495e;");

        Button addBookButton = new Button("Add Book");
        Button updateBookButton = new Button("Update Book");
        Button deleteBookButton = new Button("Delete Book");
        Button borrowBookButton = new Button("Borrow Book");
        Button returnBookButton = new Button("Return Book");

        addBookButton.setOnAction(e -> openBookModal(primaryStage, BookOperation.ADD));
        updateBookButton.setOnAction(e -> openBookModal(primaryStage, BookOperation.UPDATE));
        deleteBookButton.setOnAction(e -> openBookModal(primaryStage, BookOperation.DELETE));
        borrowBookButton.setOnAction(e -> openBookModal(primaryStage, BookOperation.BORROW));
        returnBookButton.setOnAction(e -> openBookModal(primaryStage, BookOperation.RETURN));

        dropdown.getChildren().addAll(addBookButton, updateBookButton, deleteBookButton, borrowBookButton, returnBookButton);
        return dropdown;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #3b5998; -fx-text-fill: white; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;"));
        return button;
    }

    private void loadLoginPage(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/abudu/lms/library/register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 620, 540);
            scene.getStylesheets().add(getClass().getResource("/abudu/lms/library/dark-theme.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openBookModal(Stage primaryStage, BookOperation operation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/abudu/lms/library/book_modal.fxml"));
            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(loader.load()));
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(primaryStage);
            modalStage.setTitle("Book Operation");

            BookController controller = loader.getController();
            controller.setModalStage(modalStage);
            controller.setOperation(operation, null);

            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}