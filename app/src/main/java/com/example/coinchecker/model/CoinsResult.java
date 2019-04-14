package com.example.coinchecker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinsResult {
    @SerializedName("coins")
    @Expose
    private List<CoinData> coins;

    public List<CoinData> getCoins() {
        return coins;
    }

    public void setCoins(List<CoinData> coins) {
        this.coins = coins;
    }
}
