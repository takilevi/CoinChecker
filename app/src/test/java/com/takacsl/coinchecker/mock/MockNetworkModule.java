package com.takacsl.coinchecker.mock;

import com.takacsl.coinchecker.network.CoinApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MockNetworkModule {

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create());

    }

    @Provides
    @Singleton
    public CoinApi provideCoinApi(Retrofit.Builder retrofitBuilder) {
        return new MockCoinApi();
    }
}