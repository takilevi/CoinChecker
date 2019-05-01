package com.takacsl.coinchecker.interactor.event;

import com.takacsl.coinchecker.model.Coin;

import java.util.List;

public class GetCoinsEvent {
    public GetCoinsEvent(int code, List<Coin> coins, Throwable throwable) {
        this.code = code;
        this.coins = coins;
        this.throwable = throwable;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;
    private List<Coin> coins;
    private Throwable throwable;

    public GetCoinsEvent(){}


    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
