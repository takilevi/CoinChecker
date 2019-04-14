package com.example.coinchecker.ui.coins;

import com.example.coinchecker.model.Coin;

import java.util.List;

public interface CoinsScreen {
    void showCoins(List<Coin> coins);

    void showNetworkError(String errorMsg);
}
