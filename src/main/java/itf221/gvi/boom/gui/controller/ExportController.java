package itf221.gvi.boom.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for managing the export view of the application.
 */
public class ExportController {

    @FXML
    private AnchorPane toastPane;

    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        showSuccessToast();
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
}
