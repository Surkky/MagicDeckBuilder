package org.example.magicdeckbuilder.model;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Button myDecksButton;
    @FXML
    private Button createDeckButton;
    @FXML
    private Button logOutButton;

    @FXML
    public void initialize() {
        // Cargar imagen de fondo
        Image image = new Image(getClass().getResource("/images/main_background.jpg").toExternalForm());
        backgroundImage.setImage(image);

        // Ajustar tamaño del fondo a la ventana
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        // Efecto de texto "SELECT" al pasar el ratón
        myDecksButton.setText("My Decks");
        createDeckButton.setText("Create New Deck");
        logOutButton.setText("Log Out");

        myDecksButton.setOnMouseEntered(e -> myDecksButton.setText("SELECT"));
        myDecksButton.setOnMouseExited(e -> myDecksButton.setText("My Decks"));

        createDeckButton.setOnMouseEntered(e -> createDeckButton.setText("SELECT"));
        createDeckButton.setOnMouseExited(e -> createDeckButton.setText("Create New Deck"));

        logOutButton.setOnMouseEntered(e -> logOutButton.setText("SELECT"));
        logOutButton.setOnMouseExited(e -> logOutButton.setText("Log Out"));
    }
}
