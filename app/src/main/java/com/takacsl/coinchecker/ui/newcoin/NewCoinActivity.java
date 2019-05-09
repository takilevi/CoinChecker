package com.takacsl.coinchecker.ui.newcoin;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.takacsl.coinchecker.CoinCheckerApplication;
import com.takacsl.coinchecker.R;
import com.takacsl.coinchecker.room.repository.CoinRepository;
import com.takacsl.coinchecker.ui.coins.CoinsActivity;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

public class NewCoinActivity extends AppCompatActivity implements NewCoinScreen{
    EditText etName;
    EditText etSymbol;
    EditText etPrice;
    EditText etMarketCap;

    @Inject
    NewCoinPresenter newCoinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_newcoin);
        CoinCheckerApplication.injector.inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = findViewById(R.id.etName);
        etSymbol = findViewById(R.id.etSymbol);
        etPrice = findViewById(R.id.etPrice);
        etMarketCap = findViewById(R.id.etMarketCap);

        Button btnShowSongs = findViewById(R.id.btnSendData);
        btnShowSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here comes the mock service", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */

                newCoinPresenter.addNewCoin(etName.getText().toString(), etSymbol.getText().toString(), etPrice.getText().toString(), etMarketCap.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        newCoinPresenter.attachScreen(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        newCoinPresenter.detachScreen();
    }

    @Override
    public void newCoin(final String name, final String symbol, final Double price, final Double marketcap) {
        final CoinRepository coinRepository = new CoinRepository(getApplicationContext());
        final LiveData<Integer> id = coinRepository.getMaxId();
        id.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer == null){
                    coinRepository.insertCoin(10000000,name,symbol,price,marketcap);
                    id.removeObserver(this);
                    return;
                } else {
                    coinRepository.insertCoin(integer + 1,name,symbol,price,marketcap);
                    id.removeObserver(this);
                    return;
                }
            }
        });
        Crashlytics.setString("EventAction","New coin inserted");
        Intent intent = new Intent(NewCoinActivity.this, CoinsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showNetworkError(String errorMsg) {
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
    }
}
