package com.example.coinchecker.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CoinData implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<Coin> data = null;
    private final static long serialVersionUID = -3008354287701919910L;

    public List<Coin> getData() {
        return data;
    }

    public void setData(List<Coin> data) {
        this.data = data;
    }

}
