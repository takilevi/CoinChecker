package com.example.coinchecker.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.coinchecker.model.Coin;
import com.example.coinchecker.room.dao.CoinDao;

@Database(entities = {Coin.class}, version = 1, exportSchema = false)
public abstract class CoinDatabase extends RoomDatabase {

    public abstract CoinDao daoAccess();
}
