package com.example.coinchecker.ui.main;

import com.example.coinchecker.ui.Presenter;

import javax.inject.Inject;

public class MainPresenter extends Presenter<MainScreen> {

    @Inject
    public MainPresenter() {
    }

    @Override
    public void attachScreen(MainScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void showCoinsList() {
        screen.showCoins();
    }
}
