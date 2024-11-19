package abudu.lms.library;

import javafx.application.Application;
import javafx.stage.Stage;
import abudu.lms.library.views.landingPage.Dashboard;

public class LibraryManagementSystem extends Application {
    @Override
    public void start(Stage stage) {
        Dashboard dashboard = new Dashboard();
        dashboard.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}