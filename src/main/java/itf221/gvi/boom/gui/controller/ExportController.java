package itf221.gvi.boom.gui.controller;

import itf221.gvi.boom.io.writer.XlsxWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Controller class for managing the export view of the application.
 */
public class ExportController {
    @FXML
    Button downloadLZ_button, downloadAL_button, downloadRZL_button;

    @FXML
    private AnchorPane toastPane;

    @FXML
    private Label completionScoreToast;

    XlsxWriter xlsxWriter = new XlsxWriter();

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

    /**
     * Opens a directory chooser to choose the download-location for the excel files
     * @param event
     * @throws IOException
     */
    @FXML
    private void openDirectoryChooser(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Download Location");

        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            Path downloadPath = selectedDirectory.toPath();
            Object source = event.getSource();
            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
            if (source == downloadLZ_button) {
                xlsxWriter.writeStudentPlan(downloadPath, LoadingController.boomData.getStudents());
            } else if (source == downloadAL_button) {
                xlsxWriter.writePresentationAttendance(downloadPath, LoadingController.boomData.getAllPlannedPresentations());
            } else if (source == downloadRZL_button) {
                xlsxWriter.writeRoomTimetable(downloadPath, LoadingController.boomData.getAllOfferedPresentations());
            }
        } else {
            System.out.println("Location selection canceled.");
        }
    }
}
