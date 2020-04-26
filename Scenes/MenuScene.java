package game.neonrush.Scenes;

import android.app.Application;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import game.neonrush.MainActivity;
import game.neonrush.Managers.SceneManager;
import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.Scene;

public class MenuScene extends Application implements Scene {

    private Rect r = new Rect();


    public MenuScene() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Constants.BACKGROUND);
        Paint paint = new Paint();
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setColor(Constants.TEXT);
        //paint.setTextSize(125);
        //+drawAboveText(canvas, paint, "SPACE DASH");
        Paint paint2 = new Paint();
        paint2.set(paint);
        paint2.setStrokeWidth(30f);
        paint2.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

        paint.setTextSize(50);
        paint2.setTextSize(50);
        drawAboveText1(canvas, paint, "Your top score: " + MainActivity.getTopScore());
        drawAboveText1(canvas, paint2, "Your top score: " + MainActivity.getTopScore());
        paint.setTextSize(75);
        paint2.setTextSize(75);
        drawAboveText2(canvas, paint, "Touch to start...");
        drawAboveText2(canvas, paint2, "Touch to start...");
        canvas.drawBitmap(MainActivity.controlsBmp, (Constants.screenWidth - MainActivity.controlsBmp.getWidth()) / 2, ((Constants.screenHeight - MainActivity.controlsBmp.getHeight()) / 2) - (Constants.screenHeight / 10) * 1, paint);
        canvas.drawBitmap(MainActivity.logoBmp, (Constants.screenWidth - MainActivity.logoBmp.getWidth()) / 2, ((Constants.screenHeight - MainActivity.logoBmp.getHeight()) / 2) - (Constants.screenHeight / 10) *3, paint);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 1;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MainActivity.getSoundPlayer().playClickSound(); // play click sound
                Constants.firstRun = true; // once touched, first run is activated
                break;
        }
    }


    // for "Your top score"
    private void drawAboveText1(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (Constants.screenHeight / 10 * 7);
        canvas.drawText(text, x, y, paint);
    }

    // for "Touch to start"
    private void drawAboveText2(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (Constants.screenHeight / 10 * 8);
        canvas.drawText(text, x, y, paint);
    }

}
