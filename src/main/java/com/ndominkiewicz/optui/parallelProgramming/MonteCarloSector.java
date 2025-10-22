package com.ndominkiewicz.optui.parallelProgramming;

import com.ndominkiewicz.optui.utils.Point;
import com.ndominkiewicz.optui.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

class MonteCarloSector extends Thread {
    private final int sectorId;
    private final double startX, endX, startY, endY;
    private final int pointsPerSector;
    private final BlockingQueue<Result> resultsQueue;
    private final List<Point> points;
    private int pointsInCircle = 0;

    public MonteCarloSector(int sectorId, double startX, double endX,
                            double startY, double endY, int pointsPerSector,
                            BlockingQueue<Result> resultsQueue) {
        this.sectorId = sectorId;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.pointsPerSector = pointsPerSector;
        this.resultsQueue = resultsQueue;
        this.points = new ArrayList<>(pointsPerSector);
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < pointsPerSector; i++) {
            // Losowanie tylko we własnym sektorze!
            double x = startX + random.nextDouble() * (endX - startX);
            double y = startY + random.nextDouble() * (endY - startY);

            boolean inCircle = (x * x + y * y <= 1.0);

            if (inCircle) {
                pointsInCircle++;
            }

            // Zapisz punkt do późniejszego rysowania
            points.add(new Point(x, y, inCircle));
        }

        // Wysłanie wyniku przez KOLEJKĘ
        try {
            resultsQueue.put(new Result(sectorId, pointsInCircle, pointsPerSector, points));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
