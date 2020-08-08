package ru.job4j.currency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CurrencyPullService.enqueueWork(context,new Intent(context, CurrencyPullService.class));
    }
}
