package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.Files;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImage;


    private final File userFile = new File("data/users.json");

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/images/fondo_register.jpg").toExternalForm());
        backgroundImage.setImage(image);
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());

        try {
            if (!userFile.exists()) {
                userFile.getParentFile().mkdirs();
                userFile.createNewFile();
                Files.writeString(userFile.toPath(), "[]"); // JSON array vacío
            }
        } catch (IOException e) {
            errorLabel.setText("Error setting up user file.");
            e.printStackTrace();
        }
    }

    @FXML
    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        try {
            String content = Files.readString(userFile.toPath());
            JSONArray users = new JSONArray(content);

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("name").equalsIgnoreCase(username)) {
                    errorLabel.setText("Username already exists.");
                    return;
                }
            }

            JSONObject newUser = new JSONObject();
            newUser.put("name", username);
            newUser.put("password", password); // ⚠️ sin encriptar por ahora

            users.put(newUser);
            Files.writeString(userFile.toPath(), users.toString(4));

            errorLabel.setText("User registered successfully!");

        } catch (IOException e) {
            errorLabel.setText("Error saving user.");
            e.printStackTrace();
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
