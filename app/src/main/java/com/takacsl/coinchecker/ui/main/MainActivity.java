package com.takacsl.coinchecker.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.takacsl.coinchecker.CoinCheckerApplication;
import com.takacsl.coinchecker.ui.coins.CoinsActivity;
import com.takacsl.coinchecker.R;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainScreen {

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoinCheckerApplication.injector.inject(this);

        /*
        Button btnShowCoins = findViewById(R.id.btnShowCoins);
        btnShowCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.showCoinsList();
            }
        });
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.attachScreen(this);
        mainPresenter.showCoinsList();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.detachScreen();
    }

    @Override
    public void showCoins() {
        Intent intent = new Intent(MainActivity.this, CoinsActivity.class);
        startActivity(intent);
    }
}
