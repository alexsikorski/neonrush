package game.neonrush.Objects;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.GameObject;
import game.neonrush.Utilities.Vector2D;

public class Ship implements GameObject {

    public boolean activeEffect = false; // determines if a pickup is active on the player
    private RectF rectangle;
    private int color;
    public boolean invincible = false;
    public boolean fast = false;
    public boolean small = false;
    private long createdMillis1;
    private long createdMillis2;
    private long createdMillis3;

    private Vector2D location;
    private Vector2D velocity;
    private Vector2D acceleration;


    public Ship(RectF rectangle, int color, Vector2D location, Vector2D velocity, Vector2D acceleration) {
        this.rectangle = rectangle;
        this.color = color;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public boolean getActiveEffect() {return activeEffect;}

    public RectF getRectangle() { // used to detect collisions
        return rectangle;
    }

    public boolean getInvincible() {
        return invincible;
    }

    public void goInvincible() {
        invincible = true;
        activeEffect = true;
        createdMillis1 = System.currentTimeMillis();
    }

    public void goFast() {
        fast = true;
        activeEffect = true;
        createdMillis2 = System.currentTimeMillis();
    }

    public void goSmall() {
        small = true;
        activeEffect = true;
        createdMillis3 = System.currentTimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        Paint paint2 = new Paint();
        paint2.set(paint);
        paint2.setStrokeWidth(10f);
        paint2.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

        canvas.drawRoundRect(rectangle, 30, 30, paint);
        canvas.drawRoundRect(rectangle, 30, 30, paint2);

    }

    @Override
    public void update() {

    }

    // method for moving rectangle
    public void update(Point tapPoint) {
        Vector2D mouse = new Vector2D(tapPoint.x, tapPoint.y);
        mouse.sub(location);
        if (invincible) {
            color = Constants.PICKUP_1;
            if (System.currentTimeMillis() - createdMillis1 >= Constants.pickupTime) {
                color = Constants.PLAYER; // set colour back to normal and invincibility off after given time
                invincible = false;
                activeEffect = false;
            }
        }
        if (fast) {
            color = Constants.PICKUP_2;
            mouse.setMag(1.0f);
            if (System.currentTimeMillis() - createdMillis2 >= Constants.pickupTime) {
                color = Constants.PLAYER;
                fast = false;
                activeEffect = false;
            }
        }
        if (!fast) {
            mouse.setMag(0.5f);
        }
        // l t r b, sets rectangle to centre
        acceleration = mouse;
        velocity.add(acceleration);
        location.add(velocity);

        if (fast) {
            velocity.limit(15);
        }
        if (!fast) {
            velocity.limit(10);
        }

        if (small) {
            rectangle.left = 50;
            rectangle.top = 50;
            rectangle.right = 100;
            rectangle.bottom = 100;
            color = Constants.PICKUP_3;
            if (System.currentTimeMillis() - createdMillis3 >= Constants.pickupTime) {
                color = Constants.PLAYER;
                small = false;
                activeEffect = false;
            }
        }
        if (!small) {
            rectangle.left = 100;
            rectangle.top = 100;
            rectangle.right = 200;
            rectangle.bottom = 200;
        }
        rectangle.set(location.getX() - rectangle.width() / 2, location.getY() - rectangle.height() / 2,
                location.getX() + rectangle.width() / 2, location.getY() + rectangle.height() / 2);

    }
}
