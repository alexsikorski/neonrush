package game.neonrush.Managers;

import android.graphics.Canvas;

import game.neonrush.MainActivity;
import game.neonrush.Objects.ScoringObstacle;
import game.neonrush.Objects.Ship;
import game.neonrush.Utilities.Constants;

import java.util.ArrayList;

public class ScoObsManager {
    //for obstacles, greater index = lower on screen = great y value
    private ArrayList<ScoringObstacle> scoringObstacles;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;
    private int internalScore;
    // time
    private long startTime;
    private long initTime;

    public ScoObsManager(int obstacleGap, int obstacleHeight, int color) {
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis(); // sets both

        scoringObstacles = new ArrayList<>();

        generateScoringObstacles();
    }

    private void generateScoringObstacles() {
        int currentY = -5 * Constants.screenHeight / 4;
        while (currentY < 0) {
            // while hasn't been on screen yet.

            // START X POS
            int xStart = Constants.screenWidth;

            // starting position randomly generated, using playerGap to ensure that obstacle doesnt generate beyond screen.

            scoringObstacles.add(new ScoringObstacle(obstacleHeight, color, xStart, currentY));
            currentY += obstacleHeight + obstacleGap;
        }
    }


    public int playerCollide(Ship player) {
        for (ScoringObstacle ob : scoringObstacles) {
            if (ob.playerCollide(player)) {
                scoringObstacles.remove(ob);
                internalScore++;
                MainActivity.getSoundPlayer().playScoreSound(); // plays score sound
            }
        }
        return internalScore;
    }


    public void update() {
        int timePassed = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float) (Math.sqrt(1 + (startTime - initTime) / 2000.0)) * Constants.screenHeight / (10000.0f); // 10 seconds
        // speed will increase (every 2 seconds) as game goes on after initialising time

        for (ScoringObstacle ob : scoringObstacles) {
            ob.incrementY(speed * timePassed);
        }

        if (scoringObstacles.size() < 4) { // detects if one scoring object has been removed
            int xStart = Constants.screenWidth;
            scoringObstacles.add(0, new ScoringObstacle(obstacleHeight, color, xStart, (int) (scoringObstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap)));
        }

        if (scoringObstacles.get(scoringObstacles.size() - 1).getRectangle().top >= Constants.screenHeight) {
            int xStart = Constants.screenWidth;
            scoringObstacles.add(0, new ScoringObstacle(obstacleHeight, color, xStart, (int) (scoringObstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap)));
            scoringObstacles.remove(scoringObstacles.size() - 1);
        }
    }

    public void draw(Canvas canvas) {
        for (ScoringObstacle ob : scoringObstacles) {
            ob.draw(canvas);
            // draws the obstacles on canvas
        }
    }

}