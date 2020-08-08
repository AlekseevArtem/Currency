package ru.job4j.currency;

import com.google.gson.annotations.SerializedName;

public class Rates {
    @SerializedName("USD")
    private double usd;
    @SerializedName("EUR")
    private double eur;
    @SerializedName("AUD")
    private double aud;
    @SerializedName("GBP")
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

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    public void setAud(double aud) {
        this.aud = aud;
    }

    public void setGbp(double gbp) {
        this.gbp = gbp;
    }
}
