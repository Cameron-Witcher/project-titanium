package com.quickscythe.silver.gui;

import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.game.object.entity.Controllable;
import com.quickscythe.silver.game.object.entity.Entity;
import com.quickscythe.silver.game.object.entity.Player;
import com.quickscythe.silver.utils.Direction;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

public class Window extends JFrame {

    Screen screen;


    public Window() {
        screen = new Screen();
        add(screen);
        pack();
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public Screen getScreen() {
        return screen;
    }

    public class Screen extends JPanel implements ActionListener {

        int fps = 0;
        Timer timer;
        Heartbeat heartbeat;
        Camera camera;
        int tps = 0;

        double max_fps = 100000000;
        double max_tps = 50;
        private long last_fps_check = 0;
        private long last_tps_check = 0;


        public Screen() {
            setBackground(Color.BLACK);

            addKeyListener(new TAdapter());
            setFocusable(true);
            requestFocusInWindow();

            camera = new Camera(getWidth(), getHeight());


            timer = new Timer(5, this);
            timer.start();

            heartbeat = new Heartbeat(this);
            heartbeat.start();


        }


        @Override
        public void actionPerformed(ActionEvent e) {
            timer.setDelay(0);
            camera.setSize(getWidth() - 1, getHeight() - 1);
        }

        protected void tick() {
            try {
                if (new Date().getTime() - ((1D / max_tps) * 1000D) >= last_tps_check) {
                    tps = (int) Math.floor(1000 / (new Date().getTime() - last_tps_check));
                    last_tps_check = new Date().getTime();
                    camera.update();
                }
            } catch (ArithmeticException ex) {
                tps = 999;
            }
            try {
                if (new Date().getTime() - ((1D / max_fps) * 1000D) >= last_fps_check) {
                    fps = (int) Math.floor(1000 / (new Date().getTime() - last_fps_check));
                    last_fps_check = new Date().getTime();
                    repaint();
                }

            } catch (ArithmeticException ex) {
                fps = 999;
            }

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            camera.draw(g);
            if (GameUtils.debug()) {
                g.setColor(Color.GREEN);
                g.drawString("FPS: " + fps, 0, g.getFontMetrics().getHeight());
                g.drawString("TPS: " + tps, 0, g.getFontMetrics().getHeight() * 2);
                g.drawString("Objects: " + GameUtils.getObjects().size(), 0, g.getFontMetrics().getHeight() * 3);
                g.drawString("Rendered Objects: " + camera.getObjects().size(), 0, g.getFontMetrics().getHeight() * 4);
                if (camera.getPlayer() != null) {
                    g.drawString("Player Location: " + camera.getPlayer().getLocation().toString(), 0, g.getFontMetrics().getHeight() * 5);
                    g.drawString("Player Velocity: " + camera.getPlayer().getVelocity().toString(), 0, g.getFontMetrics().getHeight() * 6);
                    g.drawString("Player Directions: ", 0, g.getFontMetrics().getHeight() * 7);
                    int i = 8;
                    for (Direction direction : camera.getPlayer().getDirections()) {
                        g.drawString(" - " + direction.name(), 0, g.getFontMetrics().getHeight() * i);
                        i = i + 1;
                    }
                    g.drawString("Player Collisions: ", 0, g.getFontMetrics().getHeight() * i);
                    for (Direction direction : camera.getPlayer().getCollisions()) {
                        i = i + 1;
                        g.drawString(" - " + direction.name(), 0, g.getFontMetrics().getHeight() * i);

                    }
                }

                String s = "Viewport Size: " + camera.getViewport().getWidth() + ", " + camera.getViewport().getHeight();
                g.drawString(s, getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight());
                s = "Viewport Scale: " + camera.getScale();
                g.drawString(s, getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight()*2);
                s = "Viewport Velocity: " + camera.getVelocity().toString();
                g.drawString(s, getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight()*3);
                s = "Viewport Directions: ";
                g.drawString(s, getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight()*4);
                int i = 5;
                for (Direction direction : camera.getDirections()) {
                    s = "- " + direction.name();
                    g.drawString(s, getWidth() - g.getFontMetrics().stringWidth(s), g.getFontMetrics().getHeight()*i);
                    i = i + 1;
                }

            }


//            last_fps_check = new Date().getTime();
        }



        private class TAdapter extends KeyAdapter {

            @Override
            public void keyReleased(KeyEvent e) {
                for (GameObject obj : GameUtils.getObjects())
                    if (obj instanceof Controllable) {
                        ((Controllable) obj).keyReleased(e);
                    }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                for (GameObject obj : GameUtils.getObjects())
                    if (obj instanceof Controllable) {
                        ((Controllable) obj).keyPressed(e);
                    }
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    GameUtils.toggleDebug();
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
                    camera.getViewport().setLocation((int) camera.getViewport().getLocation().getX() - 1, (int) camera.getViewport().getLocation().getY());
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
                    camera.getViewport().setLocation((int) camera.getViewport().getLocation().getX(), (int) camera.getViewport().getLocation().getY() - 1);
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
                    camera.getViewport().setLocation((int) camera.getViewport().getLocation().getX() + 1, (int) camera.getViewport().getLocation().getY());
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
                    camera.getViewport().setLocation((int) camera.getViewport().getLocation().getX(), (int) camera.getViewport().getLocation().getY() + 1);
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    camera = new Camera(getWidth(), getHeight());
                }
                if (e.getKeyCode() == KeyEvent.VK_ADD) {
                    camera.setScale(camera.getScale()+0.25);
                }
                if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                    camera.setScale(camera.getScale()-0.25);
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    GameUtils.createObject(new Location(100, 100), Player.class);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    GameUtils.getObjects().clear();
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    Entity obj = (Entity) GameUtils.createObject(new Location(50, 50), Entity.class);
                    obj.getDirections().add(Direction.UP);


                    Entity obj2 = (Entity) GameUtils.createObject(new Location(50, 50), Entity.class);
                    obj2.setVelocity(1, 0);




                }

            }

        }

    }

    class Heartbeat extends Thread {

        private final Screen screen;

        public Heartbeat(Screen screen) {
            this.screen = screen;
        }

        public void run() {
            while (true) screen.tick();

        }

    }

}
