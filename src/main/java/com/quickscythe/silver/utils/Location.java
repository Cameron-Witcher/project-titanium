package com.quickscythe.silver.utils;

import com.quickscythe.silver.game.Camera;

public class Location implements Cloneable {
    double x;
    double y;

    public Location(double x, double y) {
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

    public Location add(double x, double y) {
        this.x = Math.round((this.x + x) * 100D) / 100D;
        this.y = Math.round((this.y + y) * 100D) / 100D;
        return this;
    }

    public Location multiply(double x, double y) {
        this.x = Math.round((this.x * x) * 100D) / 100D;
        this.y = Math.round((this.y * y) * 100D) / 100D;
        return this;
    }

    public Location divide(double x, double y) {
        this.x = Math.round((this.x / x) * 100D) / 100D;
        this.y = Math.round((this.y / y) * 100D) / 100D;
        return this;
    }

    public Location convertFromRelative(Camera camera){
        return new Location(camera.getViewport().getBounds().getMinX() + getX(), camera.getViewport().getMinY() + getY());
//        return (int) (getLocation().getY() - (camera.getViewport().getBounds().getMinY()) );
//        return (int) (getLocation().getX() - (camera.getViewport().getBounds().getMinX()) );
    }

    public Location convertToRelative(Camera camera){
        return new Location(getX()-camera.getViewport().getBounds().getMinX(), getY()-camera.getViewport().getMinY());
//        return (int) (getLocation().getY() - (camera.getViewport().getBounds().getMinY()) );
//        return (int) (getLocation().getX() - (camera.getViewport().getBounds().getMinX()) );
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Override
    public Location clone() {
        try {
            Location clone = (Location) super.clone();
           clone.setX(x);
           clone.setY(y);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
