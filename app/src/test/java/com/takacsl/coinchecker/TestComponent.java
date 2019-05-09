package com.takacsl.coinchecker;

import com.takacsl.coinchecker.mock.MockNetworkModule;
import com.takacsl.coinchecker.test.CoinsTest;
import com.takacsl.coinchecker.test.InsertionLogicTest;
import com.takacsl.coinchecker.test.MainScreenTest;
import com.takacsl.coinchecker.test.NewCoinTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MockNetworkModule.class, TestModule.class})
public interface TestComponent extends CoinCheckerApplicationComponent {
    void inject(MainScreenTest mainTest);
    void inject(CoinsTest coinsTest);
    void inject(NewCoinTest newCoinTest);
    void inject(InsertionLogicTest insertionLogicTest);

}
