package com.takacsl.coinchecker.test;

import com.takacsl.coinchecker.DaggerTestComponent;
import com.takacsl.coinchecker.ui.main.MainPresenter;
import com.takacsl.coinchecker.ui.main.MainScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import javax.inject.Inject;

import static com.takacsl.coinchecker.TestHelper.setTestInjector;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MainScreenTest {
    @Inject
    MainPresenter mainPresenter;

    private MainScreen mainScreen;

    @Before
    public void setup() {
        DaggerTestComponent injector = setTestInjector();
        injector.inject(this);
        mainScreen = mock(MainScreen.class);
        mainPresenter.attachScreen(mainScreen);
    }

    @Test
    public void testNavigationToCoins() {
        mainPresenter.showCoinsList();
        verify(mainScreen).showCoins();
    }


    @After
    public void tearDown() {
        mainPresenter.detachScreen();
    }
}
