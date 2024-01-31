package com.quickscythe.silver.game;

import com.quickscythe.silver.Main;
import com.quickscythe.silver.game.board.Scene;
import com.quickscythe.silver.game.board.MenuBoard;
import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera {

    Rectangle viewport;


    Scene current_board;
    private Scene next_board = null;

    private double rwidth = 1280;
    private double rheight = 720;

    Graphics frame_graphics;
    Map<Integer, Image> parallax_backgrounds = new HashMap<>();
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Map<GraphicsDevice, VolatileImage> monitor_storage = new HashMap<>();
    GraphicsDevice current_screen = ge.getDefaultScreenDevice();


    public Camera(int width, int height) {
        viewport = new Rectangle(0, 0, (int) rwidth, (int) rheight);
        GameUtils.Backgrounds.BACKGROUND1.loadMap(parallax_backgrounds);
//        ge.getScreenDevices()
        for(GraphicsDevice gd : ge.getScreenDevices()){
            monitor_storage.put(gd, gd.getDefaultConfiguration().createCompatibleVolatileImage((int) rwidth, (int) rheight));
            monitor_storage.get(gd).setAccelerationPriority(0);
            frame_graphics=monitor_storage.get(gd).createGraphics();
        }

    }


    public List<GameObject> getAllObjects() {
        List<GameObject> objects = new ArrayList<>();
        for (GameObject object : GameUtils.getObjects())
            if (object.getBounds().intersects(viewport)) objects.add(object);


        return objects;
    }

    public void queueBoard(Scene board) {
        next_board = board;
    }


    public void update() {
        if (current_board == null) current_board = new MenuBoard();
        current_board.update(this);

        if (next_board != null) {
            current_board = next_board;
            next_board = null;
        }
    }


    public void draw(Window.Screen screen) {
        Graphics g = screen.getGraphics();



        if (current_board == null) current_board = new MenuBoard();
//
        if(!Main.getWindow().getGraphicsConfiguration().getDevice().equals(current_screen))
            current_screen = Main.getWindow().getGraphicsConfiguration().getDevice();


        monitor_storage.get(current_screen).validate(ge.getDefaultScreenDevice().getDefaultConfiguration());
        frame_graphics = monitor_storage.get(current_screen).createGraphics();
        current_board.draw(this, frame_graphics);
        g.drawImage(monitor_storage.get(current_screen), 0, 0, screen.getWidth(), screen.getHeight(), null);
//        frame.flush();
        frame_graphics.dispose();

        if (GameUtils.debug()) {
            g.drawRect(viewport.x, viewport.y, viewport.width, viewport.height);

            g.setColor(Color.GREEN);
            g.drawString("FPS: " + Main.getWindow().getScreen().fps, 0, g.getFontMetrics().getHeight());
            g.drawString("TPS: " + Main.getWindow().getScreen().tps, 0, g.getFontMetrics().getHeight() * 2);
            g.drawString("Objects: " + GameUtils.getObjects().size(), 0, g.getFontMetrics().getHeight() * 3);
            g.drawString("Rendered Objects: " + getAllObjects().size(), 0, g.getFontMetrics().getHeight() * 4);
            String s = "Viewport Size: " + getViewport().getWidth() + ", " + getViewport().getHeight();
            g.drawString(s, screen.getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight());
            s = "Viewport Scale: " + 1;
            g.drawString(s, screen.getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight() * 2);
            s = "Viewport Location: (" + getViewport().getLocation().getX() + ", " + getViewport().getLocation().getY() + ")";
            g.drawString(s, screen.getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight() * 3);


        }
    }

    public void setSize(int width, int height) {

//        viewport.setSize(width, height);
    }

    public Rectangle getViewport() {
        return viewport;
    }

    public boolean canSee(Location loc) {
        return viewport.contains(loc.getX(), loc.getY());
    }
}
