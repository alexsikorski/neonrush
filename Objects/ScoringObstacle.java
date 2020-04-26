package game.neonrush.Objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import game.neonrush.Utilities.GameObject;

public class ScoringObstacle implements GameObject {

    private RectF rectangle;
    private int color;


    public ScoringObstacle(int rectHeight, int color, int startX, int startY) {
        this.color = color;
        // l t r b
        rectangle = new RectF(0, startY, startX, startY + rectHeight);

    }

    public RectF getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }


    public boolean playerCollide(Ship player) {
        return RectF.intersects(rectangle, player.getRectangle()); // intersect
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);

        // draw obstacles
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }

}

