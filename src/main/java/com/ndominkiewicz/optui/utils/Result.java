package com.ndominkiewicz.optui.utils;

import com.ndominkiewicz.optui.optimizationMethods.Bisection;

import java.util.List;

public class Result {
    private final int sectorId;
    private final int pointsInCircle;
    private final int totalPoints;
    private final List<Point> points;
    public static void writeOut(Bisection algorithm) {
        CLI.log("Bisection algorithm in " + algorithm.mode + " mode");
        CLI.log("Ended in " + algorithm.iterations + " iterations");
        CLI.log("a = [" + algorithm.a + ", " + algorithm.b + "] = b");
        CLI.log("L = " + algorithm.L);
        CLI.log("x1, x2 : (" + algorithm.x1 + ", " + algorithm.x2 + ")");
        CLI.log("xsr = " + algorithm.xsr);
    }
    public Result(int sectorId, int pointsInCircle, int totalPoints, List<Point> points) {
        this.sectorId = sectorId;
        this.pointsInCircle = pointsInCircle;
        this.totalPoints = totalPoints;
        this.points = points;
    }
    public int getPointsInCircle() {
        return pointsInCircle;
    }
    public int getSectorId() {
        return sectorId;
    }
    public int getTotalPoints() {
        return totalPoints;
    }
    public List<Point> getPoints() {
        return points; }
}
