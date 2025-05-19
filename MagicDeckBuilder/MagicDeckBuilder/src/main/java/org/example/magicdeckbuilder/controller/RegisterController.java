package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.*;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;



public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/images/fondo_register.jpg").toExternalForm());
        backgroundImage.setImage(image);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
    }

    @FXML
    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        boolean datosValidos = true;
        errorLabel.setText("");

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("All fields are required");
            datosValidos = false;
        } else if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            datosValidos = false;
        }

        if (datosValidos) {
            File file = new File("data/usuarios.txt");
            file.getParentFile().mkdirs();

            boolean usuarioExiste = false;

            try {
                if (file.exists()) {
                    List<String> lines = Files.readAllLines(file.toPath());
                    for (String line : lines) {
                        String[] parts = line.split(":");
                        if (parts.length > 0 && parts[0].equals(username)) {
                            errorLabel.setText("User already exists");
                            usuarioExiste = true;
                            break;
                        }
                    }
                }

                if (!usuarioExiste) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                        writer.write(username + ":" + password);
                        writer.newLine();
                    }

                    errorLabel.setText("User registered successfully!");
                    usernameField.clear();
                    passwordField.clear();
                    confirmPasswordField.clear();
                }

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
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            errorLabel.setText("Could not return to login.");
            e.printStackTrace();
        }
    }
}

