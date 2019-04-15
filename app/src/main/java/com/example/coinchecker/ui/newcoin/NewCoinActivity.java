package com.example.coinchecker.ui.newcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.coinchecker.CoinCheckerApplication;
import com.example.coinchecker.R;
import com.example.coinchecker.ui.coins.CoinsActivity;

import javax.inject.Inject;

public class NewCoinActivity extends AppCompatActivity implements NewCoinScreen{
    EditText etName;
    EditText etSymbol;
    EditText etPrice;
    EditText etMarketCap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcoin);
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
                Snackbar.make(view, "Here comes the mock service", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void newCoin() {
        //itt kell a mockservice meghívása és a visszanavigálás
        Intent intent = new Intent(NewCoinActivity.this, CoinsActivity.class);
        startActivity(intent);
    }
}
