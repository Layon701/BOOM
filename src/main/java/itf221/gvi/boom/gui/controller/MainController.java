package itf221.gvi.boom.gui.controller;

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
import java.util.Objects;

public class MainController {
    @FXML
    Button sw_browse_button;

    @FXML
    Button ul_browse_button;

    @FXML
    Button rp_browse_button;

    @FXML
    Button import_button;

    @FXML
    TextArea sw_import_field;

    @FXML
    TextArea ul_import_field;

    @FXML
    TextArea rp_import_field;

    @FXML
    private void initImport(ActionEvent event) throws IOException {
        switchToLoadingView(event);
    }

    private void switchToLoadingView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itf221/gvi/boom/fxml/loading-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
        if(!Objects.equals(sw_import_field.getText(), "") && !Objects.equals(ul_import_field.getText(), "") && !Objects.equals(rp_import_field.getText(), "")) {
            import_button.setDisable(false);
        } else {
            import_button.setDisable(true);
        }
    }
}