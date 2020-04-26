package game.neonrush;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import game.neonrush.Utilities.Constants;

public class MainThread extends Thread {

    private static Canvas canvas; // playing field
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    // FPS
    private int targetFPS = Constants.DELAY;
    private double averageFPS;


    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }


    @Override
    public void run() {
        Constants.threadRunning = true;
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = Constants.DT;

        while (running) {

            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                sleep(waitTime);
            } catch (Exception e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }

        }

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
        Constants.threadRunning = false;
    }
}
