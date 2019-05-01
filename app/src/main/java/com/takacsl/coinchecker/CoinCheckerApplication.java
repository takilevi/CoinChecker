package com.takacsl.coinchecker;

import android.app.Application;

import com.takacsl.coinchecker.ui.UIModule;

public class CoinCheckerApplication  extends Application {

    public static CoinCheckerApplicationComponent injector;


    @Override
    public void onCreate() {
        super.onCreate();

        injector =
                DaggerCoinCheckerApplicationComponent.builder().
                        uIModule(
                                new UIModule(this)
                        ).build();
    }

}
