package com.ndominkiewicz.optui.controllers;

import com.ndominkiewicz.optui.optimizationMethods.Bisection;
import com.ndominkiewicz.optui.models.ViewController;
import com.ndominkiewicz.optui.optimizationMethods.Fibonacci;
import com.ndominkiewicz.optui.utils.MINMAX;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.Queue;

public class FibonacciController implements ViewController {
    private Queue<Fibonacci> queue = new LinkedList<>();
    private Queue<String> tasks = new LinkedList<>();
    private XYChart.Series<Number, Number> series;
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
        series = new XYChart.Series<>();
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
        chart.getData().clear();
        Fibonacci algorithm;
        updateXBounds();
        String function = task.getText();
        if(function.isEmpty()) {
            task.setText("x^3-3*x^2-20*x+1");
            function = "x^3-3*x^2-20*x+1";
            algorithm = new Fibonacci(Double.parseDouble(a.getText()), Double.parseDouble(b.getText()), Double.parseDouble(epsilon.getText()), function, MINMAX.MAXIMUM, series);
            } else {
            algorithm = new Fibonacci(Double.parseDouble(a.getText()), Double.parseDouble(b.getText()), Double.parseDouble(epsilon.getText()), function, MINMAX.MAXIMUM, series);
        }
        tasks.add(function);
        queue.add(algorithm);
        algorithm.run();
        chart.getData().add(series);
    }
}
