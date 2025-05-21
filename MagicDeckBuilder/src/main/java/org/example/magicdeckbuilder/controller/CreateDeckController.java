package org.example.magicdeckbuilder.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.concurrent.Task;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.magicdeckbuilder.model.Card;
import org.example.magicdeckbuilder.model.CardType;

public class CreateDeckController implements Initializable {

    @FXML private ImageView backgroundImage;
    @FXML private StackPane rootPane;
    @FXML private TilePane cardContainer;
    @FXML private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Fondo
        Image bg = new Image(
                getClass().getResource("/images/createDeck_background.jpg").toExternalForm());
        backgroundImage.setImage(bg);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        // Inicia carga asíncrona de cartas
        loadCardsAsync();
    }

    private void loadCardsAsync() {
        loadingIndicator.setVisible(true);

        Task<List<Card>> task = new Task<>() {
            @Override
            protected List<Card> call() throws Exception {
                try (InputStream is = getClass().getResourceAsStream("/data/cards.txt");
                     BufferedReader rd = new BufferedReader(
                             new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    if (is == null) throw new IllegalStateException("cards.txt not found");
                    return rd.lines()
                            .map(CreateDeckController.this::parseLine)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }
        };

        task.setOnSucceeded(evt -> distributeCards(task.getValue()));
        task.setOnFailed(evt -> {
            loadingIndicator.setVisible(false);
            task.getException().printStackTrace();
        });

        Thread th = new Thread(task, "load-cards-thread");
        th.setDaemon(true);
        th.start();
    }

    private void distributeCards(List<Card> cards) {
        Timeline tl = new Timeline();
        int delay = 0;
        for (Card card : cards) {
            KeyFrame kf = new KeyFrame(
                    Duration.millis(delay),
                    e -> cardContainer.getChildren().add(createCardView(card))
            );
            tl.getKeyFrames().add(kf);
            delay += 20;
        }
        tl.setOnFinished(e -> loadingIndicator.setVisible(false));
        tl.play();
    }

    private Card parseLine(String line) {
        String[] tk = line.split("\\|", 9);
        if (tk.length < 9) return null;
        try {
            String name  = tk[0];
            int    mana  = Integer.parseInt(tk[1]);
            CardType type = CardType.fromString(tk[2]);
            List<String> subs = tk[3].isBlank()
                    ? List.of()
                    : Arrays.asList(tk[3].split(","));
            return new Card(name, mana, type, subs,
                    tk[4], tk[5], tk[6], tk[7], tk[8]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private ImageView createCardView(Card card) {
        String url = card.getImgUrl();
        Image img = (url == null || url.isBlank())
                ? new Image(
                getClass().getResource("/images/placeholder.png").toExternalForm(),
                120, 0, true, true)
                : new Image(url, 120, 0, true, true);

        ImageView iv = new ImageView(img);
        String info = String.format(
                "Nombre: %s%nCoste: %d%nTipo: %s%nSubtipos: %s%nSet: %s%nRareza: %s%nArtista: %s%nFlavor: %s",
                card.getName(), card.getManaCost(),
                card.getCardType(), String.join(", ", card.getSubtypes()),
                card.getSet(), card.getRarity(), card.getArtist(), card.getFlavorText()
        );
        Tooltip.install(iv, new Tooltip(info));
        iv.setOnMouseEntered(e -> iv.setOpacity(0.8));
        iv.setOnMouseExited(e -> iv.setOpacity(1.0));
        return iv;
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
