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
        errorLabel.setText("");

        boolean fieldsFilled = !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty();
        boolean passwordsMatch = password.equals(confirmPassword);
        boolean userExists = false;
        boolean userSaved = false;

        // Verificar que todos los campos estén completos
        if (!fieldsFilled) {
            errorLabel.setText("All fields are required");
        }

        // Verificar que las contraseñas coincidan
        if (fieldsFilled && !passwordsMatch) {
            errorLabel.setText("Passwords do not match");
        }

        // Intentar guardar el usuario si todo es válido
        if (fieldsFilled && passwordsMatch) {
            File file = new File("data/usuarios.txt");
            file.getParentFile().mkdirs();

            try {
                if (file.exists()) {
                    List<String> lines = Files.readAllLines(file.toPath());

                    for (String line : lines) {
                        String[] parts = line.split(":");
                        if (parts.length > 0 && parts[0].equals(username)) {
                            userExists = true;
                        }
                    }
                }

                if (!userExists) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                        writer.write(username + ":" + password);
                        writer.newLine();
                        userSaved = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorLabel.setText("Error saving user");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Error reading file");
            }

            if (userExists) {
                errorLabel.setText("User already exists");
            }

            if (userSaved) {
                errorLabel.setText("User registered successfully!");
                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
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

