package org.example.magicdeckbuilder.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.magicdeckbuilder.model.User;
import org.example.magicdeckbuilder.model.SessionManager;



import java.io.IOException;

public class MainController {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

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

        Image image = new Image(getClass().getResource("/images/main_background.jpg").toExternalForm());
        backgroundImage.setImage(image);

        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

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

    @FXML
    private void onCreateDeckClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/create_deck.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1152, 768);
        scene.getStylesheets().add(getClass().getResource("/styles/create_deck.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }


    @FXML
    public void onLogOutClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/login.fxml"));
        Parent loginRoot = loader.load();

        Scene loginScene = new Scene(loginRoot, 1152, 768); // Tamaño fijo
        loginScene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }

    @FXML
    private void onMyDecksClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/show_deck.fxml"));
        Parent root = loader.load();

        ShowDeckController controller = loader.getController();
        controller.setCurrentUsername(SessionManager.getCurrentUser().getName());

        Scene scene = new Scene(root, 1152, 768);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
