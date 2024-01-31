package com.quickscythe.silver.gui;

import com.quickscythe.silver.game.Camera;
import com.quickscythe.silver.utils.Location;

import java.awt.*;

public class GuiElement implements GuiInterface {

    Location location;
    double width;
    double height;

    boolean hover = false;

    public boolean clicked = false;

    public GuiElement(Location location, double width, double height) {
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Location location) {
        return new Rectangle((int) (this.location.getX() - this.width / 2), (int) (this.location.getY() - this.height / 2), (int) width, (int) height).contains(location.getX(), location.getY());
    }

    public void draw(Camera camera, Graphics g) {
        Color color = g.getColor();
        g.setColor(hover ? clicked ? Color.RED : Color.BLUE : Color.GREEN);
        g.fillRect((int) (location.getX() - width / 2), (int) (location.getY() - height / 2), (int) width, (int) height);
        g.setColor(color);
    }

    @Override
    public void hover() {
        hover = true;
    }

    @Override
    public void click() {
        clicked = true;
    }

    public void unclick() {
        clicked = false;
    }

    @Override
    public void unhover() {
        hover = false;
    }
}
