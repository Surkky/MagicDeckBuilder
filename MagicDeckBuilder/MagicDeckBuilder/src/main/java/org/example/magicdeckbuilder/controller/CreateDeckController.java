package org.example.magicdeckbuilder.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;


public class CreateDeckController implements Initializable {

    @FXML private ImageView backgroundImage;
    @FXML private StackPane rootPane;
    @FXML private VBox cardContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image background = new Image(getClass().getResource("/images/createDeck_background.jpg").toExternalForm());
        backgroundImage.setImage(background);

        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
    }
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1152, 768);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}