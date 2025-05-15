package org.example.magicdeckbuilder.controller;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.magicdeckbuilder.model.Card;
import org.example.magicdeckbuilder.model.CardBuilder;
import org.example.magicdeckbuilder.model.CardType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateDeckController implements Initializable {

    @FXML
    private GridPane cardGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Create deck initialized.");
        cardGrid.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        cardGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        cardGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        cargarCartas();
    }

    private void cargarCartas() {
        // Buscar cartas usando la API de Scryfall
        String apiUrl = "https://api.scryfall.com/cards/search?q=set:neo"; // ejemplo con set NEO.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> Platform.runLater(() -> mostrarCartas(json)))
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void mostrarCartas(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray data = obj.getJSONArray("data");

            System.out.println("Cantidad de cartas: " + data.length());

            int column = 0;
            int row = 0;

            for (int i = 0; i < data.length(); i++) {
                JSONObject card = data.getJSONObject(i);

                try {
                    String name = card.getString("name");
                    String manaCostString = card.optString("cmc", "0");
                    int manaCost = (int) Double.parseDouble(manaCostString); // Scryfall da cmc como número

                    String typeLine = card.optString("type_line", ""); // ej: "Legendary Creature — Elf Warrior"
                    String[] typeParts = typeLine.split("—");
                    String fullType = typeParts[0].trim(); // ej: "Legendary Creature"
                    List<String> subtypes = new ArrayList<>();
                    if (typeParts.length > 1) {
                        String[] subs = typeParts[1].trim().split(" ");
                        subtypes.addAll(Arrays.asList(subs));
                    }

                    CardType cardType = CardType.CREATURE; // default
                    for (CardType ct : CardType.values()) {
                        if (fullType.toUpperCase().contains(ct.name())) {
                            cardType = ct;
                            break;
                        }
                    }

                    String flavorText = card.optString("flavor_text", "");
                    String set = card.optString("set", "");
                    String rarity = card.optString("rarity", "");
                    String artist = card.optString("artist", "");
                    String imgUrl = card.optString("image_uris", "");

                    Card c = new CardBuilder()
                            .setName(name)
                            .setManaCost(manaCost)
                            .setCardType(cardType)
                            .setSubtypes(subtypes)
                            .setFlavorText(flavorText)
                            .setSet(set)
                            .setRarity(rarity)
                            .setArtist(artist)
                            .setImgUrl(imgUrl)
                            .build();

                    System.out.println("Creada: " + c);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (!card.has("image_uris")) continue;

                String imageUrl = card.getJSONObject("image_uris").getString("normal");
                System.out.println("Cargando: " + imageUrl);

                Image image = new Image(imageUrl, 150, 210, true, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(210);
                imageView.setPreserveRatio(true);

                VBox container = new VBox(imageView);
                container.setAlignment(Pos.CENTER);
                container.setPadding(new Insets(5));
                container.setPrefHeight(220); // 210 de la imagen + padding
                container.setMinHeight(220);

                cardGrid.add(container, column, row);
                column++;
                if (column == 5) {
                    column = 0;
                    row++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}