package com.quickscythe.silver.utils;

import com.badlogic.gdx.Game;
import com.quickscythe.silver.game.Window;
import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.game.object.Platform;
import com.quickscythe.silver.game.object.entity.Player;
import com.studiohartman.jamepad.ControllerManager;
import org.json2.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameUtils {

    private static final List<GameObject> allGameObjects = new ArrayList<>();
    private static boolean debug = false;

    private static ControllerManager controllerManager = null;

    public static GameObject spawn(JSONObject json) {
        GameObject object = createObject(decodeLocation(json.getJSONObject("location")), ObjectType.valueOf(json.getString("type").toUpperCase()).getObjectClass());
        if(object instanceof Platform){
            Platform platform = (Platform) object;
            if(json.has("width")){
                platform.setWidth(json.getInt("width"));
            }
        }
        allGameObjects.add(object);
        return object;
    }

    private static GameObject createObject(Location loc, Class<? extends GameObject> objectClass){
        try {
            Constructor<? extends GameObject> con = objectClass.getConstructor(Location.class);
            GameObject obj = (GameObject) con.newInstance(loc);
//            System.out.println(obj.getLocation().getX() + ", " + obj.getLocation().getY());
//            System.out.println(loc.getX() + ", " + loc.getY());

            return obj;
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameObject spawn(Location loc, Class<? extends GameObject> objectClass) {
        GameObject object = createObject(loc, objectClass);
        allGameObjects.add(object);
        return object;
    }

    public static Location decodeLocation(JSONObject json) {
        return new Location(json.getDouble("x"), json.getDouble("y"));
    }

    public static JSONObject encodeLocation(Location location) {
        return new JSONObject("{\"x\":" + location.getX() + ",\"y\":" + location.getY() + "}");
    }

    public static void toggleDebug() {
        setDebug(!debug);
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

    public static ControllerManager getControllerManager() {
        if (controllerManager == null) {
            controllerManager = new ControllerManager();
            controllerManager.initSDLGamepad();
        }
        return controllerManager;
    }



    public enum ObjectType {
        PLAYER(Player.class), PLATFORM(Platform.class);

        Class<? extends GameObject> clazz;

        ObjectType(Class<? extends GameObject> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends GameObject> getObjectClass(){
            return clazz;
        }

    }

    public enum Backgrounds {

        MENU("menus/parallax"), BACKGROUND1("background1");

        String folder_name;

        Backgrounds(String folder_name) {
            this.folder_name = folder_name;
        }

        public void loadMap(Map<Integer, Image> map){
            switch(this){
                case MENU:
                    map.put(1, Resources.getImage("backgrounds/menus/parallax/sky.png"));
                    map.put(2, Resources.getImage("backgrounds/menus/parallax/clouds_1.png"));
                    map.put(3, Resources.getImage("backgrounds/menus/parallax/rocks.png"));
                    map.put(4, Resources.getImage("backgrounds/menus/parallax/clouds_2.png"));
                    map.put(5, Resources.getImage("backgrounds/menus/parallax/ground.png"));
                    break;
                case BACKGROUND1:
                    map.put(1, Resources.getImage("backgrounds/background1/sky.png"));
                    map.put(2, Resources.getImage("backgrounds/background1/clouds_3.png"));
                    map.put(3, Resources.getImage("backgrounds/background1/rocks_3.png"));
                    map.put(4, Resources.getImage("backgrounds/background1/clouds_2.png"));
                    map.put(5, Resources.getImage("backgrounds/background1/rocks_2.png"));
                    map.put(6, Resources.getImage("backgrounds/background1/clouds_1.png"));
                    map.put(7, Resources.getImage("backgrounds/background1/rocks_1.png"));
                    map.put(8, Resources.getImage("backgrounds/background1/pines.png"));
                    break;
            }
        }


    }
}
