package game.neonrush.Utilities;

import android.graphics.Color;

public class Constants {
    // these finals determine the speed of the game.
    public static final int DELAY = 60; //milliseconds
    public static final long DT = 500 / DELAY; // in seconds
    // colours
    public static final int BACKGROUND = Color.rgb(0, 0, 0);
    public static final int OBSTACLES = Color.rgb(102, 102, 153);
    public static final int PLAYER = Color.rgb(51, 204, 255);
    public static final int TEXT = Color.rgb(255, 255, 255);
    public static final int PICKUP_1 = Color.rgb(124, 252, 0);
    public static final int PICKUP_2 = Color.rgb(170, 0, 0);
    public static final int PICKUP_3 = Color.rgb(255, 204, 0);
    public static final int EXPLOSION = Color.rgb(255, 255, 0);
    // first run
    public static boolean firstRun = false;
    // screen size
    public static int screenWidth;
    public static int screenHeight;
    // session top score
    public static int topScore = 0;
    // if surface destroyed
    public static boolean threadRunning;
    // paid or free version
    public static boolean free = true;
    // playing ad?
    public static boolean playingAd;
    // pickup length
    public static int pickupTime = 3500;



}




