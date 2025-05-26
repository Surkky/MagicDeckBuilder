package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;

    // Ruta externa donde guardamos los usuarios
    private static final Path EXTERNAL_USERS = Paths.get("data", "usuarios.txt");

    @FXML
    public void initialize() {
        // Cargo el fondo
        Image img = new Image(getClass().getResource("/images/fondo_register.jpg").toExternalForm());
        backgroundImage.setImage(img);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        // Aseguro que exista el fichero externo (copio plantilla o creo vacío)
        try {
            if (Files.notExists(EXTERNAL_USERS)) {
                Files.createDirectories(EXTERNAL_USERS.getParent());
                try (InputStream is = getClass().getResourceAsStream("/data/usuarios.txt")) {
                    if (is != null) {
                        Files.copy(is, EXTERNAL_USERS);
                    } else {
                        Files.createFile(EXTERNAL_USERS);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error initializing user database");
        }
    }

    @FXML
    private void registerUser() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        String conf = confirmPasswordField.getText().trim();
        errorLabel.setText("");

        boolean valid = true;

        if (user.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
            errorLabel.setText("All fields are required");
            valid = false;
        } else if (!pass.equals(conf)) {
            errorLabel.setText("Passwords do not match");
            valid = false;
        }

        boolean exists = false;
        if (valid) {
            try {
                List<String> lines = Files.readAllLines(EXTERNAL_USERS, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] p = line.split(":");
                    if (p.length > 0 && p[0].equals(user)) {
                        exists = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Error reading user database");
                valid = false;
            }
        }

        if (valid && exists) {
            errorLabel.setText("User already exists");
            valid = false;
        }

        if (valid) {
            try (BufferedWriter writer = Files.newBufferedWriter(EXTERNAL_USERS,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND)) {
                writer.write(user + ":" + pass);
                writer.newLine();
                errorLabel.setText("User registered successfully!");
                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Error saving user");
            }
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768);
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not return to login.");
        }
    }
}
