package game.neonrush.Scenes;

import android.app.Application;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import game.neonrush.MainActivity;
import game.neonrush.Managers.ObsManager;
import game.neonrush.Managers.PickUpManager;
import game.neonrush.Managers.SceneManager;
import game.neonrush.Managers.ScoObsManager;
import game.neonrush.Objects.Ship;
import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.Scene;
import game.neonrush.Utilities.Vector2D;

public class GameplayScene extends Application implements Scene {

    public boolean gameOver = false; // end game screen
    private int deaths = 0;
    private Rect r = new Rect(); // for drawing on center
    private Ship player; // private instances of player
    private Point startingPoint; // point where ship always starts
    private Point flyToPoint; // point that ship should fly to
    private ObsManager obstacleManager; // private instance of obstacle manager
    private ScoObsManager scoObsManager; // private instance of scoring obstacle manager
    private PickUpManager pickUpManager;
    private boolean movingPlayer = false; // for player movement
    private boolean ifClicked = false;
    private boolean topScore = false;
    private boolean explosionOnce = false;
    private boolean highScoreOnce = false;
    private String retry = "Retry in 2 seconds...";
    private String timer = "";
    private boolean adRound;
    private boolean playedAd;
    private boolean loaded;


    private long gameOverTime; // time when game over starts
    private long pickUpTime;

    private int score; // scoring

    public GameplayScene() {
        player = new Ship(new RectF(100, 100, 200, 200), Constants.PLAYER,
                new Vector2D(Constants.screenWidth / 2, 3 * Constants.screenHeight / 4),
                new Vector2D(0, 0), new Vector2D(0, 0));

        startingPoint = new Point(Constants.screenWidth / 2, 3 * Constants.screenHeight / 4);
        flyToPoint = startingPoint;

        player.update(startingPoint); // player spawns half way through screen width and 3/4 way down on height

        obstacleManager = new ObsManager(400, 550, 75, Constants.OBSTACLES);
        scoObsManager = new ScoObsManager(595, 30, Constants.TEXT);
        pickUpManager = new PickUpManager(75);
    }

    public void playExplosion() { // method for playing explosion sound once where necessary
        if (!explosionOnce) {
            MainActivity.getSoundPlayer().playExplosionSound();
            explosionOnce = true; // then set to true because ran once
        }
    }

    public void playHighScore() { // method for playing explosion sound once where necessary
        if (!highScoreOnce) {
            MainActivity.getSoundPlayer().playHighScoreSound();
            highScoreOnce = true; // then set to true because ran once
        }
    }

    public void showInterstitial() { // loads ad
        if (MainActivity.getmInterstitialAd().isLoaded()) {
            loaded = true;
            MainActivity.getmInterstitialAd().show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            loaded = false;
        }
    }

    private RectF getPlayerRectAndExpand(RectF currentPlayer){ // for explosion effect we expand existing player rect
        float l = currentPlayer.left - 20;
        float t = currentPlayer.top - 20;
        float r = currentPlayer.right + 20;
        float b = currentPlayer.bottom + 20;
        return new RectF(l , t ,r , b);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void reset() {
            deaths += 1;
            //MainActivity.bannerAd = MainActivity.generateBannerAd();
            if (adRound) {
                MainActivity.generateNewAd();
            }
            topScore = false;
            player = new Ship(new RectF(100, 100, 200, 200), Constants.PLAYER,
                    new Vector2D(Constants.screenWidth / 2, 3 * Constants.screenHeight / 4),
                    new Vector2D(0, 0), new Vector2D(0, 0));
            startingPoint = new Point(Constants.screenWidth / 2, 3 * Constants.screenHeight / 4);
            flyToPoint = startingPoint;

            player.update(startingPoint); // resets position
            obstacleManager = new ObsManager(400, 550, 75, Constants.OBSTACLES);
            scoObsManager = new ScoObsManager(595, 30, Constants.TEXT);
            pickUpManager = new PickUpManager(75);
            movingPlayer = false;
            explosionOnce = false;
            highScoreOnce = false;
            retry = "Retry in 2 seconds...";
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int eX = (int) event.getX();
                int eY = (int) event.getY();
                ifClicked = player.getRectangle().contains(eX, eY);
                if (!gameOver && ifClicked)   // if game is running and if point lies within player rectangle
                    movingPlayer = true;
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    if (adRound){
                        if (Constants.free){
                            showInterstitial();
                        }
                        if (playedAd && !Constants.playingAd){
                            MainActivity.getSoundPlayer().playClickSound();
                            reset(); // resets game
                            score = 0; // resets score
                            gameOver = false;
                            playedAd = false;
                        }
                        if (!loaded){
                            MainActivity.getSoundPlayer().playClickSound();
                            reset(); // resets game
                            score = 0; // resets score
                            gameOver = false;
                            playedAd = false;
                        }
                    }
                    else{
                        MainActivity.getSoundPlayer().playClickSound();
                        reset(); // resets game
                        score = 0; // resets score
                        gameOver = false;
                        // after 2 seconds player can press to restart
                    }

                }

            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer) // if(!gameOver && movingPlayer)
                    flyToPoint.set((int) event.getX(), (int) event.getY());
                break;

