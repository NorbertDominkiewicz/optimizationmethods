package com.ndominkiewicz.optui.controllers;

import com.ndominkiewicz.optui.Launcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    Button closeButton;
    @FXML
    TabPane mainPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeButton.setOnAction(event -> Launcher.close());
    }
}
