package com.quickscythe.silver.game.board;

import com.quickscythe.silver.game.Camera;
import com.quickscythe.silver.game.object.GameObject;
import com.quickscythe.silver.game.object.Illuminant;
import com.quickscythe.silver.game.object.entity.Player;
import com.quickscythe.silver.utils.GameUtils;
import com.quickscythe.silver.utils.Location;
import com.quickscythe.silver.utils.Resources;
import org.json2.JSONObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LevelBoard implements Scene {
    public Player player = null;
    Map<Integer, Image> parallax_backgrounds = new HashMap<>();

    private final int TILE_SIZE = 10;
    private int LEVEL_DARKNESS = 0;

    public LevelBoard(int level) {
        GameUtils.getObjects().clear();
        StringBuilder json_string = new StringBuilder();
        try {
            Scanner scanner = new Scanner(Resources.getFile("levels/level-" + level + ".json"));

            while (scanner.hasNextLine()) {
                json_string.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        JSONObject json = new JSONObject(json_string.toString());
        int i = 0;
        if (json.has("background"))
            GameUtils.Backgrounds.valueOf(json.getString("background").toUpperCase()).loadMap(parallax_backgrounds);
        else GameUtils.Backgrounds.BACKGROUND1.loadMap(parallax_backgrounds);

        if(json.has("darkness"))
            LEVEL_DARKNESS = Math.min(json.getInt("darkness"), 255);

        JSONObject player = json.getJSONObject("player");
        this.player = (Player) GameUtils.spawn(GameUtils.decodeLocation(player.getJSONObject("location")), Player.class);

        for (int a = 0; a < json.getJSONArray("objects").length(); a++) {
            JSONObject object = json.getJSONArray("objects").getJSONObject(a);
            GameUtils.spawn(object);
//            GameUtils.createObject(GameUtils.decodeLocation(player.getJSONObject("location")), GameUtils.ObjectType.valueOf(object.getString("type").toUpperCase()).getObjectClass());
        }


    }

    @Override
    public void draw(Camera camera, Graphics g) {

        g.setColor(Color.BLUE);

        //Parallax Background
        double b = camera.getViewport().getLocation().getX() * 0.05;

        int cell = (int) Math.ceil(camera.getViewport().getLocation().getX() / camera.getViewport().getWidth()) - 1;
        if (b > camera.getViewport().getWidth()) {
            b = b - camera.getViewport().getWidth() * cell;
        }
//        System.out.println("B: " + b);
//        System.out.println("Cell: " + cell);
//        for (int i = 0; i < cell + 1; i++) {
//            for (Map.Entry<Integer, Image> entry : parallax_backgrounds.entrySet()) {
//                g.drawImage(entry.getValue(), (int) ((i * camera.getViewport().getWidth()) - (camera.getViewport().getLocation().getX() * entry.getKey())), -0, camera.getViewport().width, camera.getViewport().height, null);
//            }
//        }

        for (int a = 0; a < parallax_backgrounds.size() + 1; a++) {
            for (Map.Entry<Integer, Image> entry : parallax_backgrounds.entrySet()) {
                g.drawImage(entry.getValue(), (int) (-b * (entry.getKey()) + (camera.getViewport().getWidth() * a)), -0, (int) camera.getViewport().width, (int) camera.getViewport().height, null);
            }
        }


        //Draw objects
        g.setColor(Color.RED);
        for (GameObject object : camera.getAllObjects()) {
            object.draw(g, camera);
//            if (object instanceof Player) player = (Player) object;

        }


        //Darkness
        double offsetX=0;
        double offsetY=0;

        for (int x = 0; x != (camera.getViewport().width / TILE_SIZE)+1; x++) {
            for (int y = 0; y != (camera.getViewport().height / TILE_SIZE)+1; y++) {
                int brightness = LEVEL_DARKNESS;
                offsetX = camera.getViewport().getMinX() / TILE_SIZE;
                offsetY = camera.getViewport().getMinY() / TILE_SIZE;
                Location loc = new Location((x * TILE_SIZE + ((offsetX - Math.floor(offsetX)) * TILE_SIZE))-TILE_SIZE, (y * TILE_SIZE + ((offsetY - Math.floor(offsetY)) * TILE_SIZE))-TILE_SIZE);



                for (GameObject object : GameUtils.getObjects()) {
                    if (!(object instanceof Illuminant)) continue;
                    double calc_bright = GameUtils.distance(loc, new Location(object.getRelativeX(camera), object.getRelativeY(camera))) - ((Illuminant)object).getBrightness();
                    if (calc_bright < 0) calc_bright = 0;
                    brightness = Math.min((int) calc_bright, brightness);
                }
                g.setColor(new Color(0, 0, 0, brightness));
                g.fillRect((int) loc.getX(), (int) loc.getY(), TILE_SIZE, TILE_SIZE);

            }
        }
//        g.fillRect(0,0,camera.getViewport().width,(int)((offsetY - Math.floor(offsetY)) * TILE_SIZE));


        //Overlay Debug info
        if (GameUtils.debug()) {
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, camera.getViewport().width, camera.getViewport().height);
            g.setColor(Color.GREEN);

        }


    }

    @Override
    public void update(Camera cam) {


        if (player != null) {
            cam.getViewport().setLocation((int) (player.getLocation().getX() - (cam.getViewport().getWidth() / 2)), (int) (player.getLocation().getY() - (cam.getViewport().getHeight() - (cam.getViewport().getHeight() / 10))));
        }

        for (GameObject object : cam.getAllObjects())
            object.update(cam.getAllObjects());

    }
}
