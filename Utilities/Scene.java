package game.neonrush.Utilities;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    void update();

    void draw(Canvas canvas);

    void terminate(); // when scene ends call this method

    void receiveTouch(MotionEvent event);
}
