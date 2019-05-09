package com.takacsl.coinchecker.ui.coins;

import com.takacsl.coinchecker.dependencyinjection.Network;
import com.takacsl.coinchecker.interactor.CoinInteractor;
import com.takacsl.coinchecker.interactor.event.GetCoinsEvent;
import com.takacsl.coinchecker.ui.Presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class CoinsPresenter extends Presenter<CoinsScreen> {
    Executor networkExecutor;
    CoinInteractor coinInteractor;

    @Inject
    public CoinsPresenter(@Network Executor networkExecutor, CoinInteractor coinInteractor) {
        this.networkExecutor = networkExecutor;
        this.coinInteractor = coinInteractor;
    }

    @Override
    public void attachScreen(CoinsScreen screen) {
        super.attachScreen(screen);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachScreen() {
        EventBus.getDefault().unregister(this);
        super.detachScreen();
    }

    public void refreshCoins() {
        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                coinInteractor.getCoins();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final GetCoinsEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
            if (screen != null) {
                screen.showNetworkError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                screen.showCoins(event.getCoins());
            }
        }
    }
}
