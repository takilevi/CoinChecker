package com.takacsl.coinchecker.test;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.takacsl.coinchecker.DaggerTestComponent;
import com.takacsl.coinchecker.model.Coin;
import com.takacsl.coinchecker.room.dao.CoinDao;
import com.takacsl.coinchecker.room.database.CoinDatabase;
import com.takacsl.coinchecker.ui.newcoin.NewCoinActivity;
import com.takacsl.coinchecker.ui.newcoin.NewCoinPresenter;
import com.takacsl.coinchecker.ui.newcoin.NewCoinScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.io.IOException;

import javax.inject.Inject;

import androidx.test.core.app.ApplicationProvider;

import static com.takacsl.coinchecker.TestHelper.setTestInjector;
import static org.junit.Assert.assertEquals;
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
    public void testInsertionLogic() throws Exception {
        final Coin coin = new Coin(1,"test coin","TC",100.0,10000.23);
        coinDao.insertCoin(coin);
        LiveData<Coin> insertedCoin = coinDao.getCoin(1);
        ActivityController controller = Robolectric.buildActivity(NewCoinActivity.class).create().start();
        AppCompatActivity activity = (AppCompatActivity) controller.get();

        LifecycleOwner lifecycle = mock(LifecycleOwner.class);

        insertedCoin.observe(activity, new Observer<Coin>() {
            @Override
            public void onChanged(@Nullable Coin newCoin) {
                if(newCoin != null){
                    assertEquals("matching",newCoin,coin);
                }
            }
        });
    }

    @Test
    public void testNewCoinSaveLogic() throws  Exception {
        newCoinPresenter.addNewCoin("triggered", "T","100.1","212121.21");
        verify(newCoinScreen).newCoin("triggered", "T",100.1,212121.21);
    }
}
