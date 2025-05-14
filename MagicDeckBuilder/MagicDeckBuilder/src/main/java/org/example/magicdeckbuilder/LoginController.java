package org.example.magicdeckbuilder;

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
    private void login(){
        String username = userField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Both fields are required");
            return;
        }

        // Cambiar mas adelante
        if (username.equals("admin") && password.equals("1234")) {
            errorLabel.setText("");
            loadNextScene(); // Ir a la siguiente pantalla
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }
    private void loadNextScene() { //Cargar la siguiente pantalla
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/magicdeckbuilder/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load main screen");
        }

    }

    @FXML
    private void close(){
        System.exit(0);
    }


}