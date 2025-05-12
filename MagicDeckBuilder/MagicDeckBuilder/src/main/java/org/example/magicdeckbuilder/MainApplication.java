package org.example.magicdeckbuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
<<<<<<< HEAD:MagicDeckBuilder/MagicDeckBuilder/src/main/java/org/example/magicdeckbuilder/MainApplication.java
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login - Magic Deck Builder");
=======
        Scene scene = new Scene(fxmlLoader.load(), 410, 320);
        stage.setTitle("Magic Deck Builder");
>>>>>>> 048320f60d3438d6cab45541d18a4b1f975e1772:MagicDeckBuilder/src/main/java/org/example/magicdeckbuilder/MainApplication.java
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}