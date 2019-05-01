package com.takacsl.coinchecker;

import com.takacsl.coinchecker.interactor.CoinInteractor;
import com.takacsl.coinchecker.interactor.NewCoinInteractor;
import com.takacsl.coinchecker.network.NetworkModule;
import com.takacsl.coinchecker.ui.UIModule;
import com.takacsl.coinchecker.ui.coins.CoinsFragment;
import com.takacsl.coinchecker.ui.coins.CoinsPresenter;
import com.takacsl.coinchecker.ui.main.MainActivity;
import com.takacsl.coinchecker.ui.newcoin.NewCoinActivity;
import com.takacsl.coinchecker.ui.newcoin.NewCoinPresenter;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {UIModule.class, NetworkModule.class})
public interface CoinCheckerApplicationComponent {
    void inject(MainActivity mainActivity);

    void inject(CoinsFragment coinsFragment);

    void inject(CoinInteractor coinInteractor);

    void inject(CoinsPresenter coinsPresenter);

    void inject(NewCoinActivity newCoinActivity);

    void inject(NewCoinPresenter newCoinPresenter);

    void inject(NewCoinInteractor newCoinInteractor);

}
