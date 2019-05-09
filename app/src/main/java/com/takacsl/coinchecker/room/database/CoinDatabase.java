package com.takacsl.coinchecker.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.takacsl.coinchecker.model.Coin;
import com.takacsl.coinchecker.room.dao.CoinDao;

@Database(entities = {Coin.class}, version = 6, exportSchema = false)
public abstract class CoinDatabase extends RoomDatabase {

    public abstract CoinDao daoAccess();
}
