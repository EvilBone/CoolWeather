package com.coolweather.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.coolweather.service.AutoUpdateService;

/**
 * Created by bone on 16/4/10.
 */
public class AutoUpdateRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
