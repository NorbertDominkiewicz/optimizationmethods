package com.ndominkiewicz.optui.controllers;

import com.ndominkiewicz.optui.optimizationMethods.Bisection;
import com.ndominkiewicz.optui.models.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.Queue;

public class BisectionController implements ViewController {
    private Queue<Bisection> queue = new LinkedList<>();
    private Queue<String> tasks = new LinkedList<>();
    @FXML
    private GridPane root;
    @FXML
    private TextField a;
    @FXML
    private TextField b;
    @FXML
    private TextField epsilon;
    @FXML
    private TextField task;
    @FXML
    private Button run;
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    public void initialize() {
        chart.setAnimated(true);
        xAxis.setLabel("x");
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);
        yAxis.setLabel("f(x)");
        run.setOnAction(event -> addToQueue());
    }
    @Override
    public Node getView() {
        return root;
    }
    private void updateXBounds() {
        Platform.runLater(() -> {
            xAxis.setLowerBound(Double.parseDouble(a.getText()) * 2);
            xAxis.setUpperBound(Math.abs(Double.parseDouble(b.getText()) * 2));
        });
    }
    public void addToQueue() {
        updateXBounds();
        String function = task.getText();
        stopAll();
        Bisection algorithm = new Bisection(
                Double.parseDouble(a.getText()),
                Double.parseDouble(b.getText()),
                Double.parseDouble(epsilon.getText()),
                function
        );
        tasks.add(function);
        queue.add(algorithm);
        algorithm.startInThread();
    }
    public void stopAll() {
        queue.forEach(Bisection::stop);
    }
}
