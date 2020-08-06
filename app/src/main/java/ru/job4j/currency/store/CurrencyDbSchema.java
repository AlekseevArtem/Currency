package ru.job4j.currency.store;

public class CurrencyDbSchema {
    public static final class CurrencyTable {
        public static final String NAME = "currency";

        public static final class Cols {
            public static final String BASE = "base";
            public static final String DATE = "date";
            public static final String USD = "usd";
            public static final String EUR = "eur";
            public static final String AUD = "aud";
            public static final String GDP = "gdp";
        }
    }
}
