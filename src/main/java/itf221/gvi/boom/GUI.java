package itf221.gvi.boom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main JavaFX application class for launching the BOOM user interface.
 */
public class GUI extends Application {
    /**
     * Starts the JavaFX application by loading the main view and setting up the primary stage.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading of the FXML or resources
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itf221/gvi/boom/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("BOOM");
        Image icon = new Image(String.valueOf(getClass().getResource("/itf221/gvi/boom/images/boom.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
