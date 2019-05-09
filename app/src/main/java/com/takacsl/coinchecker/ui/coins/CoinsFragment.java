package com.takacsl.coinchecker.ui.coins;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.takacsl.coinchecker.CoinCheckerApplication;
import com.takacsl.coinchecker.R;
import com.takacsl.coinchecker.model.Coin;
import com.takacsl.coinchecker.room.repository.CoinRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
public class CoinsFragment extends Fragment implements CoinsScreen{

    @Inject
    CoinsPresenter coinsPresenter;
    private RecyclerView recyclerViewCoins;
    private SwipeRefreshLayout swipeRefreshLayoutCoins;
    private List<Coin> coinsList;
    private CoinsAdapter coinsAdapter;

    public CoinsFragment() {
        CoinCheckerApplication.injector.inject(this);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        coinsPresenter.attachScreen(this);
    }

    @Override
    public void onDetach() {
        coinsPresenter.detachScreen();
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coins, container, false);
        recyclerViewCoins = view.findViewById(R.id.recyclerViewCoins);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCoins.setLayoutManager(llm);

        coinsList = new ArrayList<>();
        coinsAdapter = new CoinsAdapter(getContext(), coinsList);
        recyclerViewCoins.setAdapter(coinsAdapter);

        swipeRefreshLayoutCoins = view.findViewById(R.id.swipeRefreshLayoutCoins);

        swipeRefreshLayoutCoins.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                coinsPresenter.refreshCoins();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        coinsPresenter.refreshCoins();
    }

    public void showCoins(final List<Coin> coins) {
        if (swipeRefreshLayoutCoins != null) {
            swipeRefreshLayoutCoins.setRefreshing(false);
        }
        final CoinRepository coinRepository = new CoinRepository(getActivity());

        coinsList.clear();


        if (!coins.isEmpty()) {
            for (Coin coin : coins) {
                coin.setPrice(coin.getQuote().getUSD().getPrice());
                coin.setMarketCap(coin.getQuote().getUSD().getMarketCap());
                final Coin insertableCoin = coin;
                final LiveData<Boolean> inDbCoinFav = coinRepository.getCoinFav(coin.getId());
                inDbCoinFav.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if(aBoolean == null){
                            coinRepository.insertCoin(insertableCoin);
                        } else if(aBoolean){
                            Coin trueCoin = insertableCoin;
                            trueCoin.setFavorite(true);
                            coinRepository.updateCoin(trueCoin);
                        } else {
                            coinRepository.updateCoin(insertableCoin);
                        }

                        coinsList.add(insertableCoin);
                        inDbCoinFav.removeObserver(this);
                    }
                });
            }
        }

        final LiveData<List<Coin>> coinLive = coinRepository.getCoins();
        coinLive.observe(this, new Observer<List<Coin>>() {
            @Override
            public void onChanged(@Nullable List<Coin> coinList){
                if(coinList == null || coinList.isEmpty()) {
                    // No data in database
                } else {
                    //coinsList.addAll(coinList);
                    for (Coin coin : coinList){
                        boolean inView = false;
                        for (Coin onViewCoin : coinsList){
                            if(onViewCoin.getId()==coin.getId()){
                                inView = true;
                            }
                        }
                        if(!inView){
                            coinsList.add(coin);
                        }
                    }
                    if(coinsList.isEmpty()){
                        recyclerViewCoins.setVisibility(View.GONE);
                    } else {
                        recyclerViewCoins.setVisibility(View.VISIBLE);
                    }

                    coinsAdapter.notifyDataSetChanged();
                    coinLive.removeObserver(this);
                }
            }
        });

        if(coinsList.isEmpty()){
            recyclerViewCoins.setVisibility(View.GONE);
        } else {
            recyclerViewCoins.setVisibility(View.VISIBLE);
        }

        coinsAdapter.notifyDataSetChanged();

    }

    @Override
    public void showNetworkError(String errorMsg) {
        if (swipeRefreshLayoutCoins != null) {
            swipeRefreshLayoutCoins.setRefreshing(false);
        }
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
    }
}
