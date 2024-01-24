package com.quickscythe.silver.utils;

import java.text.DecimalFormat;

public class Velocity {
    double x;
    double y;

    public static final double BUFFER = 10000D;

    public Velocity(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Velocity add(double x, double y) {
        this.x = Math.round((this.x+x) * BUFFER) / BUFFER;
        this.y = Math.round((this.y+y) * BUFFER) / BUFFER;
        return this;
    }

    public double getSpeed() {
        return Math.round(Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2)) * BUFFER) / BUFFER;
    }


    public Velocity divide(double x, double y) {
        this.x = Math.round((this.x/x) * BUFFER) / BUFFER;
        this.y = Math.round((this.y/y) * BUFFER) / BUFFER;
        return this;
    }

    @Override
    public String toString() {
        this.x = Math.round((this.x) * BUFFER) / BUFFER;
        this.y = Math.round((this.y) * BUFFER) / BUFFER;
        return "<" + x + ", " + y + "> (" + getSpeed() + ")";
    }

    public void multiply(double x, double y) {
        this.x = Math.round((this.x*x) * BUFFER) / BUFFER;
        this.y = Math.round((this.y*y) * BUFFER) / BUFFER;
    }
}
