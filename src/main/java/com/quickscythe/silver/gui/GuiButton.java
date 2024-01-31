package com.quickscythe.silver.gui;

import com.quickscythe.silver.game.Camera;
import com.quickscythe.silver.utils.Location;

import java.awt.*;

public class GuiButton extends GuiElement {

    String value;
    Runnable runnable;

    public GuiButton(Location location, String value, Runnable runnable) {
        super(location, 100, 100);
        this.value = value;
        this.runnable = runnable;
    }

    @Override
    public void draw(Camera camera, Graphics g) {
        Color color = g.getColor();
        g.setColor(hover ? clicked ? Color.RED : Color.BLUE : Color.GREEN);
        width = g.getFontMetrics().stringWidth(value)+20;
        height = g.getFontMetrics().getHeight()+4;
//        super.draw(camera, g);
        g.drawRect((int) (location.getX() - width / 2), (int) (location.getY() - height / 2), (int) width, (int) height);
//        g.fillRect((int) (location.getX() - width / 2), (int) (location.getY() - height / 2), (int) width, (int) height);
        g.setColor(Color.WHITE);
        g.drawString(value, (int) (location.getX() - g.getFontMetrics().stringWidth(value)/2), (int) (location.getY() + g.getFontMetrics().getHeight()/4));
        g.setColor(color);

    }

    @Override
    public void unclick() {
        super.unclick();
        runnable.run();
    }
}
