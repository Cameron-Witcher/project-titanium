package com.quickscythe.silver.game.object.entity;

import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.gui.Camera;
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
    double accel = 0.3;
    double max_speed = 5;
    Map<Double, BufferedImage> cached_images = new HashMap<>();

    public Entity(Location location) {
        super(location);
        this.size = 20;

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
        }
        g.setColor(color);
    }

    @Override
    public void update(List<GameObject> objects) {

        super.update(objects);

        velocity.divide(1.2, 1.2);


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
        if (Math.abs(velocity.getX()) < 0.006) velocity.setX(0);
        if (Math.abs(velocity.getY()) < 0.006) velocity.setY(0);

//        boolean movex = true;
//        boolean movey = true;

        double mx = getVelocity().getX();
        double my = getVelocity().getY();

        if ((collisions.contains(Direction.LEFT) && mx >= 0) || (collisions.contains(Direction.RIGHT) && mx <= 0)) mx = 0;
//        if () mx = 0;
        if (collisions.contains(Direction.UP) && my > 0) my = 0;
        if (collisions.contains(Direction.DOWN) && my <= 0) my = 0;

        getLocation().setX(getLocation().getX() + mx);
        getLocation().setY(getLocation().getY() - my);

//        if (!collisions.contains(Direction.LEFT) && !collisions.contains(Direction.RIGHT))
//            getLocation().setX(getLocation().getX() + velocity.getX());
//        if (!collisions.contains(Direction.UP) && !collisions.contains(Direction.DOWN))
//            getLocation().setY(getLocation().getY() - velocity.getY());

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
