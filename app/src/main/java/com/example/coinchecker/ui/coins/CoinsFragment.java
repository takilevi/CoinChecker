package com.example.coinchecker.ui.coins;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coinchecker.CoinCheckerApplication;
import com.example.coinchecker.R;
import com.example.coinchecker.model.Coin;
import com.example.coinchecker.room.repository.CoinRepository;

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

    public void showCoins(List<Coin> coins) {
        if (swipeRefreshLayoutCoins != null) {
            swipeRefreshLayoutCoins.setRefreshing(false);
        }

        coinsList.clear();
        coinsList.addAll(coins);
        coinsAdapter.notifyDataSetChanged();

        if (coinsList.isEmpty()) {
            recyclerViewCoins.setVisibility(View.GONE);
        } else {
            recyclerViewCoins.setVisibility(View.VISIBLE);

            CoinRepository coinRepository = new CoinRepository(getActivity());

            for (Coin coin : coinsList) {
                if(coinRepository.getCoin(coin.getId()) != null){
                    coinRepository.updateCoin(coin);
                } else {
                    coinRepository.insertCoin(coin);
                }
            }
        }
    }

    @Override
    public void showNetworkError(String errorMsg) {
        if (swipeRefreshLayoutCoins != null) {
            swipeRefreshLayoutCoins.setRefreshing(false);
        }
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
    }
}
