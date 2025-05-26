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
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import org.example.magicdeckbuilder.model.Card;
import org.example.magicdeckbuilder.model.CardType;

public class ShowDeckController {

    @FXML
    private ComboBox<String> deckComboBox;
    @FXML
    private TilePane cardContainer;
    @FXML
    private Button backButton;
    @FXML
    private Button selectButton;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

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
                String[] parts = line.split("\\|");
                if (parts.length >= 9) {

                    Card card = new Card(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            CardType.fromString(parts[2]),
                            Arrays.asList(parts[3].split(",")),
                            parts[4],
                            parts[5],
                            parts[6],
                            parts[7],
                            parts[8]
                    );

                    ImageView cardImage = createCardView(card);
                    cardContainer.getChildren().add(cardImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImageView createCardView(Card card) {
        Image image;
        try {
            image = new Image(card.getImgUrl(), 120, 0, true, true);
        } catch (Exception e) {
            image = new Image(getClass().getResource("/images/placeholder.png").toExternalForm(), 120, 0, true, true);
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        Tooltip tooltip = new Tooltip();
        tooltip.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        String tooltipText = String.format(
                "╔══════════════════════════════╗\n" +
                        "  Name: %s\n" +
                        "  Mana Cost: %d\n" +
                        "  Type: %s\n" +
                        "  Subtypes: %s\n" +
                        "  Flavor Text: %s\n" +
                        "  Set: %s\n" +
                        "  Rarity: %s\n" +
                        "  Artist: %s\n" +
                        "╚══════════════════════════════╝",
                card.getName(),
                card.getManaCost(),
                card.getCardType(),
                card.getSubtypes().isEmpty() ? "None" : String.join(", ", card.getSubtypes()),
                card.getFlavorText().isEmpty() ? "No flavor text" : "\"" + card.getFlavorText() + "\"",
                card.getSet(),
                card.getRarity(),
                card.getArtist()
        );

        tooltip.setText(tooltipText);
        Tooltip.install(imageView, tooltip);

        imageView.setOnMouseEntered(e -> {
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255,215,0,0.8), 20, 0.5, 0, 0);");
        });
        imageView.setOnMouseExited(e -> {
            imageView.setStyle(null);
        });

        return imageView;
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
                controller.setEditingDeck(currentUsername, deckName);

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