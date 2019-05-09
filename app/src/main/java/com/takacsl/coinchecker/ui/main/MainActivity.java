package com.takacsl.coinchecker.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.takacsl.coinchecker.CoinCheckerApplication;
import com.takacsl.coinchecker.R;
import com.takacsl.coinchecker.ui.coins.CoinsActivity;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainScreen {

    /*@Inject
    MainPresenter mainPresenter;*/
    protected Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        splashOpened();

        setContentView(R.layout.activity_splash);
        // Obtain the shared Tracker instance.
        CoinCheckerApplication application = (CoinCheckerApplication) getApplication();
        mTracker = application.getDefaultTracker();


        //if you do not want to use splash screen, then enable injecting
        showCoins();

    }
    @Override
    protected void onResume() {
        super.onResume();
        String name = MainActivity.class.getName();
        Log.i("MainActivity", "Setting screen name: " + name);
        mTracker.setScreenName("Splash~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        mainPresenter.attachScreen(this);
        mainPresenter.showCoinsList();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.detachScreen();
    }*/

    @Override
    public void showCoins() {
        final long splashScreenDuration = getSplashScreenDuration();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("NavigateToCoinsScreen")
                .build());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, CoinsActivity.class);
                startActivity(intent);
                finish();
            }
        },splashScreenDuration);

        splashLeaved();
    }

    private long getSplashScreenDuration() {return 2000L;}

    private void splashOpened(){
        Crashlytics.setString("EventAction","Splash screen opened");
    }

    private void splashLeaved(){
        Crashlytics.setString("EventAction","Splash screen leaved");
    }
}
