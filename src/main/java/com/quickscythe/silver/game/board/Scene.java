package com.quickscythe.silver.game.board;

import com.quickscythe.silver.game.Camera;

import java.awt.*;

public interface Scene {

    public void draw(Camera camera, Graphics g);

    public void update(Camera camera);

}
