package itf221.gvi.boom.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.awt.Desktop;
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
        completionScoreToast.setText("✔ Erfolg! Erfüllungsscore: " + LoadingController.completionScore + "%");
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

    /**
     * Opens the user documentation PDF file.
     */
    @FXML
    private void openDocumentation() {
        File pdfFile = new File("src/main/resources/itf221/gvi/boom/documentation/Benutzerdoku.pdf");
        try {
            Desktop.getDesktop().open(pdfFile);
        } catch (IOException e) {
            System.out.println("Can't find documentation");
        }
    }
}
