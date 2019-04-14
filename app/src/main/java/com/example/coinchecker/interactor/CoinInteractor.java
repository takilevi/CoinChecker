package com.example.coinchecker.interactor;

import com.example.coinchecker.CoinCheckerApplication;
import com.example.coinchecker.interactor.event.GetCoinsEvent;
import com.example.coinchecker.model.CoinsResult;
import com.example.coinchecker.network.CoinApi;
import com.example.coinchecker.network.NetworkConfig;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class CoinInteractor {
    CoinApi coinApi;

    @Inject
    public CoinInteractor(CoinApi coinApi) {
        this.coinApi = coinApi;
        CoinCheckerApplication.injector.inject(this);
    }

    public void getCoins() {

        GetCoinsEvent event = new GetCoinsEvent();
        try {
            Call<CoinsResult> coinsResultCall = coinApi.getCoins(NetworkConfig.API_TOKEN,1,100,"USD");

            Response<CoinsResult> response = coinsResultCall.execute();
            if (response.code() != 200) {
                throw new Exception("Result code is not 200");
            }
            event.setCode(response.code());
            event.setCoins(response.body().getCoins());
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }
}
