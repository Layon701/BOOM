package itf221.gvi.boom.gui.controller;

import itf221.gvi.boom.RoomManagementUnit;
import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.io.IOManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Controller class for managing user interactions in the main view of the application.
 */
public class MainController {
    @FXML
    Button sw_browse_button, ul_browse_button, rp_browse_button, import_button;

    @FXML
    TextArea sw_import_field, ul_import_field, rp_import_field;

    /**
     * Initializes the 'start' process by switching to the loading view and executing the distribution algorithm.
     * @param event The ActionEvent triggered by the user action.
     * @throws IOException If there is an error while loading the view or executing the distribution algorithm.
     */
    @FXML
    private void initStart(ActionEvent event) throws IOException {
        switchToLoadingView(event);
        runDistributionAlg();
    }

    /**
     * Executes the distribution algorithm.
     */
    @FXML
    private void runDistributionAlg() {
        IOManager ioManager = new IOManager(
                Paths.get(sw_import_field.getText()),
                Paths.get(ul_import_field.getText()),
                Paths.get(rp_import_field.getText()),
                getDownloadFolderPath());
        BoomData boomData = ioManager.readFiles();
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
        roomManagementUnit.execute(boomData);
    }

    /**
     * Returns the output path to the user's default "Downloads" folder.
     * @return The Path to the "Downloads" folder of the current user.
     */
    public Path getDownloadFolderPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, "Downloads");
    }

    /**
     * Switches to the loading view. It loads the loading-view FXML file and sets it as the scene for the current stage.
     * @param event The ActionEvent triggered by the user action.
     * @throws IOException If there is an error while loading the FXML file.
     */
    private void switchToLoadingView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itf221/gvi/boom/fxml/loading-view.fxml"));
        Parent root = fxmlLoader.load();
        LoadingController loadingController = fxmlLoader.getController();
        loadingController.setPaths(
                Paths.get(sw_import_field.getText()),
                Paths.get(ul_import_field.getText()),
                Paths.get(rp_import_field.getText()),
                getDownloadFolderPath()
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        stage.show();
    }

    /**
     * Opens the user documentation PDF file.
     */
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

    /**
     * Opens a file chooser to select an Excel file.
     * Based on the button clicked, the path of the selected file is displayed in the corresponding text area.
     * Enables the 'start' button if all three file paths are selected.
     * @param event The ActionEvent triggered by the user action.
     */
    @FXML
    private void openFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            Object source = event.getSource();
            if (source == sw_browse_button) {
                sw_import_field.setText(selectedFile.getAbsolutePath());
            } else if (source == ul_browse_button) {
                ul_import_field.setText(selectedFile.getAbsolutePath());
            } else if (source == rp_browse_button) {
                rp_import_field.setText(selectedFile.getAbsolutePath());
            }
        } else {
            System.out.println("File selection canceled.");
        }
        // Enables or disables the import button based on whether all paths are selected
        if(!Objects.equals(sw_import_field.getText(), "") && !Objects.equals(ul_import_field.getText(), "") && !Objects.equals(rp_import_field.getText(), "")) {
            import_button.setDisable(false);
        } else {
            import_button.setDisable(true);
        }
    }
}