package org.example.magicdeckbuilder.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;


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
}