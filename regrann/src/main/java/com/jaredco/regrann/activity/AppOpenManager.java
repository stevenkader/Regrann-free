package com.jaredco.regrann.activity;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

/**
 * Prefetches App Open Ads.
 */
public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "app5";
    private static final String AD_UNIT_ID = "ca-app-pub-8534786486141147/6665853268";  //ca-app-pub-8534786486141147/6665853268   //ca-app-pub-3940256099942544/3419835294"
    private AppOpenAd appOpenAd = null;
    private boolean isAutoSave, isQuickPost, isQuickKeep;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity currentActivity;
    private static final boolean okToShow = false;
    private final RegrannApp myApplication;
    FullScreenContentCallback fullScreenContentCallback;
    private static boolean isShowingAd = false;

    /**
     * Constructor
     */
    public AppOpenManager(RegrannApp myApplication) {
        this.myApplication = myApplication;

        this.myApplication.registerActivityLifecycleCallbacks(this);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        /** LifecycleObserver methods */


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegrannApp._this);


        boolean noAds = preferences.getBoolean("removeAds", false);


        if (noAds)
            return;

        isAutoSave = preferences.getBoolean("quicksave", false);
        isQuickPost = preferences.getBoolean("quickpost", false);
        isQuickKeep = preferences.getBoolean("quickkeep", false);

        if (isAutoSave == false && isQuickKeep == false && isQuickPost == false)
            return;


        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };


            if (currentActivity.toString().contains("NewShareText")) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RegrannApp.sendEvent("AppOpen - Showing Quick");
                        appOpenAd.show(currentActivity, fullScreenContentCallback);
                    }
                }, 50);


            }

            Log.d("app5", "show ad  : " + currentActivity.toString());

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAppOpenAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        RegrannApp.sendEvent("AppOpen - Loaded");
                        Log.d("app5", "Add Loaded");
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAppOpenAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                        RegrannApp.sendEvent("AppOpen - Fail Load");
                        Log.d("app5", "Ad failed to load  : " + loadAdError.getMessage());
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * ActivityLifecycleCallback methods
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {


        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {


        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(
            Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        currentActivity = activity;

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }
}

