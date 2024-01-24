package com.quickscythe.silver.gui;

import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.game.object.entity.Player;
import com.quickscythe.silver.utils.Direction;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Resources;
import com.quickscythe.silver.utils.Velocity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Camera {

    Rectangle viewport;
    List<GameObject> cached_objects = new ArrayList<>();
    boolean track_player = true;
    Player player = null;
    double tracking_buffer = 250;
    double max_speed = 4.5;
    double accel = 0.3;
    Velocity velocity = new Velocity(0, 0);
    List<Direction> directions = new ArrayList<Direction>();
    Image background;

    double scale = 1;


    public Camera(int width, int height) {
        background = Resources.getImage("test.jpg");
        viewport = new Rectangle(0, 0, width, height);
    }

    public List<GameObject> getObjects() {

        return cached_objects;
    }

    public List<GameObject> getAllObjects() {
        List<GameObject> objects = new ArrayList<>();
        for (GameObject object : GameUtils.getObjects())
            if (object.getBounds().intersects(viewport)) objects.add(object);


        return objects;
    }

    public void update() {
        tracking_buffer = (getViewport().getHeight() - getViewport().getHeight() / 10)*getScale();
        viewport.setLocation((int) (viewport.getLocation().getX() + velocity.getX()), (int) (viewport.getLocation().getY() + velocity.getY()));
        if (Math.abs(velocity.getX()) < 0.006) velocity.setX(0);
        if (Math.abs(velocity.getY()) < 0.006) velocity.setY(0);

        for(GameObject object : cached_objects)
            object.update(cached_objects);


    }


    public void draw(Graphics g) {
//        cached_objects.clear();
        if (track_player && player != null) {
//
            if (directions.isEmpty()) velocity.multiply(0.9, 0.9);
//                velocity = new Velocity(0,0);

            directions.clear();
            if (player.getRelativeX(this) > viewport.getWidth() / 2 + (tracking_buffer / 2)) {
                directions.add(Direction.RIGHT);
            }
            if (player.getRelativeX(this) < viewport.getWidth() / 2 - (tracking_buffer / 2)) {
                directions.add(Direction.LEFT);
            }
            if (player.getRelativeY(this) > viewport.getHeight() / 2 + (tracking_buffer / 2)) {
                directions.add(Direction.UP);
            }
            if (player.getRelativeY(this) < viewport.getHeight() / 2 - (tracking_buffer / 2)) {
                directions.add(Direction.DOWN);
            }

            for (Direction dir : directions) {
                switch (dir) {
                    case UP:
                        if (velocity.getY() < max_speed) velocity.add(0, accel);
                        break;
                    case LEFT:
                        if (velocity.getX() > -max_speed) velocity.add(-accel, 0);
                        break;
                    case DOWN:
                        if (velocity.getY() > -max_speed) velocity.add(0, -accel);
                        break;
                    case RIGHT:
                        if (velocity.getX() < max_speed) velocity.add(accel, 0);
                        break;
                }
            }
        }
        g.drawImage(background, -viewport.x, -viewport.y, Color.YELLOW, null);
        if (GameUtils.debug()) {
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, viewport.width, viewport.height);
            g.drawRect((int) (viewport.getWidth() / 2 - (tracking_buffer / 2)), (int) (viewport.getHeight() / 2 - (tracking_buffer / 2)), (int) (tracking_buffer), (int) (tracking_buffer));
//            g.drawRect(viewport.width/2);
        }

        g.setColor(Color.RED);
        for (GameObject object : GameUtils.getObjects()) {
            if (object.getBounds().intersects(viewport)) {
                if (!cached_objects.contains(object)) cached_objects.add(object);

                object.draw(g, this);
                if (object instanceof Player) player = (Player) object;
            } else cached_objects.remove(object);

        }

    }

    public void setSize(int width, int height) {
        viewport.setSize(width, height);
    }

    public Rectangle getViewport() {
        return viewport;
    }

    public Player getPlayer() {
        return player;
    }


    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public List<Direction> getDirections() {
        return directions;
    }
}
