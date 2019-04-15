package com.example.coinchecker;

import com.example.coinchecker.interactor.CoinInteractor;
import com.example.coinchecker.interactor.NewCoinInteractor;
import com.example.coinchecker.network.NetworkModule;
import com.example.coinchecker.ui.UIModule;
import com.example.coinchecker.ui.coins.CoinsFragment;
import com.example.coinchecker.ui.coins.CoinsPresenter;
import com.example.coinchecker.ui.main.MainActivity;
import com.example.coinchecker.ui.newcoin.NewCoinPresenter;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {UIModule.class, NetworkModule.class})
public interface CoinCheckerApplicationComponent {
    void inject(MainActivity mainActivity);

    void inject(CoinsFragment coinsFragment);

    void inject(CoinInteractor coinInteractor);

    void inject(CoinsPresenter coinsPresenter);

    void inject(NewCoinPresenter coinsPresenter);

    void inject(NewCoinInteractor coinInteractor);

}
