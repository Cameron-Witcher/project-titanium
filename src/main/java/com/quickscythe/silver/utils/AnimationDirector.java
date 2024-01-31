package com.quickscythe.silver.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

public class AnimationDirector {

    BufferedImage sprite_sheet;
    int width;
    int height;

    int current_animation = 0;
    int current_frame = 0;


    int cdata[] = new int[3];

    long last_frame_update = 0;



    public AnimationDirector(BufferedImage sprite_sheet, Dimension object_size){
        this(sprite_sheet,object_size.width,object_size.height);
    }
    public AnimationDirector(BufferedImage sprite_sheet, Dimension object_size, long animation_speed){
        this(sprite_sheet,object_size.width,object_size.height);
    }



    public AnimationDirector(BufferedImage sprite_sheet, int width, int height){
        this.sprite_sheet = sprite_sheet;
        this.width = width;
        this.height = height;
        setCData();
    }

    public void setAnimation(int animation_id){
        if(current_animation!=animation_id) {
            this.current_animation = animation_id;
            this.last_frame_update = new Date().getTime();
            setCData();
        }
    }

    private void setCData() {
        int rgb = sprite_sheet.getRGB(current_animation,sprite_sheet.getHeight()-1);
        cdata[0] = (rgb >> 16) & 0x000000FF;
        cdata[1] = (rgb >>8 ) & 0x000000FF;
        cdata[2] = (rgb) & 0x000000FF;
    }

    public BufferedImage getFrame(){
        if(new Date().getTime() - last_frame_update >= cdata[1]){
            last_frame_update = new Date().getTime();
            current_frame = current_frame+1;
            if(current_frame>=cdata[0]){
                current_frame=0;
            }

        }
        return sprite_sheet.getSubimage(current_frame*width,current_animation*height,width,height);

    }



}
