package com.ndominkiewicz.optui.controllers;

import com.ndominkiewicz.optui.models.ViewController;
import com.ndominkiewicz.optui.parallelProgramming.MonteCarlo;
import com.ndominkiewicz.optui.parallelProgramming.MonteCarloParallel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.K;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NonLinearCarloController implements ViewController, Initializable {
    private GraphicsContext graphicsContext;
    private final int canvasWidth = 400;
    private final int canvasHeight = 400;
    private MonteCarloParallel algorithm;
    /* FXML */
    @FXML
    private GridPane root;
    @FXML
    private Canvas canvas;
    @FXML
    private Label result;
    @FXML
    private TextField input;
    @FXML
    private Button start;
    @FXML
    private Label time;
    @FXML
    private Label error;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUpActions();
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);
        graphicsContext = canvas.getGraphicsContext2D();
        algorithm = new MonteCarloParallel(canvasWidth, canvasHeight, result, time);
    }
    @Override
    public Node getView() {
        return root;
    }
    private void loadUpActions() {
        start.setOnAction(event -> {
            if(isInputAlright()) {
                runAlgorithm();
            }
        });
    }
    private boolean isInputAlright() {
        error.setVisible(false);
        String text = input.getText().trim();
        if (text.isEmpty()) {
            error.setText("Input nie może być pusty");
            error.setVisible(true);
            return false;
        }
        if (text.contains(" ")) {
            error.setText("Input nie może zawierać spacji");
            error.setVisible(true);
            return false;
        }
        try {
            if (text.contains("d") || text.contains("f")) {
                error.setText("Input musi zawierać jedynie liczby");
                error.setVisible(true);
                return false;
            }
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            error.setText("Input musi zawierać jedynie liczby");
            error.setVisible(true);
            return false;
        }
    }
    public void runAlgorithm() {
        Timeline timeline = new Timeline();
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        for(int i = 0; i <= 10; i++) {
            double opacity = i * 0.1;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * 0.05),
                    event -> {
                        graphicsContext.setStroke(Color.color(0.8, 0.8, 0.96, opacity));
                        graphicsContext.setLineWidth(1);
                        graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);
                        graphicsContext.strokeOval(0, 0, canvasWidth, canvasHeight);
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setOnFinished(event -> {
            graphicsContext.setStroke(Color.DARKKHAKI);
            graphicsContext.setLineWidth(1);
            graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);
            algorithm.runParallel(MonteCarloParallel.SectorLayout.FOUR_SECTORS, Integer.parseInt(input.getText()), graphicsContext);
        });
        timeline.play();
    }
}
