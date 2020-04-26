package game.neonrush;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import game.neonrush.Utilities.Constants;
import game.neonrush.Utilities.SoundPlayer;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends Activity {

    public static Context context;
    public static SoundPlayer sound;
    public static MediaPlayer backgroundMusic;
    public static Bitmap logoBmp;
    public static Bitmap controlsBmp;
    public static InterstitialAd mInterstitialAd;
    private static SharedPreferences myPreferences;
    private static SharedPreferences.Editor myEditor;
    private static AdView adView;
    public static RelativeLayout bannerAd;

    // set/get preferences
    public static void setTopScore() {
        myEditor.putInt("USER_TOPSCORE", Constants.topScore);
        myEditor.commit();
    }

    public static int getTopScore() {
        return myPreferences.getInt("USER_TOPSCORE", 0);
    }

    public static SoundPlayer getSoundPlayer() {
        return sound;
    }

    public static MediaPlayer getMusicPlayer() {
        return backgroundMusic;
    }

    public static InterstitialAd getmInterstitialAd() { return mInterstitialAd; }

    public static void generateNewAd(){
        mInterstitialAd = new InterstitialAd(MainActivity.context);

        // TEST AD
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // AdListener
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Constants.playingAd = true;
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Constants.playingAd = false;
            }

            @Override
            public void onAdClosed() {
                Constants.playingAd = false;
            }

        });
    }
    public static RelativeLayout generateBannerAd(){
        // set view for ad
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1054654897700599/4108607498");

        // TEST ad
        //adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        RelativeLayout layout = new RelativeLayout(context); // creates new layout
        layout.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)); // sets it to the same as its parent

        // loads ads
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        View myView = new GameView(context);

        // essentially layers the ad ontop of the game view
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(myView);
        layout.addView(adView, adParams);

        return layout;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // disable screen orientation sensor
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // set context var
        context = getApplicationContext();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // gets screen sizes and applies to constants class
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.screenWidth = dm.widthPixels;
        Constants.screenHeight = dm.heightPixels;

        // preferences
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        myEditor = myPreferences.edit();

        int topScore = myPreferences.getInt("USER_TOPSCORE", 0);
        System.out.println(topScore);

        // view content
        setContentView(new GameView(this));

        // sound
        sound = new SoundPlayer(this);

        // song player (SoundPlayer requires more programming for loading up big sound files)
        backgroundMusic = MediaPlayer.create(this, R.raw.music_1);

        if (Constants.free){
            // load ads
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            // intersitial ad
            generateNewAd();

            // set view for banner and ad and generate ad
            //bannerAd = generateBannerAd();
            //setContentView(bannerAd);
        }
        // for logo
        logoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.neonrush);
        // for controls
        controlsBmp = BitmapFactory.decodeResource(getResources(), R.drawable.controls);
    }
}
