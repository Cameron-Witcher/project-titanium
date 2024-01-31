package com.quickscythe.silver;

import com.quickscythe.silver.game.Window;

public class Main {

    public static Window window;

    public static void main(String[] args){
        window = new Window();
    }

    public static Window getWindow(){
        return window;
    }
}
