package com.quickscythe.silver.game.object;

import com.quickscythe.silver.gui.Camera;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameObject {

    protected BufferedImage image = null;
    Location location;
    protected int size = 10;
    Rectangle bounds;
    protected List<GameObject> intersections = new ArrayList<>();

    public GameObject(Location location) {
        this.location = location;
        this.bounds = new Rectangle((int) location.getX() - (size / 2), (int) location.getY() - (size / 2), size, size);
    }

    public void update(List<GameObject> objects) {
        intersections.clear();
        for (GameObject object : objects)
            if (getBounds().intersects(object.getBounds()) && !object.equals(this))
                intersections.add(object);
    }

    public void draw(Graphics g, Camera camera) {
        bounds.setBounds((int) (location.getX() - ((size / 2) )), (int) (location.getY() - ((size / 2) )), (int) (size ), (int) (size ));

        if (image == null)
            g.fillRect((int) (getRelativeX(camera) - ((bounds.getWidth() / 2)* camera.getScale())), (int) (getRelativeY(camera) - ((bounds.getHeight() / 2)* camera.getScale())), (int) (bounds.getWidth()* camera.getScale()), (int) (bounds.getHeight()* camera.getScale()));
        else {
            g.drawImage(image, (int) (bounds.getX() - camera.getViewport().getX()), (int) (bounds.getY() - camera.getViewport().getY()), (int) bounds.getWidth(), (int) bounds.getHeight(), null);
        }

        if (GameUtils.debug()) {
            g.setColor(Color.RED);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public int getRelativeX(Camera camera) {
        return (int) (((bounds.getX() + (size / 2) ) - camera.getViewport().getX())* camera.getScale());
    }

    public int getRelativeY(Camera camera) {
        return (int) (((bounds.getY() + (size / 2) ) - camera.getViewport().getY())* camera.getScale());
    }

    public Location getLocation() {
        return location;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
