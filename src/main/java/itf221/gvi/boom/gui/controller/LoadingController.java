package itf221.gvi.boom.gui.controller;

import itf221.gvi.boom.RoomManagementUnit;
import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.io.IOManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Controller class for managing the loading view of the application.
 */
public class LoadingController {

    @FXML
    Button export_view_button;

    @FXML
    ProgressIndicator progressIndicator;

    private Path swPath;
    private Path ulPath;
    private Path rpPath;
    private Path downloadPath;

    /**
     * Sets the paths required for the distribution algorithm and starts the task.
     * @param swPath The path to the SW file.
     * @param ulPath The path to the UL file.
     * @param rpPath The path to the RP file.
     * @param downloadPath The path to the download folder.
     */
    public void setPaths(Path swPath, Path ulPath, Path rpPath, Path downloadPath) {
        this.swPath = swPath;
        this.ulPath = ulPath;
        this.rpPath = rpPath;
        this.downloadPath = downloadPath;
        runDistributionTask();
    }

    /**
     * Initializes the 'loading' process by switching to the export view.
     * @param event  The ActionEvent triggered by the user action.
     * @throws IOException If there is an error while loading the view.
     */
    @FXML
    private void initLoading(ActionEvent event) throws IOException {
        // TODO: No real logic for now, just a button to see the next view
        switchToExportView(event);
    }

    /**
     * Runs the distribution algorithm in a background thread and updates the progress indicator.
     */
    private void runDistributionTask() {
        // Background thread in JavaFX that allows running long-running operations without freezing the UI
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 100);

                IOManager ioManager = new IOManager(swPath, ulPath, rpPath, downloadPath);
                updateProgress(10, 100);

                BoomData boomData = ioManager.readFiles();
                updateProgress(50, 100);

                RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
                roomManagementUnit.execute(boomData);
                updateProgress(100, 100);

                return null;
            }
        };
        // Bind the progress indicator to the task's progress
        progressIndicator.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            // TODO: Switch to the export view after the task completes with switchToExportView(...);
        });
        // Start the task in a new thread
        new Thread(task).start();
    }

    /**
     * Switches to the export view. It loads the export-view FXML file and sets it as the scene for the current stage.
     * @param event The ActionEvent triggered by the user action.
     * @throws IOException If there is an error while loading the FXML file.
     */
    private void switchToExportView(ActionEvent event) throws IOException {
        // TODO: No real logic for now, just a button to see the next view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itf221/gvi/boom/fxml/export-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        stage.setScene(new Scene(root, stageWidth, stageHeight));
        stage.show();
    }
}
