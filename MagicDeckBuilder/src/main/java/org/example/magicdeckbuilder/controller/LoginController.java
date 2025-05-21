package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class LoginController {
    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button closeButton;
    @FXML private Button registerButton;
    @FXML private ImageView backgroundImage;
    @FXML private StackPane rootPane;
    @FXML private VBox loginBox;
    @FXML private Label errorLabel;

    // Ruta externa donde se encuentra el fichero de usuarios
    private static final Path USERS_FILE = Paths.get("data", "usuarios.txt");

    public void initialize() {
        // Fondo
        Image img = new Image(getClass().getResource("/images/fondo.jpg")
                .toExternalForm());
        backgroundImage.setImage(img);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        // Ajuste del loginBox
        loginBox.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.3));
        loginBox.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.4));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setSpacing(15);
    }

    @FXML
    private void login() {
        String user = userField.getText().trim();
        String pass = passwordField.getText().trim();
        errorLabel.setText("");

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Both fields are required");
            return;
        }
        if (Files.notExists(USERS_FILE)) {
            errorLabel.setText("User database not found");
            return;
        }

        boolean ok = false;
        try (BufferedReader reader = Files.newBufferedReader(USERS_FILE, StandardCharsets.UTF_8)) {
            List<String> lines = reader.lines().toList();
            for (String line : lines) {
                String[] p = line.split(":");
                if (p.length == 2 && p[0].equals(user) && p[1].equals(pass)) {
                    ok = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error reading user data");
            return;
        }

        if (ok) {
            loadNextScene();
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }

    private void loadNextScene() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load main screen");
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/magicdeckbuilder/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/login.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load register screen");
        }
    }

    @FXML
    private void close() {
        System.exit(0);
    }
}