package com.jaredco.regrann.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseEventBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "FirebaseEventB";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("custom_firebase_event")) {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            String eventName = intent.getStringExtra("eventName");
            String imageName = intent.getStringExtra("imageName");
            String fullText = intent.getStringExtra("fullText");
            String eventType = intent.getStringExtra("eventType");
            if (mFirebaseAnalytics != null && eventName != null) {
                Log.d(TAG, "logging firebase event.. eventName = " + eventName +
                        ", imageName = " + imageName + ", fullText = " + fullText + ", eventType = " + eventType);
                Bundle params = new Bundle();
                if (imageName != null) {
                    params.putString("image_name", imageName);
                }
                if (fullText != null) {
                    params.putString("full_text", fullText);
                }
                mFirebaseAnalytics.logEvent(eventName, params);
            }
        }
    }
}
