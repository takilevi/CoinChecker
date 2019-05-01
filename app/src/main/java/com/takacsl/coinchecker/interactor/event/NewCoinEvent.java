package com.takacsl.coinchecker.interactor.event;

public class NewCoinEvent {
    private int code;
    private String name;
    private String symbol;
    private Double price;
    private Double marketcap;
    private Throwable throwable;

    public NewCoinEvent(){}

    public NewCoinEvent(int code, String name, String symbol, Double price, Double marketcap, Throwable throwable) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.marketcap = marketcap;
        this.throwable = throwable;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(Double marketcap) {
        this.marketcap = marketcap;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
