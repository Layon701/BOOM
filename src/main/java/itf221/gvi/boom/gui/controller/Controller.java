package itf221.gvi.boom.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    private Button myButton;

    @FXML
    private void onButtonClick() {
        myButton.setText("Geklickt!");
    }
}
