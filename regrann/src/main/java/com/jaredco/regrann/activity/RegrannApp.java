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
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

//import com.google.ads.mediation.inmobi.InMobiConsent;

public class RegrannApp extends Application {

//    public class RegrannApp extends Application  {


    // The following line should be changed to include the correct property id.

    private static FirebaseAnalytics mFirebaseAnalytics;
    public static RegrannApp _this;
    public static boolean admobReady = false;
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
/**
 androidx.work.Configuration myConfig = new androidx.work.Configuration.Builder()
 .setMinimumLoggingLevel(Log.INFO)
 .build();
 WorkManager.initialize(this, myConfig);
 Calldorado.startCalldorado(this);


 Calldorado.acceptConditions(getApplicationContext(), true);
 **/

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = preferences.getBoolean("startShowTutorial", true);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // if database present
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

    public void copy(File src, File dst) throws IOException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Log.d("app5", "Copying :  " + src.toString() + "   " + dst.toString());
            Files.copy(src.toPath(), dst.toPath());
        } else {


            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();

        }
    }


}
