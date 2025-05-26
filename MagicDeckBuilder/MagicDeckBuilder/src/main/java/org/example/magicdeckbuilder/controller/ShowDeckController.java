package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class ShowDeckController {

    @FXML private ComboBox<String> deckComboBox;
    @FXML private TilePane cardContainer;
    @FXML private Button backButton;
    @FXML private Button selectButton;
    @FXML private ImageView backgroundImage;
    @FXML private Button deleteButton;
    @FXML private Button editButton;



    private String currentUsername;

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        loadUserDecks();

        selectButton.setOnAction(event -> showDeck(deckComboBox.getValue()));
        backButton.setOnAction(event -> goBack(event));
        deleteButton.setOnAction(event -> deleteDeck(deckComboBox.getValue()));
        editButton.setOnAction(event -> editDeck(deckComboBox.getValue()));


        System.out.println("Usuario actual en ShowDeckController: " + currentUsername);
    }

    @FXML
    private void initialize() {
        Image bg = new Image(getClass().getResource("/images/createDeck_background.jpg").toExternalForm());
        backgroundImage.setImage(bg);
    }

    private void loadUserDecks() {

        File folder = Paths.get("data", "users", currentUsername, "decks").toFile();
        if (!folder.exists()) folder.mkdir();

        File[] files = folder.listFiles((dir, name) ->
                name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                String deckName = file.getName().replace(".txt", "");
                deckComboBox.getItems().add(deckName);
            }
        }
        System.out.println("Buscando mazos en: " + folder.getAbsolutePath());
        System.out.println("Archivos encontrados:");
        for (File file : files) {
            System.out.println(" -> " + file.getName());
        }
    }



    private void showDeck(String deckName) {
        cardContainer.getChildren().clear();

        File file = Paths.get("data", "users", currentUsername, "decks", deckName + ".txt").toFile();
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                javafx.scene.control.Label cardLabel = new javafx.scene.control.Label(line);
                cardLabel.setStyle("-fx-background-color: lightgray; -fx-padding: 5px; -fx-border-radius: 5;");
                cardContainer.getChildren().add(cardLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goBack(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1152, 768);

            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void deleteDeck(String deckName) {
        boolean validName = deckName != null;

        if (validName) {
            File file = Paths.get("data", "users", currentUsername, "decks", deckName + ".txt").toFile();
            boolean deleted = file.exists() && file.delete();

            if (deleted) {
                deckComboBox.getItems().remove(deckName);
                deckComboBox.getSelectionModel().clearSelection();
                cardContainer.getChildren().clear();
                System.out.println("Mazo eliminado: " + deckName);
            } else {
                System.out.println("No se pudo eliminar el mazo: " + deckName);
            }
        }
    }

    private void editDeck(String deckName) {
        if (deckName != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/create_deck.fxml"));
                Parent root = loader.load();

                CreateDeckController controller = loader.getController();
                controller.setEditingDeck(currentUsername, deckName); // carga el mazo a editar

                Scene scene = new Scene(root, 1152, 768);
                scene.getStylesheets().add(getClass().getResource("/styles/create_deck.css").toExternalForm());

                Stage stage = (Stage) editButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
