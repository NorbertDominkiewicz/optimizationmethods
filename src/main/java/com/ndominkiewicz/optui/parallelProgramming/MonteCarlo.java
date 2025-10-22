package com.ndominkiewicz.optui.parallelProgramming;

import com.ndominkiewicz.optui.controllers.LinearCarloController;
import com.ndominkiewicz.optui.models.Algorithm;
import com.ndominkiewicz.optui.models.Mode;
import com.ndominkiewicz.optui.models.ViewController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.checkerframework.checker.units.qual.K;

import java.util.Random;

class Process extends Thread {
    int maxX, maxY;
    int totalPoints;
    final PixelWriter pw;
    Random random;
    GraphicsContext gc;
    int currentPoint;
    int pointsInCircle;
    final Object lock = new Object();
    WritableImage img;
    String name;
    Process(String name, int maxX, int maxY, GraphicsContext gc, PixelWriter pw, WritableImage img, int totalPoints) {
        this.name = name;
        this.maxX = maxX;
        this.maxY = maxY;
        this.totalPoints = totalPoints;
        this.gc = gc;
        this.pw = pw;
        this.img = img;
        random = new Random();
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        for(currentPoint= 0; currentPoint < totalPoints; currentPoint++) {
            if(currentPoint >= totalPoints) return;
            double x = random.nextDouble() * 2 - 1;
            double y = random.nextDouble() * 2 - 1;
            int px = (int) ((x + 1) * maxX / 2);
            int py = (int) ((y + 1) * maxY / 2);
            if(Math.pow(x, 2) + Math.pow(y, 2) <= 1) {
                synchronized (pw) {
                    pw.setColor(px, py, Color.color(0.9, 0.9, 0.4));
                }
                synchronized (lock) {
                    pointsInCircle++;
                }
            } else {
                synchronized (pw) {
                    pw.setColor(px, py, Color.color(0.6, 0.0, 0.0));
                }
            }
            Platform.runLater(() -> gc.drawImage(img, 0, 0));
        }
    }
    public int getCurrentPoint() {
       return currentPoint;
    }
    public int getPointsInCircle() {
        synchronized (lock) {
            return pointsInCircle;
        }
    }
}

public class MonteCarlo {
    private final int areaWidth;
    private final int areaHeight;
    private int currentPoint;
    private int pointsInCircle;
    private long finalTime;
    private boolean isRunning = false;
    private ViewController controller;
    private  GraphicsContext graphicsContext;
    private Label result;
    private Label timer;
    public MonteCarlo(int areaWidth, int areaHeight, Label result, Label timer){
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.result = result;
        this.timer = timer;
    }
    private void clearData() {
        currentPoint = 0;
        pointsInCircle = 0;
        finalTime = 0;
    }
    public void runLinearly(int totalPoints, GraphicsContext gc) {
        graphicsContext = gc;
        if (!isRunning) {
            isRunning = true;
            clearData();
            long startTime = System.currentTimeMillis();
            WritableImage img = new WritableImage(areaWidth, areaHeight);
            PixelWriter pw = img.getPixelWriter();
            Random random = new Random();
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1), event -> {
                    if(currentPoint >= totalPoints) {
                        return;
                    }
                    double x = random.nextDouble() * 2 - 1;
                    double y = random.nextDouble() * 2 - 1;
                    int px = (int) ((x + 1) * areaWidth / 2);
                    int py = (int) ((y + 1) * areaHeight / 2);
                    if(Math.pow(x, 2) + Math.pow(y, 2) <= 1) {
                        pw.setColor(px, py, Color.color(0.9, 0.9, 0.4));
                        pointsInCircle++;
                    } else {
                        pw.setColor(px, py,Color.color(0.6, 0.0, 0.0));
                    }
                    graphicsContext.drawImage(img, 0, 0);
                    long currentTime = System.currentTimeMillis();
                    currentPoint++;
                    double PI = 4.0 * pointsInCircle / currentPoint;
                    result.setText(PI + " π");
                    timer.setText((currentTime - startTime) * 0.001 + " s");
                }
            ));
            timeline.setCycleCount(totalPoints);
            timeline.setOnFinished(event -> {
                finalTime = System.currentTimeMillis() - startTime;
                isRunning = false;
            });
            timeline.play();
        }
    }

}
