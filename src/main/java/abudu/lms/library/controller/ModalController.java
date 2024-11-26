package abudu.lms.library.controller;

import abudu.lms.library.models.BookOperation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ModalController {

    @FXML
    private Label operationLabel;

    private Stage modalStage;

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setOperation(BookOperation operation) {
        operationLabel.setText("Operation: " + operation.name());
    }

    @FXML
    private void handleClose() {
        modalStage.close();
    }
}