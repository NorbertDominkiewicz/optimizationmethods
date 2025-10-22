package com.ndominkiewicz.optui.parallelProgramming;

import com.ndominkiewicz.optui.utils.Point;
import com.ndominkiewicz.optui.utils.Result;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MonteCarloParallel {
    private final int areaWidth, areaHeight;
    private BlockingQueue<Result> resultsQueue;
    private List<MonteCarloSector> sectors;
    private Label resultLabel;
    private Label timerLabel;
    private long startTime;

    private final AtomicInteger totalPointsInCircle = new AtomicInteger(0);
    private final AtomicInteger totalProcessedPoints = new AtomicInteger(0);

    public MonteCarloParallel(int areaWidth, int areaHeight, Label resultLabel, Label timerLabel) {
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.resultLabel = resultLabel;
        this.timerLabel = timerLabel;
    }

    public enum SectorLayout {
        TWO_SECTORS(2), FOUR_SECTORS(4), SIXTEEN_SECTORS(16);
        private final int count;
        SectorLayout(int count) { this.count = count; }
        public int getCount() { return count; }
    }

    public void runParallel(SectorLayout layout, int totalPoints, GraphicsContext gc) {
        totalPointsInCircle.set(0);
        totalProcessedPoints.set(0);

        int numSectors = layout.getCount();
        resultsQueue = new LinkedBlockingQueue<>();
        sectors = new ArrayList<>();

        createSectors(layout, totalPoints / numSectors);

        for (MonteCarloSector sector : sectors) {
            sector.start();
        }

        startResultCollector(totalPoints, gc);
    }

    private void updateUI(double pi, int processedPoints, int totalPoints) {
        long currentTime = System.currentTimeMillis();
        double elapsedSeconds = (currentTime - startTime) / 1000.0;

        resultLabel.setText(pi + " π");
        timerLabel.setText((currentTime - startTime) * 0.001 + " s");
    }

    private void createSectors(SectorLayout layout, int pointsPerSector) {
        switch (layout) {
            case TWO_SECTORS:
                sectors.add(new MonteCarloSector(0, -1, 0, -1, 1, pointsPerSector, resultsQueue));
                sectors.add(new MonteCarloSector(1, 0, 1, -1, 1, pointsPerSector, resultsQueue));
                break;

            case FOUR_SECTORS:
                sectors.add(new MonteCarloSector(0, -1, 0, -1, 0, pointsPerSector, resultsQueue));
                sectors.add(new MonteCarloSector(1, 0, 1, -1, 0, pointsPerSector, resultsQueue));
                sectors.add(new MonteCarloSector(2, -1, 0, 0, 1, pointsPerSector, resultsQueue));
                sectors.add(new MonteCarloSector(3, 0, 1, 0, 1, pointsPerSector, resultsQueue));
                break;

            case SIXTEEN_SECTORS:
                createSixteenSectors(pointsPerSector);
                break;
        }
    }

    private void createSixteenSectors(int pointsPerSector) {
        double sectorWidth = 2.0 / 4;
        double sectorHeight = 2.0 / 4;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                double startX = -1 + col * sectorWidth;
                double endX = startX + sectorWidth;
                double startY = -1 + row * sectorHeight;
                double endY = startY + sectorHeight;

                int sectorId = row * 4 + col;
                sectors.add(new MonteCarloSector(sectorId, startX, endX, startY, endY, pointsPerSector, resultsQueue));
            }
        }
    }

    private void drawSectorPoints(int sectorId, GraphicsContext gc, List<Point> points) {
        Color[] sectorColors = {
                Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
                Color.PURPLE, Color.CYAN, Color.MAGENTA, Color.YELLOW,
                Color.BROWN, Color.PINK, Color.LIME, Color.TEAL,
                Color.NAVY, Color.MAROON, Color.OLIVE, Color.CORAL
        };

        Color sectorColor = sectorColors[sectorId % sectorColors.length];

        for (Point point : points) {
            int px = (int) ((point.getX() + 1) * areaWidth / 2);
            int py = (int) ((point.getY() + 1) * areaHeight / 2);

            if (point.isInCircle()) {
                gc.setFill(sectorColor.brighter());
            } else {
                gc.setFill(sectorColor.darker());
            }

            gc.fillRect(px, py, 1, 1);
        }
    }

    private void startResultCollector(int totalPoints, GraphicsContext gc) {
        startTime = System.currentTimeMillis();

        Thread collector = new Thread(() -> {
            int completedSectors = 0;

            try {
                while (completedSectors < sectors.size()) {
                    Result result = resultsQueue.take();

                    totalPointsInCircle.addAndGet(result.getPointsInCircle());
                    totalProcessedPoints.addAndGet(result.getTotalPoints());
                    completedSectors++;

                    int currentPointsInCircle = totalPointsInCircle.get();
                    int currentProcessedPoints = totalProcessedPoints.get();

                    double pi = 4.0 * currentPointsInCircle / currentProcessedPoints;

                    Platform.runLater(() -> {
                        updateUI(pi, currentProcessedPoints, totalPoints);
                        drawSectorPoints(result.getSectorId(), gc, result.getPoints());
                    });
                }
                Platform.runLater(() -> {
                    int finalPointsInCircle = totalPointsInCircle.get();
                    int finalProcessedPoints = totalProcessedPoints.get();
                    double finalPi = 4.0 * finalPointsInCircle / finalProcessedPoints;
                    long totalTime = System.currentTimeMillis() - startTime;

                    resultLabel.setText(finalPi + " π");
                    timerLabel.setText(totalTime * 0.001 + " s");
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        collector.start();
    }

    public void clearCanvas(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, areaWidth, areaHeight);
    }

    public int getTotalPointsInCircle() {
        return totalPointsInCircle.get();
    }

    public int getTotalProcessedPoints() {
        return totalProcessedPoints.get();
    }
}
