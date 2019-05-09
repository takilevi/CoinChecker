package com.takacsl.coinchecker.ui.newcoin;

import com.takacsl.coinchecker.dependencyinjection.Network;
import com.takacsl.coinchecker.interactor.NewCoinInteractor;
import com.takacsl.coinchecker.interactor.event.NewCoinEvent;
import com.takacsl.coinchecker.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class NewCoinPresenter extends Presenter<NewCoinScreen> {
    NewCoinInteractor newCoinInteractor;
    Executor networkExecutor;

    @Inject
    public NewCoinPresenter(@Network Executor networkExecutor, NewCoinInteractor newCoinInteractor) {
        this.newCoinInteractor = newCoinInteractor;
        this.networkExecutor = networkExecutor;
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

    public void addNewCoin(final String name, final String symbol, final String price, final String marketcap) {
        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                newCoinInteractor.newCoin(name,symbol,Double.parseDouble(price),Double.parseDouble(marketcap));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final NewCoinEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
            if (screen != null) {
                screen.showNetworkError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                screen.newCoin(event.getName(),event.getSymbol(),event.getPrice(),event.getMarketcap());
            }
        }
    }
}
