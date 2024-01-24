package com.quickscythe.silver.utils;

public class Location {
    double x;
    double y;

    public Location(double x, double y) {
        System.out.println("X: " + x + " Y: " + y);
        this.x = Math.round((x) * 100D) / 100D;
        this.y = Math.round((y) * 100D) / 100D;

    }


    public double getX() {
        return this.x = Math.round((this.x) * 100D) / 100D;
    }

    public void setX(double x) {
        this.x = Math.round(x * 100D) / 100D;
    }


    public double getY() {
        return this.y = Math.round((this.y) * 100D) / 100D;
    }

    public void setY(double y) {
        this.y = Math.round(y * 100D) / 100D;
    }

    public void add(double x, double y) {
        this.x = Math.round((this.x + x) * 100D) / 100D;
        this.y = Math.round((this.y + y) * 100D) / 100D;
    }

    public void multiply(double x, double y) {
        this.x = Math.round((this.x * x) * 100D) / 100D;
        this.y = Math.round((this.y * y) * 100D) / 100D;
    }

    public void divide(double x, double y) {
        this.x = Math.round((this.x / x) * 100D) / 100D;
        this.y = Math.round((this.y / y) * 100D) / 100D;
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }
}
