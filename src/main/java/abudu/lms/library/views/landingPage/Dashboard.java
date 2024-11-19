package abudu.lms.library.views.landingPage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard extends Application {

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

        // Create the main board
        VBox mainBoard = new VBox();
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

        navbar.getChildren().addAll(homeButton, aboutButton, contactButton);
        return navbar;
    }

    private VBox createSidebar(Stage primaryStage) {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        // Add a logo at the top
        ImageView logo = new ImageView(new Image("file:src/main/resources/logo.png"));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);
        sidebar.getChildren().add(logo);

        // Create navigation buttons
        Button homeButton = createSidebarButton("Home");
        Button booksButton = createSidebarButton("Books");
        Button usersButton = createSidebarButton("Users");
        Button settingsButton = createSidebarButton("Settings");

        // Add buttons to the sidebar
        sidebar.getChildren().addAll(homeButton, booksButton, usersButton, settingsButton);
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Set action for the Users button
        usersButton.setOnAction(e -> loadLoginPage(primaryStage));

        return sidebar;
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/abudu/lms/library/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            scene.getStylesheets().add(getClass().getResource("/abudu/lms/library/dark-theme.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}