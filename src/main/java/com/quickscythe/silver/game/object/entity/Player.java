package com.quickscythe.silver.game.object.entity;

import com.quickscythe.silver.game.Camera;
import com.quickscythe.silver.utils.AnimationDirector;
import com.quickscythe.silver.game.controllers.GamepadController;
import com.quickscythe.silver.game.controllers.KeyboardController;
import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.game.object.Illuminant;
import com.quickscythe.silver.utils.Direction;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;
import com.quickscythe.silver.utils.Resources;

import java.awt.*;
import java.util.List;

public class Player extends Entity implements Illuminant {

    public AnimationDirector animationController = new AnimationDirector(Resources.getImage("sprites/block_man.png"), 32, 32);

    Direction last_direction = Direction.RIGHT;

    public Player(Location location) {
        super(location);
        this.width = 50;
        this.height = 50;
        collidable = true;
        controllers.add(new KeyboardController());
        controllers.add(new GamepadController(0));
        animationController.setAnimation(12);
        collision_box.setSize((int) (width - 14), (int) (height - 12));

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
    public void updateCollisionBox() {
        collision_box.setLocation((int) (predictX() - (width - 14) / 2), (int) (predictY() - (height) / 2) +12);
//        collision_box.setSize((int) (width - 14), (int) (height - 8));
    }

    @Override
    public void update(List<GameObject> objects) {
        if(getLocation().getX() < 640)
            getLocation().setX(640);

//        collision_box.setLocation((int) (predictX() - (getBounds().getWidth() + 2) / 2), (int) (predictY() - (getBounds().getHeight() + 2) / 2));
//        collision_box.setSize((int) (getBounds().getWidth() + 2), (int) (getBounds().getHeight() + 2));

        super.update(objects);
        if (Math.abs(velocity.getX()) < accel) animationController.setAnimation(0);
        else animationController.setAnimation(1);
        if (velocity.getX() > 0) last_direction = Direction.LEFT;
        if (velocity.getX() < 0) last_direction = Direction.RIGHT;
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        image = animationController.getFrame();
        if (last_direction.equals(Direction.RIGHT)) {
            image = Resources.flipImage(image);
        }

//        animationController.setAnimation(6);d
        super.draw(g, camera);
        if (GameUtils.debug()) {
            g.drawString("Player Location: " + getLocation().toString() + "~(" + getRelativeX(camera) + ", " + getRelativeY(camera) + ")", 0, g.getFontMetrics().getHeight() * 5);
            g.drawString("Player Velocity: " + getVelocity().toString(), 0, g.getFontMetrics().getHeight() * 6);
            g.drawString("Player Directions: ", 0, g.getFontMetrics().getHeight() * 7);
            int i = 8;
            for (Direction direction : getDirections()) {
                g.drawString(" - " + direction.name(), 0, g.getFontMetrics().getHeight() * i);
                i = i + 1;
            }
            g.drawString("Player Collisions: ", 0, g.getFontMetrics().getHeight() * i);
            for (Direction direction : getCollisions()) {
                i = i + 1;
                g.drawString(" - " + direction.name(), 0, g.getFontMetrics().getHeight() * i);


            }
        }
    }

    @Override
    public boolean on() {
        return true;
    }

    @Override
    public int getBrightness() {
        return 50;
    }
}
