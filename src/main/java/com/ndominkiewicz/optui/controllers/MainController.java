package com.ndominkiewicz.optui.controllers;

import com.ndominkiewicz.optui.Launcher;
import com.ndominkiewicz.optui.models.ViewController;
import com.ndominkiewicz.optui.models.Page;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    /*FXML*/
    @FXML
    Button closeButton;
    @FXML
    StackPane mainPane;
    @FXML
    Label title;
    @FXML
    private Button dwudzielna;
    @FXML
    private Button linearCarlo;
    @FXML
    private Button nonLinearCarlo;
    //
    BisectionController bisectionController;
    LinearCarloController linearCarloController;
    NonLinearCarloController nonLinearCarloController;
    List<ViewController> controllers;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllers = new ArrayList<>();
        loadUpActions();
        closeButton.setOnAction(event -> Launcher.close());
        switchView(Page.BISECTION);
    }
    private void loadUpActions() {
        dwudzielna.setOnAction(event -> switchView(Page.BISECTION));
        linearCarlo.setOnAction(event -> switchView(Page.LINEARCARLO));
        nonLinearCarlo.setOnAction(event -> switchView(Page.NONLINEARCARLO));
    }
    private void changeTitle(String value) {
        Platform.runLater(() -> title.setText(value));
    }
    public void switchView(Page view) {
        String name = "";
        mainPane.getChildren().clear();
        switch (view) {
            case BISECTION -> {
                if (bisectionController == null) {
                    name = "bisection";
                    changeTitle("Metoda Dwudzielna");
                    bisectionController = loadController(name);
                    controllers.add(bisectionController);
                } else {
                    mainPane.getChildren().add(bisectionController.getView());
                    changeTitle("Metoda Dwudzielna");
                }
            }
            case LINEARCARLO -> {
                if (linearCarloController == null) {
                    name = "linearcarlo";
                    changeTitle("Algorytm Monte Carlo Liniowo");
                    linearCarloController = loadController(name);
                    controllers.add(linearCarloController);
                } else {
                    mainPane.getChildren().add(linearCarloController.getView());
                    changeTitle("Algorytm Monte Carlo Liniowo");
                }
            }
            case NONLINEARCARLO -> {
                if (nonLinearCarloController == null) {
                    name = "nonlinearcarlo";
                    changeTitle("Algorytm Monte Carlo Nieliniowo");
                    nonLinearCarloController = loadController(name);
                    controllers.add(nonLinearCarloController);
                } else {
                    mainPane.getChildren().add(nonLinearCarloController.getView());
                    changeTitle("Algorytm Monte Carlo Nieliniowo");
                }
            }
        }
    }
    private <T extends ViewController> T loadController(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ndominkiewicz/optui/fxml/views/" + name + ".fxml"));
            Node node = loader.load();
            T controller = loader.getController();
            mainPane.getChildren().add(node);
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
