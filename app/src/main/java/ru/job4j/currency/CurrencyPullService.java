package ru.job4j.currency;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.job4j.currency.store.CurrencyBaseHelper;

public class CurrencyPullService extends JobIntentService {
    public static final String ACTION_MYINTENTSERVICE = "ru.job4j";
    private JsonCurrencyHolderApi jsonCurrencyHolderApi;
    static final int JOB_ID = 1000;

    static void enqueueWork(Context context, Intent work) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(work);
        } else {
            enqueueWork(context, CurrencyPullService.class, JOB_ID, work);
        }
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.exchangeratesapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.jsonCurrencyHolderApi = retrofit.create(JsonCurrencyHolderApi.class);
        getCurrency();
    }

    public void getCurrency() {
        Call<Currency> call = jsonCurrencyHolderApi.getCurrency();
        call.enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(@NotNull Call<Currency> call, @NotNull Response<Currency> response) {
                CurrencyBaseHelper store = CurrencyBaseHelper.getInstance(CurrencyPullService.this);
                store.addCurrency(response.body());
                Intent responseIntent = new Intent();
                responseIntent.setAction(ACTION_MYINTENTSERVICE);
                responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                responseIntent.putExtra("response", true);
                responseIntent.putExtra("responseIsSuccess", response.isSuccessful());
                responseIntent.putExtra("responseCode", response.code());
                sendBroadcast(responseIntent);
            }

            @Override
            public void onFailure(@NotNull Call<Currency> call, @NotNull Throwable t) {
                Intent responseIntent = new Intent();
                responseIntent.setAction(ACTION_MYINTENTSERVICE);
                responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                responseIntent.putExtra("response", false);
                responseIntent.putExtra("error", t.getMessage());
                sendBroadcast(responseIntent);
            }
        });
    }
}
