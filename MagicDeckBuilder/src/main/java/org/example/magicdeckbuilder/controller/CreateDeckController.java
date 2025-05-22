package org.example.magicdeckbuilder.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.util.*;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.concurrent.Task;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.magicdeckbuilder.model.*;

public class CreateDeckController implements Initializable {

    @FXML private ImageView backgroundImage;
    @FXML private StackPane rootPane;
    @FXML private TilePane cardContainer;
    @FXML private ProgressIndicator loadingIndicator;
    @FXML private ComboBox<CardType> typeFilter;
    @FXML private ComboBox<Integer> manaFilter;
    @FXML private ComboBox<String> rarityFilter;
    @FXML private Button applyButton;
    @FXML private Button clearButton;
    @FXML private TextField deckNameField;
    @FXML private Button saveButton;

    private User currentUser;
    private List<Card> allCards = new ArrayList<>();
    private Map<Card, Integer> selected = new LinkedHashMap<>();

    /** Inyecta el User desde el LoginController */
    public void setUser(User u) {
        this.currentUser = u;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Si no se inyectó vía setUser(…), lo recuperamos de la sesión
        if (this.currentUser == null) {
            this.currentUser = SessionManager.getCurrentUser();
        }
        // fondo
        Image bg = new Image(
                getClass().getResource("/images/createDeck_background.jpg").toExternalForm());
        backgroundImage.setImage(bg);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        applyButton.setOnAction(e -> onApplyFilters());
        clearButton.setOnAction(e -> onClearFilters());
        saveButton.setOnAction(e -> onSaveDeck());

        loadCardsAsync();
    }

    private void loadCardsAsync() {
        loadingIndicator.setVisible(true);
        Task<List<Card>> task = new Task<>() {
            @Override
            protected List<Card> call() throws Exception {
                try (InputStream is = getClass().getResourceAsStream("/data/cards.txt");
                     BufferedReader rd = new BufferedReader(
                             new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))) {
                    return rd.lines()
                            .map(CreateDeckController.this::parseLine)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }
        };
        task.setOnSucceeded(evt -> {
            allCards = task.getValue();
            initFilters();
            displayCards(allCards);
        });
        task.setOnFailed(evt -> {
            loadingIndicator.setVisible(false);
            task.getException().printStackTrace();
        });
        new Thread(task, "load-cards-thread").start();
    }

    private void initFilters() {
        typeFilter.getItems().setAll(CardType.values());
        manaFilter.getItems().setAll(
                allCards.stream().map(Card::getManaCost).distinct().sorted().toList()
        );
        rarityFilter.getItems().setAll(
                allCards.stream().map(Card::getRarity).distinct().sorted().toList()
        );
    }

    private void onApplyFilters() {
        CardType t = typeFilter.getValue();
        Integer m = manaFilter.getValue();
        String r = rarityFilter.getValue();
        var filtered = allCards.stream()
                .filter(c -> t == null || c.getCardType() == t)
                .filter(c -> m == null || c.getManaCost() == m)
                .filter(c -> r == null || r.equals(c.getRarity()))
                .toList();
        displayCards(filtered);
    }

    private void onClearFilters() {
        typeFilter.getSelectionModel().clearSelection();
        manaFilter.getSelectionModel().clearSelection();
        rarityFilter.getSelectionModel().clearSelection();
        displayCards(allCards);
    }

    private void displayCards(List<Card> cards) {
        cardContainer.getChildren().clear();
        cards.forEach(c -> cardContainer.getChildren().add(createCardPane(c)));
        loadingIndicator.setVisible(false);
    }

    private AnchorPane createCardPane(Card card) {
        ImageView iv = createCardView(card);
        Label count = new Label();
        count.setStyle("-fx-text-fill:white;-fx-font-weight:bold;-fx-background-color:rgba(0,0,0,0.6);");
        count.setVisible(false);
        AnchorPane.setBottomAnchor(count, 4.0);
        AnchorPane.setRightAnchor(count, 4.0);

        AnchorPane pane = new AnchorPane(iv, count);
        iv.setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.PRIMARY)
                selected.put(card, selected.getOrDefault(card, 0) + 1);
            else if (evt.getButton() == MouseButton.SECONDARY)
                selected.computeIfPresent(card, (k,v) -> v>1 ? v-1 : null);

            int q = selected.getOrDefault(card, 0);
            if (q>0) {
                count.setText(String.valueOf(q));
                count.setVisible(true);
                iv.setStyle("-fx-effect:dropshadow(gaussian,yellow,10,0.5,0,0);");
            } else {
                count.setVisible(false);
                iv.setStyle(null);
            }
        });
        return pane;
    }

    @FXML
    private void onSaveDeck() {
        String name = deckNameField.getText().trim();
        if (name.isEmpty()) {
            deckNameField.setPromptText("Enter deck name");
            return;
        }
        var instances = selected.entrySet().stream()
                .map(e -> new CardInstance(e.getKey(), e.getValue()))
                .toList();
        Deck deck = new Deck(name, instances, "Custom");
        // añadir al usuario en memoria
        currentUser.getDecks().add(deck);

        try {
            String user = currentUser.getName();
            Path dir = Paths.get("data", "users", user, "decks");
            Files.createDirectories(dir);
            Path file = dir.resolve(name + ".txt");
            try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                for (CardInstance ci : instances) {
                    Card c = ci.getCard();
                    w.write(String.join("|",
                            c.getName(),
                            String.valueOf(c.getManaCost()),
                            c.getCardType().name(),
                            String.join(",", c.getSubtypes()),
                            c.getFlavorText(),
                            c.getSet(),
                            c.getRarity(),
                            c.getArtist(),
                            c.getImgUrl(),
                            String.valueOf(ci.getQuantity())
                    ));
                    w.newLine();
                }
            }
            saveButton.setText("Saved");
        } catch (IOException e) {
            e.printStackTrace();
            saveButton.setText("Error");
        }
    }

    private Card parseLine(String line) {
        String[] tk = line.split("\\|", 9);
        if (tk.length < 9) return null;
        try {
            String name = tk[0];
            int mana = Integer.parseInt(tk[1]);
            CardType type = CardType.fromString(tk[2]);
            List<String> subs = tk[3].isBlank()
                    ? Collections.emptyList()
                    : Arrays.asList(tk[3].split(","));
            return new Card(name, mana, type, subs, tk[4], tk[5], tk[6], tk[7], tk[8]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private ImageView createCardView(Card card) {
        String url = card.getImgUrl();
        Image img = (url==null||url.isBlank())
                ? new Image(getClass().getResource("/images/placeholder.png").toExternalForm(), 120,0,true,true)
                : new Image(url,120,0,true,true);
        ImageView iv = new ImageView(img);
        Tooltip.install(iv, new Tooltip(
                card.getName() + " (" + card.getManaCost() + ") " + card.getRarity()
        ));
        iv.setOnMouseEntered(e->iv.setOpacity(0.7));
        iv.setOnMouseExited(e->iv.setOpacity(1.0));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}