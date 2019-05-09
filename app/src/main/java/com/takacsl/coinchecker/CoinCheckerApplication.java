package com.takacsl.coinchecker;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.takacsl.coinchecker.ui.UIModule;

public class CoinCheckerApplication  extends Application {

    public static CoinCheckerApplicationComponent injector;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;


    @Override
    public void onCreate() {
        super.onCreate();
        sAnalytics = GoogleAnalytics.getInstance(this);

        injector =
                DaggerCoinCheckerApplicationComponent.builder().
                        uIModule(
                                new UIModule(this)
                        ).build();
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

}
