module org.example.magicdeckbuilder {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.magicdeckbuilder to javafx.fxml;
    exports org.example.magicdeckbuilder;
}