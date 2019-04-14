package com.example.coinchecker;

import android.app.Application;

import com.example.coinchecker.ui.UIModule;

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