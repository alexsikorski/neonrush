package game.neonrush.Managers;

import android.graphics.Canvas;

import game.neonrush.Objects.PickUp;
import game.neonrush.Objects.Ship;
import game.neonrush.Utilities.Constants;

import java.util.ArrayList;

public class PickUpManager {
    private ArrayList<PickUp> pickUps;
    private int pickUpHeight;

    public PickUpManager(int pickUpHeight) {
        this.pickUpHeight = pickUpHeight;

        pickUps = new ArrayList<>();

        //for testing pickups
        //generatePickUp();
    }

    public void generatePickUp() {
        if (pickUps.size() < 1) { // if only one pick up exists already...
            // randomly pick which pickup to generate
            int randomType = (int) (Math.random() * 3) + 1;
            boolean left;
            int currentX;
            int speed = (int) (Math.random() * 9) + 1; // determines speed of pickup

            // for the x axis
            // randomly choose to spawn on left side or right side
            double randomSide = Math.random();
            if (randomSide > 0.5) {
                currentX = -5; // start from left
                left = true;
            } else {
                currentX = Constants.screenWidth + 5; // start from right
                left = false;
            }

            // for the y axis
            int yStart = (int) (Math.random() * Constants.screenHeight);
            while (yStart < Constants.screenHeight / 5 || yStart > Constants.screenHeight / 5 * 4) {
                yStart = (int) (Math.random() * Constants.screenHeight);
                // this code makes sure the pickup spawns at a height between 1/5 of the screen
                // and 4/5 of the screen
            }
            pickUps.add(new PickUp(pickUpHeight, currentX, yStart, randomType, left, speed)); // ...add one
        }
    }

    public boolean playerCollide(Ship player) {
        for (PickUp ob : pickUps) {
            if (ob.playerCollide(player)) {
                if (ob.getType() == 1) {
                    player.goInvincible();
                }
                if (ob.getType() == 2) {
                    player.goFast();
                }
                if (ob.getType() == 3) {
                    player.goSmall();
                }
                pickUps.remove(ob);
                return true;
            }
        }
        return false;
    }

    public void update() {
        for (PickUp ob : pickUps) {
            ob.incrementX(); //
            if (ob.direction) { // if spawning left
                if (ob.getRectangle().left >= Constants.screenWidth) { // once pickup reaches the edge of screen...
                    ob.getRectangle().left = -75; // ... restart its position
                    ob.getRectangle().right = 0;
                }
            }
            if (!ob.direction){   // if spawning right
                if (ob.getRectangle().left <= -75) {
                    ob.getRectangle().left = Constants.screenWidth;
                    ob.getRectangle().right = Constants.screenWidth + 75;
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        for (PickUp ob : pickUps) {
            ob.draw(canvas);
        }
    }
}
