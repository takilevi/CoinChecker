package com.example.coinchecker.ui.coins;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coinchecker.model.Coin;
import com.example.coinchecker.R;
import java.util.List;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.ViewHolder>{
    private Context context;
    private List<Coin> coinsList;

    public CoinsAdapter(Context context, List<Coin> coinsList) {
        this.context = context;
        this.coinsList = coinsList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_coin, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Coin coin = coinsList.get(position);

        holder.tvName.setText(coin.getName());
        holder.tvSymbol.setText(coin.getSymbol());
        holder.tvPrice.setText(coin.getQuote().getUSD().getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return coinsList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvSymbol;
        public TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
