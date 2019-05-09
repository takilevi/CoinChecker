package com.takacsl.coinchecker.ui.coins;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.takacsl.coinchecker.R;
import com.takacsl.coinchecker.model.Coin;
import com.takacsl.coinchecker.room.repository.CoinRepository;

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
        final Coin coin = coinsList.get(position);

        holder.tvName.setText(coin.getName());
        holder.tvSymbol.setText(coin.getSymbol());
        holder.tvPrice.setText(coin.getPrice().toString());
        holder.cbFavorite.setOnCheckedChangeListener(null);
        holder.cbFavorite.setChecked(coin.isFavorite());
        Glide.with(this.context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/".concat(coin.getId().toString()).concat(".png")).into(holder.ivImage);

        holder.cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Coin toUpdate = coin;
                toUpdate.setFavorite(isChecked);
                CoinRepository coinRepository = new CoinRepository(context);
                if(coinRepository.getCoin(toUpdate.getId()) != null){
                    coinRepository.updateCoin(toUpdate);
                    Crashlytics.setString("EventAction","Favorite checkbox changed");
                } else {
                    //olyat checkoltunk aminek nincs id-ja
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return coinsList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvSymbol;
        public TextView tvPrice;
        public ImageView ivImage;
        public CheckBox cbFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSymbol = itemView.findViewById(R.id.tvSymbol);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            cbFavorite = itemView.findViewById(R.id.cbFavorite);
        }
    }
}
