package com.ndominkiewicz.optui.utils;

public class Point {
    private final double x, y;
    private final boolean inCircle;

    public Point(double x, double y, boolean inCircle) {
        this.x = x;
        this.y = y;
        this.inCircle = inCircle;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isInCircle() { return inCircle; }
}
