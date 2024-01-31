package com.quickscythe.silver.game.controllers;

import com.quickscythe.silver.Main;
import com.quickscythe.silver.utils.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardController implements EntityController, KeyListener {

    Map<String, Direction> keybinds = new HashMap<>();
    List<String> keys_pressed = new ArrayList<>();

    public KeyboardController(){
        keybinds.put(KeyEvent.VK_SPACE + "",Direction.UP);
        keybinds.put(KeyEvent.VK_S+ "",Direction.DOWN);
        keybinds.put(KeyEvent.VK_A+ "",Direction.LEFT);
        keybinds.put(KeyEvent.VK_D+ "",Direction.RIGHT);
        Main.getWindow().getScreen().addKeyListener(this);
    }


    private int getKeybind(Direction direction){
        for(Map.Entry<String, Direction> entry : keybinds.entrySet()){
            if(entry.getValue().equals(direction)) return Integer.parseInt(entry.getKey());
        }
        return -1;
    }


    @Override
    public boolean requestingUp() {
        return keys_pressed.contains(getKeybind(Direction.UP) + "");
    }

    @Override
    public boolean requestingDown() {
        return keys_pressed.contains(getKeybind(Direction.DOWN) + "");
    }

    @Override
    public boolean requestingLeft() {
        return keys_pressed.contains(getKeybind(Direction.LEFT) + "");
    }

    @Override
    public boolean requestingRight() {
        return keys_pressed.contains(getKeybind(Direction.RIGHT) + "");
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys_pressed.remove(e.getKeyCode()+ "");
        keys_pressed.add(e.getKeyCode() + "");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys_pressed.remove(e.getKeyCode()+ "");
    }
}
