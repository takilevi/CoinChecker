package com.takacsl.coinchecker.interactor;

import com.takacsl.coinchecker.CoinCheckerApplication;
import com.takacsl.coinchecker.interactor.event.NewCoinEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class NewCoinInteractor {

    @Inject NewCoinInteractor(){
        CoinCheckerApplication.injector.inject(this);
    }

    public void newCoin(String name, String symbol, Double price, Double marketcap) {

        NewCoinEvent event = new NewCoinEvent(0, name, symbol, price, marketcap, null);
        try {
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }
}
