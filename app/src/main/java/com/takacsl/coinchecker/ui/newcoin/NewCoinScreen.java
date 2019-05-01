package com.takacsl.coinchecker.ui.newcoin;

public interface NewCoinScreen {
    void newCoin(String name, String symbol, Double price, Double marketcap);
    void showNetworkError(String errorMsg);
}
