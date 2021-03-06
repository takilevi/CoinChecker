package com.takacsl.coinchecker.test;

import android.arch.persistence.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.takacsl.coinchecker.DaggerTestComponent;
import com.takacsl.coinchecker.room.dao.CoinDao;
import com.takacsl.coinchecker.room.database.CoinDatabase;
import com.takacsl.coinchecker.ui.newcoin.NewCoinPresenter;
import com.takacsl.coinchecker.ui.newcoin.NewCoinScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import javax.inject.Inject;

import static com.takacsl.coinchecker.TestHelper.setTestInjector;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class NewCoinTest {
    private CoinDao coinDao;
    private CoinDatabase coinDatabase;
    @Inject
    NewCoinPresenter newCoinPresenter;
    private NewCoinScreen newCoinScreen;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        coinDatabase = Room.databaseBuilder(context, CoinDatabase.class, "db_coin").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        coinDao = coinDatabase.daoAccess();
        DaggerTestComponent injector = setTestInjector();
        injector.inject(this);
        newCoinScreen = mock(NewCoinScreen.class);
        newCoinPresenter.attachScreen(newCoinScreen);
    }

    @After
    public void closeDb() throws IOException {
        coinDatabase.close();
    }

    @Test
    public void testNewCoinSaveLogic() throws  Exception {
        newCoinPresenter.addNewCoin("triggered", "T","100.1","212121.21");
        verify(newCoinScreen).newCoin("triggered", "T",100.1,212121.21);
    }
}
