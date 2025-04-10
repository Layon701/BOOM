package itf221.gvi.boom.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Controller class for managing the export view of the application.
 */
public class ExportController {

    @FXML
    private AnchorPane toastPane;

    @FXML
    private Label completionScoreToast;

    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        showSuccessToast();
        completionScoreToast.setText("✔ Erfolg! Erfüllungsscore: " + MainController.completionScore + "%");
    }

    /**
     * Shows the success toast message.
     */
    private void showSuccessToast() {
        toastPane.setVisible(true);
    }

    /**
     * Closes the toast when the user clicks the "x" button.
     */
    @FXML
    public void closeToast() {
        toastPane.setVisible(false);
    }

    @FXML
    private void openDocumentation() {
        File pdfFile = new File("src/main/resources/itf221/gvi/boom/documentation/Benutzerdoku.pdf");
        if (pdfFile.exists()) {
            try {
                Desktop.getDesktop().open(pdfFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found!");
        }
    }
}
