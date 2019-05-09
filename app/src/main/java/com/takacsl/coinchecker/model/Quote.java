package com.takacsl.coinchecker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quote implements Serializable
{

    @SerializedName("USD")
    @Expose
    private USD uSD;
    private final static long serialVersionUID = 1495424441168075625L;

    public USD getUSD() {
        return uSD;
    }

    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }

}
