package game.neonrush.Utilities;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import game.neonrush.R;

public class SoundPlayer {
    private static SoundPool sounds;

    // sounds
    private static int clickSound;
    private static int explosionSound;
    private static int scoreSound;
    private static int highScore;
    private static int pickUp;

    public SoundPlayer(Context context) {
        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        clickSound = sounds.load(context, R.raw.click, 1);
        explosionSound = sounds.load(context, R.raw.explosion, 1);
        scoreSound = sounds.load(context, R.raw.score, 1);
        highScore = sounds.load(context, R.raw.high_score, 1);
        pickUp = sounds.load(context, R.raw.pickup, 1);
    }

    public void playClickSound() {
        sounds.play(clickSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playExplosionSound() {
        sounds.play(explosionSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playScoreSound() {
        sounds.play(scoreSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHighScoreSound() {
        sounds.play(highScore, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playPickUpSound() {
        sounds.play(pickUp, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
