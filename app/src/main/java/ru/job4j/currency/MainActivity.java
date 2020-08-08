package ru.job4j.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import ru.job4j.currency.store.CurrencyBaseHelper;

public class MainActivity extends AppCompatActivity {
    private TextView usd, eur, aud, gdp, date;
    private Button update;
    private Currency currency;
    private CurrencyBaseHelper store;
    private MyBroadcastReceiver mMyBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateCurrency();
        usd = findViewById(R.id.usd_value);
        eur = findViewById(R.id.eur_value);
        aud = findViewById(R.id.aud_value);
        gdp = findViewById(R.id.gdp_value);
        date = findViewById(R.id.date_of_update);
        update = findViewById(R.id.update);
        update.setOnClickListener(v -> updateCurrency());
        store = CurrencyBaseHelper.getInstance(this);
        currency = store.getCurrency();
        if(currency != null) {
            refreshUI();
        }
        initBroadcastReceiver();
        initAlarm();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyBroadcastReceiver);
    }

    private void refreshUI() {
        DecimalFormat f = new DecimalFormat("0.00");
        usd.setText(f.format(currency.getRates().getUsd()));
        eur.setText(f.format(currency.getRates().getEur()));
        aud.setText(f.format(currency.getRates().getAud()));
        gdp.setText(f.format(currency.getRates().getGbp()));
        date.setText(String.valueOf(currency.getDate()));
    }

    private void updateCurrency() {
        CurrencyPullService.enqueueWork(this,new Intent(this, CurrencyPullService.class));
    }

    private void initBroadcastReceiver() {
        mMyBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(
                CurrencyPullService.ACTION_MYINTENTSERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mMyBroadcastReceiver, intentFilter);
    }

    private void initAlarm() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                1000 * 2, alarmIntent);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean response = intent.getBooleanExtra("response",false);
            boolean success = intent.getBooleanExtra("responseIsSuccess", false);
            String code = intent.getStringExtra("responseCode");
            String error = intent.getStringExtra("error");
            if(response) {
                if(success) {
                    currency = store.getCurrency();
                    refreshUI();
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, String.format("Ошибка, статус ответа: %s", code), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, String.format("Error: %s", error), Toast.LENGTH_SHORT).show();
            }
        }
    }


}