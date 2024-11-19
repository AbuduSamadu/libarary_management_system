package abudu.lms.library.controller;

import abudu.lms.library.models.OperationType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BookModalController {

    @FXML
    private Label operationLabel;

    private Stage modalStage;
    private OperationType currentOperation;

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    public void setOperation(OperationType operation) {
        this.currentOperation = operation;
        operationLabel.setText("Operation: " + operation.name());
    }

    @FXML
    private void handleClose() {
        modalStage.close();
    }
}