            case MotionEvent.ACTION_UP: // checks every time we tap on screen
                movingPlayer = false; // else it will be true forever
                break;
        }
    }

    @Override
    public void update() {
        if(Constants.playingAd){
            playedAd = true;
        }
        if (deaths % 3 == 0) {
            adRound = true;
        }
        if (deaths % 3 != 0){
            adRound = false;
        }
        if (Constants.threadRunning) {

            if (score > MainActivity.getTopScore()) {    // if score is greater than saved top score
                topScore = true;    // set to true (so it displays on end screen)
                Constants.topScore = score; //sets top score
                MainActivity.setTopScore(); // sets preference score
            }
            if (!gameOver) {
                MainActivity.getMusicPlayer().start(); // starts music
                player.update(flyToPoint);
                obstacleManager.update();
                scoObsManager.update();
                pickUpManager.update();

                score = scoObsManager.playerCollide(player); // score is equal to internal score counter in scoring object manager
                if (score > 1 && score % 5 == 0) { // every time score hits a 5, 10, 15 etc... spawn a pickup (max one at a time)
                    if (!player.activeEffect) { // if player does not have an active pickup effect
                        pickUpManager.generatePickUp(); // generate pickup
                    }
                }
                if (pickUpManager.playerCollide(player)) {
                    MainActivity.getSoundPlayer().playPickUpSound();
                    pickUpTime = System.currentTimeMillis();

                }
                if (obstacleManager.playerCollide(player)) {
                    gameOver = true;
                    gameOverTime = System.currentTimeMillis();
                }
            }
            if (player.getActiveEffect()){
                if (System.currentTimeMillis() - pickUpTime >= 500){
                    timer = "3";
                }
                if (System.currentTimeMillis() - pickUpTime >= 1500){
                    timer = "2";
                }
                if (System.currentTimeMillis() - pickUpTime >= 2500){
                    timer = "1";
                }
                if (System.currentTimeMillis() - pickUpTime >= 3450){
                    timer = "";
                }
            }
            if (gameOver) {
                MainActivity.getMusicPlayer().pause();
            }
            if (gameOver && System.currentTimeMillis() - gameOverTime >= 1000) {
                // change string to 1 second left
                retry = "Retry in 1 second...";

            }
            if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                // change string to click here
                retry = "Tap to try again!";
            }

        } else {
            MainActivity.getMusicPlayer().pause();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Constants.BACKGROUND); // sets canvas to black
        player.draw(canvas);
        scoObsManager.draw(canvas);
        obstacleManager.draw(canvas);
        pickUpManager.draw(canvas);

        // paint
        Paint paint = new Paint();
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setColor(Constants.TEXT);
        paint.setTextSize(125);
        paint.setAntiAlias(true);
        paint.setDither(true);

        // for glow
        Paint paint2 = new Paint();
        paint2.set(paint);
        paint2.setStrokeWidth(30f);
        paint2.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));

        // for explosion glow
        Paint paint3 = new Paint();
        paint3.set(paint);
        paint3.setColor(Constants.EXPLOSION);
        paint3.setStrokeWidth(60f);
        paint3.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL));

        drawAboveText(canvas, paint, "" + score);
        drawAboveText(canvas, paint2, "" + score);

        // for timer
        paint.setTextSize(72);
        paint2.setTextSize(72);
        if (player.invincible){
            paint.setColor(Constants.PICKUP_1);
            paint2.setColor(Constants.PICKUP_1);
        }
        if (player.fast){
            paint.setColor(Constants.PICKUP_2);
            paint2.setColor(Constants.PICKUP_2);
        }
        if (player.small){
            paint.setColor(Constants.PICKUP_3);
            paint2.setColor(Constants.PICKUP_3);
        }
        drawBelowText3(canvas, paint, timer);
        drawBelowText3(canvas, paint2, timer);

        // the rest
        paint.setTextSize(125);
        paint2.setTextSize(125);
        paint.setColor(Constants.TEXT);
        paint2.setColor(Constants.TEXT);

        if (gameOver && !topScore) {
            playExplosion(); // play explosion sound
            canvas.drawRoundRect(getPlayerRectAndExpand(player.getRectangle()), 30, 30, paint3); // explosion effect
            paint.setTextSize(125);
            drawCenterText(canvas, paint, "GAME OVER");
            drawCenterText(canvas, paint2, "GAME OVER");
            paint.setTextSize(65);
            paint2.setTextSize(65);
            drawBelowText(canvas, paint, "YOUR TOP SCORE: " + MainActivity.getTopScore());
            drawBelowText(canvas, paint2, "YOUR TOP SCORE: " + MainActivity.getTopScore());
            paint.setTextSize(35);
            paint2.setTextSize(35);
            drawBelowText2(canvas, paint, retry);
            drawBelowText2(canvas, paint2, retry);
        }
        if (gameOver && topScore) {
            playExplosion(); // play explosion sound
            canvas.drawRoundRect(getPlayerRectAndExpand(player.getRectangle()), 30, 30, paint3); // explosion effect
            paint.setTextSize(125);
            drawCenterText(canvas, paint, "GAME OVER");
            drawCenterText(canvas, paint2, "GAME OVER");
            paint.setTextSize(35);
            paint2.setTextSize(35);
            drawBelowText2(canvas, paint, retry);
            drawBelowText2(canvas, paint2, retry);
            if (System.currentTimeMillis() - gameOverTime >= 1000) { // draw new top score after 1s
                playHighScore(); // play high score sound so that it doesn't merge with explosion sound
                paint.setTextSize(65);
                paint2.setTextSize(65);
                drawBelowText(canvas, paint, "NEW TOP SCORE: " + Constants.topScore + "!");
                drawBelowText(canvas, paint2, "NEW TOP SCORE: " + Constants.topScore + "!");
            }


        }
        if (!gameOver) {
            // TESTING
            // drawCenterText(canvas, paint, " NOT GAME OVER");
        }
        if (movingPlayer) {
            // TESTING
            // drawAboveText(canvas, paint, "MOVING PLAYER");
        }
        if (!movingPlayer) {
            // TESTING
            //drawAboveText(canvas, paint, "NOT MOVING PLAYER");
        }
    }

    // for "GAME OVER"
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    // for current score
    private void drawAboveText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (Constants.screenHeight / 10) * 1;
        canvas.drawText(text, x, y, paint);
    }

    // for top score/new topscore
    private void drawBelowText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (Constants.screenHeight / 10) * 4;
        canvas.drawText(text, x, y, paint);
    }

    // for retry message
    private void drawBelowText2(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (Constants.screenHeight / 10) * 6;
        canvas.drawText(text, x, y, paint);
    }

    // for timer
    private void drawBelowText3(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = (cWidth / 2f - r.width() / 2f - r.left);
        float y = (Constants.screenHeight / 10) * 9;
        canvas.drawText(text, x, y, paint);
    }


}
