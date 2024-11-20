package abudu.lms.library.views.landingPage;

import abudu.lms.library.controller.DashboardController;
import abudu.lms.library.utils.UserSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/abudu/lms/library/dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            String username = UserSession.getInstance().getUsername();

            DashboardController controller = loader.getController();
            if (username != null) {
                controller.setUsernameLabel(username);
                controller.setLogoutButtonText("Logout");
            } else {
                controller.setUsernameLabel("Guest");
                controller.setLogoutButtonText("Login");
            }
            scene.getStylesheets().add(getClass().getResource("/abudu/lms/library/Styles/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Dashboard");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}