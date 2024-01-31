package com.quickscythe.silver.game.object.entity;

import com.quickscythe.silver.game.Camera;
import com.quickscythe.silver.game.controllers.EntityController;
import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.game.object.Platform;
import com.quickscythe.silver.utils.Direction;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;
import com.quickscythe.silver.utils.Velocity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Entity extends GameObject {

    Velocity velocity = new Velocity(0, 0);
    List<Direction> directions = new ArrayList<Direction>();
    List<Direction> collisions = new ArrayList<Direction>();
    double accel = 0.5;
    double max_speed = 9;
    Map<Double, BufferedImage> cached_images = new HashMap<>();
    Rectangle collision_box;
    List<EntityController> controllers = new ArrayList<>();


    public Entity(Location location) {
        super(location);
        collision_box = new Rectangle((int) (getBounds().getX() - 1), (int) (getBounds().getY() - 1), (int) (getBounds().getWidth() + 2), (int) (getBounds().getHeight() + 2));
        this.width = 20;
        this.height = 20;

    }

    @Override
    public void setImage(BufferedImage image) {

        super.setImage(image);
    }

    @Override
    public void draw(Graphics g, Camera camera) {

        Color color = g.getColor();
        g.setColor(Color.GREEN);
        super.draw(g, camera);
//        int r2 = (int) (Math.round(((double) (Math.toDegrees(Math.acos(velocity.getX()/ velocity.getSpeed()))))/45))*45;
//        if(r2 > 0) r2=r2+180;
//        r2 = Integer.isNaN(r2) ? 45 : r2;

//        System.out.println(Math.toDegrees(Math.acos(velocity.getX()/ velocity.getSpeed())));

//        double r = 45;
//        if(cached_images.containsKey(Double.parseDouble(r2+""))){
//            image = cached_images.get(Double.parseDouble(r2+""));
//        } else {
//            image = Resources.rotate(image, Double.parseDouble(r2+""));
//            cached_images.put(Double.parseDouble(r2+""),image);
//        }

        if (GameUtils.debug()) {
            g.setColor(Color.BLUE);
            g.drawLine(getRelativeX(camera), getRelativeY(camera), (int) (getRelativeX(camera) + getVelocity().getX() * 10), (int) (getRelativeY(camera) - getVelocity().getY() * 10));
            g.drawRect((int) ((getRelativeX(camera) + velocity.getX() * 1.2) - collision_box.getWidth() / 2), (int) ((getRelativeY(camera) + velocity.getY() * 1.2) - collision_box.getHeight() / 2), (int) collision_box.getWidth(), (int) collision_box.getHeight());
            g.setColor(Color.MAGENTA);
            if (GameUtils.debug())
                g.drawRect(collision_box.x, collision_box.y, collision_box.width, collision_box.height);
        }
//        (int) (((bounds.getX() + (width / 2)) - camera.getViewport().getX()) * 1)
        g.setColor(color);
    }

    public List<Direction> getCollisions() {
        return collisions;
    }

    public void updateCollisionBox() {
        collision_box.setLocation((int) (predictX() - (getBounds().getWidth() + 2) / 2), (int) (predictY() - (getBounds().getHeight() + 2) / 2));
        collision_box.setSize((int) (getBounds().getWidth() + 2), (int) (getBounds().getHeight() + 2));
    }

    @Override
    public void update(List<GameObject> objects) {

        super.update(objects);
        updateCollisionBox();

        collisions.clear();
        intersections.clear();
        for (GameObject object : objects)
            if (collision_box.getBounds().intersects(object.getBounds()) && !object.equals(this))
                if (object.isCollidable()) checkCollisionDirection(object);

//        velocity.add(0, -0.5);

        if (!GameUtils.debug()) {
            if (!collisions.contains(Direction.DOWN)) velocity.add(0, -0.2);
            else if (velocity.getY() < 0) velocity.setY(0);
        }
        if (collisions.contains(Direction.UP) && velocity.getY() > 0) velocity.setY(0);
        velocity.divide(1.2, 1);
        if (Math.abs(velocity.getX()) < 0.006) velocity.setX(0);
        if (Math.abs(velocity.getY()) < 0.006) velocity.setY(0);


//        my = my - 0.3;
        directions.clear();

        for (EntityController cont : controllers) {
            if (cont.requestingUp() && (GameUtils.debug() || collisions.contains(Direction.DOWN))) {
                jump();
            }

//            if(cont.requestingUp()) directions.add(Direction.UP);
            if (cont.requestingDown()) directions.add(Direction.DOWN);
            if (cont.requestingLeft()) directions.add(Direction.LEFT);
            if (cont.requestingRight()) directions.add(Direction.RIGHT);
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
                    if (velocity.getY() > -max_speed) velocity.add(0, -accel * 0.7);
                    break;
                case RIGHT:
                    if (velocity.getX() < max_speed) velocity.add(accel, 0);
                    break;
            }

        }
        double mx = getVelocity().getX();
        double my = getVelocity().getY();
//        for (Direction dir : collisions) {
//            switch (dir) {
//                case UP:
//                    velocity.setY(0);
//                    break;
//                case DOWN:
//                    velocity.setY(0);
//                    break;
//                case LEFT:
//                    velocity.setX(0);
//                    break;
//                case RIGHT:
//                    velocity.setX(0);
//                    break;
//            }
//        }


        if ((collisions.contains(Direction.LEFT) && mx < 0) || (collisions.contains(Direction.RIGHT) && mx > 0)) mx = 0;
        if (collisions.contains(Direction.UP) && my > 0) my = 0;
        if (collisions.contains(Direction.DOWN) && my < 0) my = 0;

//        System.out.println("MX: " + mx + " MY: " + my);

//        velocity.setX(mx);
//        velocity.setY(my);
        getLocation().setX(getLocation().getX() + mx);
        getLocation().setY(getLocation().getY() - my);


//        if (!collisions.contains(Direction.LEFT) && !collisions.contains(Direction.RIGHT))
//            getLocation().setX(getLocation().getX() + velocity.getX());
//        if (!collisions.contains(Direction.UP) && !collisions.contains(Direction.DOWN))
//            getLocation().setY(getLocation().getY() - velocity.getY());

    }

    private void jump() {
        if (GameUtils.debug()) {
            getVelocity().setY(getVelocity().getY() + accel);
        } else getVelocity().setY(6);
    }

    private void checkCollisionDirection(GameObject object) {

        if (collision_box.getMaxX() > object.getBounds().getMinX() && collision_box.getMaxX() < object.getBounds().getMinX() + velocity.getSpeed() + 3) {
            if (!(object instanceof Platform)) {
                if (!collisions.contains(Direction.RIGHT)) collisions.add(Direction.RIGHT);
                getLocation().setX(getLocation().getX() - 0.05);
            }


        }
        if (collision_box.getMinX() < object.getBounds().getMaxX() && collision_box.getMinX() > object.getBounds().getMaxX() - velocity.getSpeed() - 3) {
            if (!(object instanceof Platform)) {
                if (!collisions.contains(Direction.LEFT)) collisions.add(Direction.LEFT);
                getLocation().setX(getLocation().getX() + 0.05);
            }
        }

        if (collision_box.getMaxY() > object.getBounds().getMinY() && collision_box.getMaxY() < object.getBounds().getMinY() + velocity.getSpeed() + 3) {
            if (!collisions.contains(Direction.DOWN)) collisions.add(Direction.DOWN);

            if (object instanceof Platform && directions.contains(Direction.DOWN)) {
                collisions.remove(Direction.DOWN);
            }
            if(getVelocity().getY() <0)
            getLocation().setY(object.getLocation().getY() - (object.getBounds().getHeight() / 2) - (collision_box.getHeight() / 2) - 4);
        }
        if (collision_box.getMinY() < object.getBounds().getMaxY() && collision_box.getMinY() > object.getBounds().getMaxY() - velocity.getSpeed() - 3) {
            if (!(object instanceof Platform)) {
                if (!collisions.contains(Direction.UP)) collisions.add(Direction.UP);
            }
//            getLocation().setY(object.getLocation().getY() + (object.getBounds().getHeight() / 2) + (collision_box.getHeight() / 2)+2);
        }


    }

    double predictX() {
        return getLocation().getX() + velocity.getX();
    }

    double predictY() {
        return getLocation().getY() - velocity.getY();
    }


    public void setVelocity(double x, double y) {
        setVelocity(new Velocity(x, y));
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }


    public List<Direction> getDirections() {
        return directions;
    }

}
