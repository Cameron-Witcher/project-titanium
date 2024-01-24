package com.quickscythe.silver.game.object.entity;

import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.gui.Camera;
import com.quickscythe.silver.utils.Direction;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends Entity implements Controllable {

    Rectangle collision_box;


    public Player(Location location) {
        super(location);
        size = 10;
        collision_box = new Rectangle((int) (getBounds().getX() - 1), (int) (getBounds().getY() - 1), (int) (getBounds().getWidth() + 2), (int) (getBounds().getHeight() + 2));
//        cached_images.put(0D, Resources.getImage("player.png"));
//        cached_images.put(45D, Resources.getImage("player45.png"));
//        cached_images.put(90D, Resources.getImage("player90.png"));
//        cached_images.put(135D, Resources.getImage("player135.png"));
//        cached_images.put(180D, Resources.getImage("player180.png"));
//        cached_images.put(225D, Resources.getImage("player225.png"));
//        cached_images.put(270D, Resources.getImage("player270.png"));
//        cached_images.put(315D, Resources.getImage("player315.png"));
//        image = Resources.getImage("player.png");
    }

    @Override
    public void update(List<GameObject> objects) {

        collisions.clear();
        intersections.clear();
        Thread loop = new Thread(()->{
            for (GameObject object : objects)
                if (collision_box.getBounds().intersects(object.getBounds()) && !object.equals(this))
                    checkCollisionDirection(object);
        });
        loop.start();


        super.update(objects);

    }

    @Override
    public void draw(Graphics g, Camera camera) {
        collision_box.setLocation((int) getLocation().getX() - collision_box.width / 2, (int) getLocation().getY() - collision_box.height / 2);
        super.draw(g, camera);
        g.setColor(Color.MAGENTA);
        if (GameUtils.debug()) g.drawRect(collision_box.x, collision_box.y, collision_box.width, collision_box.height);
//        g.drawRect((int) (getRelativeX(camera) - (getBounds().getWidth() / 2)), (int) (getRelativeY(camera) - (getBounds().getHeight() / 2)), (int) getBounds().getWidth(), (int) getBounds().getHeight());
    }

    private void checkCollisionDirection(GameObject object) {

        if (predictX() + getBounds().getWidth() / 2 > object.getBounds().getMinX() && predictX() + getBounds().getWidth() / 2 < object.getBounds().getMinX() + max_speed) {
            if (!collisions.contains(Direction.RIGHT)) collisions.add(Direction.RIGHT);
            getLocation().setX(object.getLocation().getX() - (object.getBounds().getWidth() / 2) - (getBounds().getWidth() / 2));

        }
        if (predictX() - getBounds().getWidth() / 2 < object.getBounds().getMaxX() && predictX() - getBounds().getWidth() / 2 > object.getBounds().getMaxX() - max_speed) {
            if (!collisions.contains(Direction.LEFT)) collisions.add(Direction.LEFT);
            getLocation().setX(object.getLocation().getX() + (object.getBounds().getWidth() / 2) + (getBounds().getWidth() / 2) + 0);
        }
        if (predictY() - getBounds().getHeight() / 2 < object.getBounds().getMaxY() && predictY() - getBounds().getHeight() / 2 > object.getBounds().getMaxY() - max_speed) {
            if (!collisions.contains(Direction.UP)) collisions.add(Direction.UP);
            getLocation().setY(object.getLocation().getY() + (object.getBounds().getHeight() / 2) + (getBounds().getHeight() / 2)-1);
        }


    }

    private double predictX() {
        return getLocation().getX() + velocity.getX();
    }

    private double predictY() {
        return getLocation().getY() + velocity.getY();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            directions.remove(Direction.UP);
            directions.add(Direction.UP);
//            if (Math.abs(getVelocity().getY()) < max_speed) velocity.add(0, accel);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            directions.remove(Direction.LEFT);
            directions.add(Direction.LEFT);
//            if (Math.abs(getVelocity().getX()) < max_speed) velocity.add(-accel, 0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            directions.remove(Direction.DOWN);
            directions.add(Direction.DOWN);
//            if (Math.abs(getVelocity().getY()) < max_speed) velocity.add(0, -accel);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            directions.remove(Direction.RIGHT);
            directions.add(Direction.RIGHT);
//            if (Math.abs(getVelocity().getX()) < max_speed) velocity.add(accel, 0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            directions.remove(Direction.UP);
//            velocity.setY(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            directions.remove(Direction.LEFT);
//            velocity.setX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            directions.remove(Direction.DOWN);
//            velocity.setY(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            directions.remove(Direction.RIGHT);
//            velocity.setX(0);
        }

    }

    public List<Direction> getCollisions() {
        return collisions;
    }
}
