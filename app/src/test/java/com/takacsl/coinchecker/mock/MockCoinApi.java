package com.takacsl.coinchecker.mock;

import com.takacsl.coinchecker.model.Coin;
import com.takacsl.coinchecker.model.CoinData;
import com.takacsl.coinchecker.network.CoinApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockCoinApi implements CoinApi {
    @Override
    public Call<CoinData> getCoins(String authorisation, int start, int limit, String convert) {
        final CoinData coins = new CoinData();
        List<CoinData> coinDataList = new ArrayList<CoinData>();
        List<Coin> coinList = new ArrayList<>();
        Coin item = new Coin(1,"test coin","TC",100.0,10000.23);
        coinList.add(item);
        coins.setData(coinList);
        coinDataList.add(coins);

        Call<CoinData> call = new Call<CoinData>() {
            @Override
            public Response<CoinData> execute() throws IOException {
                return Response.success(coins);
            }

            @Override
            public void enqueue(Callback<CoinData> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<CoinData> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        return call;
    }
}
