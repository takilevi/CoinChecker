package com.example.coinchecker.room.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.coinchecker.model.Coin;
import com.example.coinchecker.room.database.CoinDatabase;

import java.util.Calendar;
import java.util.List;

public class CoinRepository {

    private String DB_NAME = "db_coin";

    private CoinDatabase coinDatabase;

    public CoinRepository(Context context) {
        coinDatabase = Room.databaseBuilder(context, CoinDatabase.class, DB_NAME).build();
    }

    public void insertCoin(Integer id, String name, String symbol) {
        Coin coin = new Coin(id, name, symbol);
        insertCoin(coin);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertCoin(final Coin coin) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coinDatabase.daoAccess().insertCoin(coin);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCoin(final Coin coin) {
        coin.setDateAdded(Calendar.getInstance().getTime().toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coinDatabase.daoAccess().updateCoin(coin);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteCoin(final Coin coin) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coinDatabase.daoAccess().deleteTask(coin);
                return null;
            }
        }.execute();
    }

    public LiveData<Coin> getCoin(int id) {
        return coinDatabase.daoAccess().getCoin(id);
    }

    public LiveData<List<Coin>> getCoins() {
        return coinDatabase.daoAccess().fetchAllCoins();
    }
}
