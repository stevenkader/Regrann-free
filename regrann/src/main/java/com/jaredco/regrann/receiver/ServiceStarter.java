package com.jaredco.regrann.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {


    @Override
    public void onReceive(Context ctx, Intent intent) {
        try {
            Log.d("REGRANN", "Received broadcast");
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

                Log.d("REGRANN", "starting clipboard service ");

/**
 if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

 Intent myService = new Intent(ctx, ClipboardListenerService.class);
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 ContextCompat.startForegroundService(ctx, myService);
 } else {
 ctx.startService(myService);
 }
 }
 **/

            }
        }catch (Exception e){}

    }

}
