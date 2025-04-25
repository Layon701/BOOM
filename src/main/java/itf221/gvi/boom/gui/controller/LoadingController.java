package itf221.gvi.boom.gui.controller;

import itf221.gvi.boom.RoomManagementUnit;
import itf221.gvi.boom.data.BoomData;
import itf221.gvi.boom.io.IOManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Controller class for managing the loading view of the application.
 */
public class LoadingController {

    @FXML
    AnchorPane rootPane;

    static int completionScore;

    static BoomData boomData;

    private Path swPath;
    private Path ulPath;
    private Path rpPath;
    private Path downloadPath;

    /**
     * Sets the paths required for the distribution algorithm and starts the task.
     * @param swPath       The path to the SW file.
     * @param ulPath       The path to the UL file.
     * @param rpPath       The path to the RP file.
     * @param downloadPath The path to the download folder.
     */
    public void setPaths(Path swPath, Path ulPath, Path rpPath, Path downloadPath) {
        this.swPath = swPath;
        this.ulPath = ulPath;
        this.rpPath = rpPath;
        this.downloadPath = downloadPath;
    }

    /**
     * On initializing runs algorithm.
     * Changes view to Export view, if algorithm succeeds.
     * Changes view back to Main view, if algorithm fails and shows error toast.
     */
    @FXML
    public void initialize() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                runDistributionAlg();
                return null;
            }
        };

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            System.err.println("Algorithm failed: " + ex.getMessage());
            switchToMainView();
            MainController.shouldFailedToastBeShown = true;
        });

        task.setOnSucceeded(e -> {
            try {
                switchToExportView();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    /**
     * Executes the distribution algorithm and sets the completion score.
     */
    @FXML
    private void runDistributionAlg() {
        IOManager ioManager = new IOManager(
                swPath,
                ulPath,
                rpPath,
                downloadPath);
        boomData = ioManager.readFiles();
        RoomManagementUnit roomManagementUnit = new RoomManagementUnit();
        completionScore = roomManagementUnit.execute(boomData);
    }

    /**
     * Switches to the main view.
     * Loads the main-view FXML file and sets it as the scene for the current stage.
     */
    private void switchToMainView() {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itf221/gvi/boom/fxml/main-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Switches to the export view.
     * Loads the export-view FXML file and sets it as the scene for the current stage.
     * @throws IOException If there is an error while loading the FXML file.
     */
    private void switchToExportView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itf221/gvi/boom/fxml/export-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) rootPane.getScene().getWindow();

        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);

        stage.setScene(new Scene(root, stageWidth, stageHeight));
        stage.show();
    }
}
