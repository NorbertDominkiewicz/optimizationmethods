package com.ndominkiewicz.optui.utils;

public class Cords {
    private int x, y;
    public Cords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cords() {
        x = 0;
        y = 0;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}