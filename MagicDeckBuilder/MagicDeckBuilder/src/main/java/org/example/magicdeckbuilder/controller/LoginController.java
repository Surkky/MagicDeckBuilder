package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.io.File;
import java.nio.file.Files;



public class LoginController {
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button registerButton;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private StackPane rootPane;
    @FXML
    private VBox loginBox; //centrar el login
    @FXML
    private Label errorLabel;


    public void initialize() {
        // Cargar imagen de fondo desde resources/images/fondo.jpg
        Image image = new Image(getClass().getResource("/images/fondo.jpg").toExternalForm());
        backgroundImage.setImage(image);
        // Ajusta el tamaño de la imagen del fondo al tamaño de la ventana(rootPane)
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        //Ajusta el tamaño del login al tamaño de la ventana(rootPane)
        loginBox.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.3)); // 30% del ancho
        loginBox.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.4)); // 40% del alto
        // Centrar VBox desde código
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setSpacing(15);
    }

    @FXML
    private void login() {
        String username = userField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Both fields are required");
        }

        File file = new File("data/usuarios.txt");

        if (!file.exists()) {
            errorLabel.setText("User database not found");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    errorLabel.setText("");
                    loadNextScene(); //acceso permitido
                    return;
                }
            }
            errorLabel.setText("Invalid username or password");
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error reading user data");
        }
    }

    private void loadNextScene() { //carga siguiente escena
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768); //tamaño fijo de todas las ventanas
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root, 1152, 768); // Tamaño fijo
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load register screen");
        }
    }

    @FXML
    private void close(){
        System.exit(0);
    }
}