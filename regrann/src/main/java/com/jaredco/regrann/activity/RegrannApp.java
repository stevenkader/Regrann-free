package com.jaredco.regrann.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.reflect.Method;
import java.util.Arrays;

//import com.google.ads.mediation.inmobi.InMobiConsent;

public class RegrannApp extends Application {
//    public class RegrannApp extends Application  {

    private static AppOpenManager appOpenManager;
    // The following line should be changed to include the correct property id.

    private static FirebaseAnalytics mFirebaseAnalytics;
    public static RegrannApp _this;
    public static boolean admobReady = false ;
    SharedPreferences preferences;

    //try to catch some uncaught exception
    public static boolean crashInterceptor(Thread thread, Throwable throwable) {

        if (throwable == null || thread.getId() == 1) {
            //Don't intercept the Exception of Main Thread.
            return false;
        }

        String classpath = null;
        if (throwable.getStackTrace() != null && throwable.getStackTrace().length > 0) {
            classpath = throwable.getStackTrace()[0].toString();
        }

//intercept GMS Exception
        return classpath != null
                && throwable.getMessage().contains("Results have already been set")
                && classpath.contains("com.google.android.gms");

    }

    public static void sendEvent(String cat) {
        Log.i("app5", cat) ;
        try {
            mFirebaseAnalytics.logEvent(cat, null);
        }catch (Exception e){}

    }

    public static void sendEvent(String cat, String action, String label) {

        cat = cat.replace('-', '_');
        cat = cat.replace(' ', '_');

        action = action.replace('-', '_');
        action = action.replace(' ', '_');

        label = label.replace('-', '_');
        label = label.replace(' ', '_');

        String evenTxt = cat.replaceAll("\\s+", "") + action.replaceAll("\\s+", "") + label.replaceAll("\\s+", "");

        if (evenTxt.length() > 32)
            evenTxt = evenTxt.substring(0, 31);

        Log.d("app5", evenTxt);
        mFirebaseAnalytics.logEvent(evenTxt, null);


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("app5", "In Regrann App - onCreate");

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("6B4BD6A9C40F28AEA23FBD3EFC72B6A9") )
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        // AdSettings.addTestDevice("24a1e03e-2ea4-4de2-8b4e-bc7fbf4599ed");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        appOpenManager = new AppOpenManager(this);
/**
 JSONObject consentObject = new JSONObject();
 try {
 consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
 consentObject.put("gdpr", "1");
 } catch (JSONException exception) {
 exception.printStackTrace();
 }

 InMobiConsent.updateGDPRConsent(consentObject);
**/
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


/**
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Token : " + token;
                        Log.d("app5", msg);

                    }
                });

 **/


        _this = this;


        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(_this);




        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(
                            Thread paramThread,
                            Throwable paramThrowable
                    ) {
                        //Do your own error handling here

                        if (crashInterceptor(paramThread, paramThrowable))
                            return;


                        if (oldHandler != null)
                            oldHandler.uncaughtException(
                                    paramThread,
                                    paramThrowable
                            ); //Delegates to Android's error handling
                        else
                            System.exit(2); //Prevents the service/app from freezing
                    }
                });


    }





       public int getBannerPlacementId() {
           int bannerPlacementId = -1;
           return bannerPlacementId;
       }

}
