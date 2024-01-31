package com.quickscythe.silver.game.controllers;

public interface EntityController {

    public boolean requestingUp();
    public boolean requestingDown();
    public boolean requestingLeft();
    public boolean requestingRight();
    public double getX();
    public double getY();
}
