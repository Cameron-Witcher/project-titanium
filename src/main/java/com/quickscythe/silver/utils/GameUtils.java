package com.quickscythe.silver.utils;

import com.quickscythe.silver.game.object.GameObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    private static final List<GameObject> allGameObjects = new ArrayList<>();
    private static boolean debug = false;

    public static GameObject createObject(Location loc, Class<? extends GameObject> objectClass) {
        try {
            Constructor<? extends GameObject> con = objectClass.getConstructor(Location.class);
            GameObject obj = (GameObject) con.newInstance(loc);
            System.out.println(obj.getLocation().getX() + ", " + obj.getLocation().getY());
            System.out.println(loc.getX() + ", " + loc.getY());
            allGameObjects.add(obj);
            return obj;
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean toggleDebug() {
        debug = !debug;
        return debug;
    }

    public static void setDebug(boolean set) {
        debug = set;
    }

    public static List<GameObject> getObjects() {
        return allGameObjects;
    }

    public static boolean debug() {
        return debug;
    }

    public static double distance(Location loc1, Location loc2) {
        return Math.sqrt(Math.pow(loc2.getX() - loc1.getX(), 2) + Math.pow(loc2.getY() - loc1.getY(), 2));

    }
}
