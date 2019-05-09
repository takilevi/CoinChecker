package com.takacsl.coinchecker.ui.coins;

import com.takacsl.coinchecker.model.Coin;

import java.util.List;

public interface CoinsScreen {
    void showCoins(List<Coin> coins);

    void showNetworkError(String errorMsg);
}
