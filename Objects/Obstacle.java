package game.neonrush.Objects;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.GameObject;

public class Obstacle implements GameObject {

    private RectF rectangle;
    private RectF rectangle2;
    private int color;


    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        // l t r b
        rectangle = new RectF(1, startY, startX, startY + rectHeight);
        rectangle2 = new RectF(startX + playerGap, startY, Constants.screenWidth, startY + rectHeight);
    }

    public RectF getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;

        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public boolean playerCollide(Ship player) {
        return RectF.intersects(rectangle, player.getRectangle()) || RectF.intersects(rectangle2, player.getRectangle());
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

        // draw obstacles
        canvas.drawRoundRect(rectangle, 30, 30, paint);
        canvas.drawRoundRect(rectangle2, 30, 30, paint);

        canvas.drawRoundRect(rectangle, 30, 30, paint2);
        canvas.drawRoundRect(rectangle2, 30, 30, paint2);
    }

    @Override
    public void update() {

    }

}
