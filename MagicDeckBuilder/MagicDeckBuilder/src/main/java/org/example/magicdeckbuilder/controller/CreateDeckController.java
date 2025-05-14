package org.example.magicdeckbuilder.controller;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
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