package game.neonrush;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.neonrush.Managers.SceneManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread; // private instance of thread
    private SceneManager manager;
    // private Logo sprite; // private instance of sprite
    private boolean firstTimeRunning = true;


    public GameView(Context context) {   // constructor
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);


    }


    // overriding methods from superclass
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread.getState() == Thread.State.TERMINATED) { // check if thread is terminated
            thread = new MainThread(getHolder(), this);
        }
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;   // to stop thread, will take multiple attempts
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override // for touch events
    public boolean onTouchEvent(MotionEvent event) {
        manager.receiveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {
        manager.update();
    }

    @Override
    public void draw(Canvas canvas) {    // for painting graphics
        super.draw(canvas);
        manager.draw(canvas);

    }
}
