package com.example.coinchecker.network;

import com.example.coinchecker.model.CoinsResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CoinApi {
    @GET("latest?")
    Call<CoinsResult> getCoins(@Header("X-CMC_PRO_API_KEY") String authorisation, @Query("start") int start, @Query("limit") int limit, @Query("convert") String convert);
}
