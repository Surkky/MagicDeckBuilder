package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

import org.example.magicdeckbuilder.model.User;
import org.example.magicdeckbuilder.model.SessionManager;

public class LoginController {
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button closeButton;
    @FXML private ImageView backgroundImage;
    @FXML private StackPane rootPane;
    @FXML private VBox loginBox;
    @FXML private Label errorLabel;

    public void initialize() {

        Image img = new Image(getClass().getResource("/images/fondo.jpg").toExternalForm());
        backgroundImage.setImage(img);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        loginBox.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.3));
        loginBox.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.4));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setSpacing(15);
    }

    @FXML
    private void login() {
        String username = userField.getText().trim();
        String password = passwordField.getText().trim();
        errorLabel.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Both fields are required");
            return;
        }

        InputStream is = getClass().getResourceAsStream("/data/usuarios.txt");
        if (is == null) {
            errorLabel.setText("User database not found");
            return;
        }

        boolean loginSuccess = false;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines().toList();
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2
                        && parts[0].equals(username)
                        && parts[1].equals(password)) {
                    loginSuccess = true;
                    break;
                }
            }

            if (loginSuccess) {
                // Creamos el User con listas mutables para decks
                User user = new User(
                        username,
                        password,
                        new ArrayList<>(),    // aquí la colección de cartas si la necesitas
                        new ArrayList<>()     // decks mutables
                );
                SessionManager.setCurrentUser(user);

                loadNextScene();
            } else {
                errorLabel.setText("Invalid username or password");
            }

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error reading user data");
        }
    }

    private void loadNextScene() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();

            MainController ctrl = loader.getController();
            ctrl.setUser(SessionManager.getCurrentUser());

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load create-deck screen");
        }
    }

    @FXML
    private void goToRegister() { /* ... */ }

    @FXML
    private void close() {
        System.exit(0);
    }
}