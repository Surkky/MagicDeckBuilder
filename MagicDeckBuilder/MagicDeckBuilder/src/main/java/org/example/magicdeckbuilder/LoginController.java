package org.example.magicdeckbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;




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
    private StackPane rootPane; //para que la imagen de fondo se
    @FXML
    private VBox loginBox; //centrar el login

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
    }

    @FXML
    private void close(){
        System.exit(0);
    }


}