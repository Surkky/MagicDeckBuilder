package org.example.magicdeckbuilder.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;

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
    private void login(){
        String username = userField.getText();
        String password = passwordField.getText();
    }

    @FXML
    private void close(){
        System.exit(0);
    }
}