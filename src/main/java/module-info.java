module abudu.lms.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    // Add this line
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires java.dotenv;
    requires javafx.swing;
    requires annotations;

    opens abudu.lms.library to javafx.fxml;
    opens abudu.lms.library.controller to javafx.fxml;
    opens abudu.lms.library.views.landingPage to javafx.graphics;

    exports abudu.lms.library;
    exports abudu.lms.library.views.landingPage;
}