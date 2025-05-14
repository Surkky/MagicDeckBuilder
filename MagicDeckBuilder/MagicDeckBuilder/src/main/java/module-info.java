module org.example.magicdeckbuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;


    opens org.example.magicdeckbuilder to javafx.fxml;
    exports org.example.magicdeckbuilder;
    exports org.example.magicdeckbuilder.controller;
    opens org.example.magicdeckbuilder.controller to javafx.fxml;
    opens org.example.magicdeckbuilder.model to javafx.fxml;

}