package com.ndominkiewicz.optui.optimizationMethods;

import com.ndominkiewicz.optui.models.Algorithm;
import com.ndominkiewicz.optui.utils.CLI;
import com.ndominkiewicz.optui.utils.MINMAX;
import com.ndominkiewicz.optui.utils.Result;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Fibonacci implements Algorithm, Runnable {
    public double a, b, e;
    int n;
    public double x1, x2;
    public MINMAX mode;
    public Function<Double, Double> function;
    private XYChart.Series<Number, Number> series;
    private List<XYChart.Data<Number, Number>> functionPoints;
    public Fibonacci(double a, double b, double e, String polynomialEquation, MINMAX mode, XYChart.Series<Number, Number > series) {
        this.a = a;
        this.b = b;
        this.e = e;
        this.mode = mode;
        this.function = x -> {
            Expression expression = new ExpressionBuilder(polynomialEquation).variable("x").build().setVariable("x", x);
            return expression.evaluate();
        };
        this.series = series;
        this.functionPoints = new ArrayList<>();
        initializeFunctionPoints(a, b);
    }
    public Fibonacci(double a, double b, double e, Function<Double, Double> function, MINMAX mode, XYChart.Series<Number, Number > series) {
        this.a = a;
        this.b = b;
        this.e = e;
        this.mode = mode;
        this.function = function;
        this.series = series;
    }
    public Fibonacci(double a, double b, double e, MINMAX mode, XYChart.Series<Number, Number > series) {
        this.a = a;
        this.b = b;
        this.e = e;
        this.mode = mode;
        this.function = x -> Math.pow(x, 3) - 3 * Math.pow(x, 2) - 20 * x + 1;
        this.series = series;
    }
    private void initializeFunctionPoints(double a, double b) {
        int points = 100;
        double range = b - a;
        for (int i = 0; i <= points; i++) {
            double x = a + (range * i / points);
            double y = function.apply(x);
            functionPoints.add(new XYChart.Data<>(x, y));
        }
    }
    private int getFn(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int k = 2;
        int F_k1 = 1;
        int F_k2 = 1;
        int F_k = F_k1 + F_k2;
        if(k == n) return F_k;
        else return getFn(k + 1, n, F_k, F_k1);
    }
    private int getFn(int k, int n, int F_k1, int F_k2) {
        int F_k = F_k1 + F_k2;
        if(k == n) return F_k;
        else return getFn(k + 1, n, F_k, F_k1);
    }
    private int getN(int n, int F_k1, int F_k2) {
        int F_k = F_k1 + F_k2;
        double condition = (b - a) / F_k;
        if (condition < 2 * e) return n;
        else return getN(n + 1, F_k, F_k1);
    }
    private double getX1() {
        return b - ((double) getFn(this.n - 1) / getFn(this.n)) * (b - a);
    }
    private double getX2() {
        return a + ((double) getFn(this.n - 1) / getFn(this.n)) * (b - a);
    }
    @Override
    public void run() {
        n = getN(1, 1, 1);
        x1 = getX1();
        x2 = getX2();
        step3();
    }
    private void step3() {
        switch (mode) {
            case MAXIMUM -> {
                if(function.apply(x1) > function.apply(x2)) {
                    b = x2;
                    x2 = x1;
                    x1 = getX1();
                } else {
                    a = x1;
                    x1 = x2;
                    x2 = getX2();
                }
                n--;
                step4();
            }
            case MINIMUM -> {
                if(function.apply(x1) < function.apply(x2)) {
                    b = x2;
                    x2 = x1;
                    n--;
                    x1 = getX1();
                } else {
                    a = x1;
                    x1 = x2;
                    n--;
                    x2 = getX2();
                }
                step4();
            }
        }
    }
    private void step4() {
        if ((Math.abs(x2 - x1) < e) || n == 1) {
            double result = (a + b) / 2;
            Platform.runLater(() -> {
                series.getData().clear();
                series.getData().addAll(functionPoints);
                XYChart.Data<Number, Number> optimumPoint = new XYChart.Data<>(
                        result, function.apply(result)
                );
                optimumPoint.setNode(createCustomNode("red"));
                series.getData().add(optimumPoint);
            });
            Result.writeOut(this, result);
            series.setName("Wynik: " + String.valueOf(result));
        } else {
            Platform.runLater(() -> {
                XYChart.Data<Number, Number> point1 = new XYChart.Data<>(x1, function.apply(x1));
                XYChart.Data<Number, Number> point2 = new XYChart.Data<>(x2, function.apply(x2));
                point1.setNode(createCustomNode("blue"));
                point2.setNode(createCustomNode("blue"));
                series.getData().add(point1);
                series.getData().add(point2);
            });
            step3();
        }
    }

    private Node createCustomNode(String color) {
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(3);
        circle.setFill(javafx.scene.paint.Color.valueOf(color));
        circle.setStroke(javafx.scene.paint.Color.BLACK);
        return circle;
    }

}
