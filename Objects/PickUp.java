package game.neonrush.Objects;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.GameObject;

public class PickUp implements GameObject {

    public boolean direction;
    public int speed;
    private RectF rectangle;
    private int color;
    private int type;

    public PickUp(int rectHeight, int startX, int startY, int type, boolean direction, int speed) {

        this.type = type;
        this.direction = direction;
        this.speed = speed;

        // l t r b
        if (direction) {
            rectangle = new RectF(-75, startY, startX, startY + rectHeight);
        } else {
            rectangle = new RectF(startX, startY, Constants.screenWidth + 75, startY + rectHeight);
        }


        if (type == 1) {
            this.color = Constants.PICKUP_1;
        }
        if (type == 2) {
            this.color = Constants.PICKUP_2;
        }
        if (type == 3) {
            this.color = Constants.PICKUP_3;
        }

    }

    public RectF getRectangle() {
        return rectangle;
    }

    public int getType() {
        return type;
    }

    public void incrementX() {
        if (direction) { // if left = true
            // increment x because pickup go left to right
            rectangle.right += speed;
            rectangle.left += speed;
        } else {
            // decrement x because pickup goes right to left
            rectangle.right -= speed;
            rectangle.left -= speed;
        }

    }

    public boolean playerCollide(Ship player) {
        return RectF.intersects(rectangle, player.getRectangle());
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
}
