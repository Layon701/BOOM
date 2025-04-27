package itf221.gvi.boom.gui.controller;

import itf221.gvi.boom.io.writer.XlsxWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Controller class for managing the export view of the application.
 */
public class ExportController {

    @FXML
    private AnchorPane toastPane;

    @FXML
    private Label completionScoreToast;

    XlsxWriter xlsxWriter = new XlsxWriter();
    Path downloadPath = MainController.getDownloadFolderPath();

    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        showSuccessToast();
        completionScoreToast.setText("✔ Erfolg! Erfüllungsscore: " + LoadingController.completionScore + "%");
    }

    /**
     * Method to download student plan ("Laufzettel").
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void downloadLZ() throws IOException {
        xlsxWriter.writeStudentPlan(downloadPath, LoadingController.boomData.getStudents());
    }

    /**
     * Method to download attendance list ("Anwesenheitsliste").
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void downloadAL() throws IOException {
        xlsxWriter.writePresentationAttendance(downloadPath, LoadingController.boomData.getAllPlannedPresentations());
    }

    /**
     * Method to download room timetable plan ("Raum-/Zeitpläne-Liste").
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void downloadRZL() throws IOException {
        xlsxWriter.writeRoomTimetable(downloadPath, LoadingController.boomData.getAllOfferedPresentations());
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
