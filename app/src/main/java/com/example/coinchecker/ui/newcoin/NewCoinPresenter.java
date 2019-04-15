package com.example.coinchecker.ui.newcoin;

import com.example.coinchecker.interactor.NewCoinInteractor;
import com.example.coinchecker.ui.Presenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class NewCoinPresenter extends Presenter<NewCoinScreen> {
    NewCoinInteractor newCoinInteractor;

    @Inject
    public NewCoinPresenter(NewCoinInteractor newCoinInteractor) {
        this.newCoinInteractor = newCoinInteractor;
    }

    @Override
    public void attachScreen(NewCoinScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void addNewCoin() {
        screen.newCoin();
    }
}
