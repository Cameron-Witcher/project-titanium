package com.quickscythe.silver.game.object;

import com.quickscythe.silver.game.Camera;
import com.quickscythe.silver.utils.Location;

import java.awt.*;

public class Platform extends GameObject {



    public Platform(Location location) {
        super(location);
        this.height = 5;
        this.collidable = true;
    }

    public void setWidth(int width){
        this.width = width;
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        super.draw(g, camera);
    }
}
