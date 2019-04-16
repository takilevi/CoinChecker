package com.example.coinchecker.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.coinchecker.model.Coin;

import java.util.List;

@Dao
public interface CoinDao {

    @Insert
    Long insertCoin(Coin coin);


    @Query("SELECT * FROM Coin ORDER BY id asc")
    LiveData<List<Coin>> fetchAllCoins();


    @Query("SELECT * FROM Coin WHERE id =:coinId")
    LiveData<Coin> getCoin(int coinId);


    @Update
    void updateCoin(Coin coin);


    @Delete
    void deleteTask(Coin coin);
}
