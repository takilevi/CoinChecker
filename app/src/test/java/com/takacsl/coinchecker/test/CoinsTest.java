package com.takacsl.coinchecker.test;

import com.takacsl.coinchecker.DaggerTestComponent;
import com.takacsl.coinchecker.ui.coins.CoinsPresenter;
import com.takacsl.coinchecker.ui.coins.CoinsScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static com.takacsl.coinchecker.TestHelper.setTestInjector;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class CoinsTest {
    @Inject
    CoinsPresenter coinsPresenter;
    private CoinsScreen coinsScreen;

    @Before
    public void setup() {
        DaggerTestComponent injector = setTestInjector();
        injector.inject(this);
        coinsScreen = mock(CoinsScreen.class);
        coinsPresenter.attachScreen(coinsScreen);
    }

    @Test
    public void testCoins() {
        coinsPresenter.refreshCoins();

        ArgumentCaptor<List> coinsCaptor = ArgumentCaptor.forClass(List.class);
        verify(coinsScreen).showCoins(coinsCaptor.capture());
        assertTrue(coinsCaptor.getValue().size() > 0);
    }

    @After
    public void tearDown() {
        coinsPresenter.detachScreen();
    }
}
