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
            UserSession userSession = UserSession.getInstance();
            String username = (userSession.getCurrentUser() != null) ? userSession.getCurrentUser().getName() : "Guest";

            DashboardController controller = loader.getController();
            controller.setUsernameLabel(username);
            controller.setLogoutButtonText(userSession.getCurrentUser() != null ? "Logout" : "Login");

            scene.getStylesheets().add(getClass().getResource("/abudu/lms/library/styles/styles.css").toExternalForm());
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
