package com.example.coinchecker.interactor;

import com.example.coinchecker.CoinCheckerApplication;

import javax.inject.Inject;

public class NewCoinInteractor {

    @Inject NewCoinInteractor(){
        CoinCheckerApplication.injector.inject(this);
    }
}
