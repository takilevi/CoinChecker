package com.example.coinchecker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coin {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private int name;

    @SerializedName("symbol")
    @Expose
    private int symbol;

    @SerializedName("total_supply")
    @Expose
    private int total_supply;

    @SerializedName("price")
    @Expose
    private int price_USD;

    @SerializedName("market_cap")
    @Expose
    private int market_cap_USD;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public int getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(int total_supply) {
        this.total_supply = total_supply;
    }

    public int getPrice_USD() {
        return price_USD;
    }

    public void setPrice_USD(int price_USD) {
        this.price_USD = price_USD;
    }

    public int getMarket_cap_USD() {
        return market_cap_USD;
    }

    public void setMarket_cap_USD(int market_cap_USD) {
        this.market_cap_USD = market_cap_USD;
    }
}
