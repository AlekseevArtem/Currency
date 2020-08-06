package ru.job4j.currency;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonCurrencyHolderApi {
    @GET("latest?base=RUB")
    Call<Currency> getCurrency();
}
