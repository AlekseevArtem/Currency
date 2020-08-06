package ru.job4j.currency;

public class Currency {
    private String base;
    private String date;
    private Rates rates;

    public Currency(String base, String date, Rates rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public Rates getRates() {
        return rates;
    }
}

