package game.neonrush.Managers;

import android.graphics.Canvas;

import game.neonrush.Objects.Obstacle;
import game.neonrush.Objects.Ship;
import game.neonrush.Utilities.Constants;

import java.util.ArrayList;

public class ObsManager {
    //for obstacles, greater index = lower on screen = great y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    // time
    private long startTime;
    private long initTime;

    public ObsManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;


        startTime = initTime = System.currentTimeMillis(); // sets both

        obstacles = new ArrayList<>();

        generateObstacles();
    }

    private void generateObstacles() {
        int currentY = -5 * Constants.screenHeight / 4;
        while (currentY < 0) {
            // while hasn't been on screen yet.

            // START X POS
            int xStart = (int) (Math.random() * (Constants.screenWidth - playerGap));

            // starting position randomly generated, using playerGap to ensure that obstacle doesnt generate beyond screen.

            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currentY, playerGap));
            currentY += obstacleHeight + obstacleGap;
        }
    }

    public boolean playerCollide(Ship player) {
        for (Obstacle ob : obstacles) {
            if (ob.playerCollide(player) && !player.getInvincible())
                return true;
        }
        return false;
    }

    public void update() {
        int timePassed = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime) / 2000.0)) * Constants.screenHeight / (10000.0f); // 10 seconds
        // speed will increase (every 2 seconds) as game goes on after initialising time

        for (Obstacle ob : obstacles) {
            ob.incrementY(speed * timePassed);
        }

        if (obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.screenHeight) {
            int xStart = (int) (Math.random() * (Constants.screenWidth - playerGap));  // generates new xStart
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, (int) (obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap), playerGap));
            obstacles.remove(obstacles.size() - 1);
        }
    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles) {
            ob.draw(canvas);
            // draws the obstacles on canvas
        }
    }

}