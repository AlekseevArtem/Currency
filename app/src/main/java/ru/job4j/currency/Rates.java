package ru.job4j.currency;

import com.google.gson.annotations.SerializedName;

public class Rates {
    @SerializedName("USD")
    private double usd;
    @SerializedName("EUR")
    private double eur;
    @SerializedName("AUD")
    private double aud;
    @SerializedName("GDP")
    private double gbp;

    public Rates(double usd, double eur, double aud, double gbp) {
        this.usd = usd;
        this.eur = eur;
        this.aud = aud;
        this.gbp = gbp;
    }

    public double getUsd() {
        return usd;
    }

    public double getEur() {
        return eur;
    }

    public double getAud() {
        return aud;
    }

    public double getGbp() {
        return gbp;
    }
}
