package game.neonrush.Managers;

import android.graphics.Canvas;
import android.view.MotionEvent;

import game.neonrush.Scenes.GameplayScene;
import game.neonrush.Scenes.MenuScene;
import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.Scene;

import java.util.ArrayList;

public class SceneManager {
    public static int ACTIVE_SCENE;
    private ArrayList<Scene> scenes = new ArrayList<>();

    public SceneManager() {
        ACTIVE_SCENE = 0;
        scenes.add(new MenuScene());
    }

    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);

    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
        if (Constants.firstRun) {    // if first run is true..
            scenes.add(new GameplayScene()); // add gameplay scene
            ACTIVE_SCENE = 1; // set active scene to gameplay scene
        }
    }

    public static void returnToMenu() {
        ACTIVE_SCENE = 0;
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }
}
