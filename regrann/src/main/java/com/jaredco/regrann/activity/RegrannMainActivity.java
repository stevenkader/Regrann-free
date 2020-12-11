package com.jaredco.regrann.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jaredco.regrann.BuildConfig;
import com.jaredco.regrann.R;
import com.jaredco.regrann.service.ClipboardListenerService;
import com.jaredco.regrann.sqlite.KeptListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegrannMainActivity extends AppCompatActivity {

    Intent fileObserverIntent;
    SharedPreferences preferences;
    public static RegrannMainActivity _this;

    PendingIntent pendingIntent;
    private SimpleCursorAdapter dataAdapter;
    public static String caption_prefix = "Reposted";

    boolean firstRun, olderUser;

    boolean noAds  ;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    ProgressBar spinner ;

    private BillingClient billingClient;
    boolean billingReady = false;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String country = getUserCountry(getApplicationContext());
        Log.d("app", "countr code " + country);


        _this = this;


          //  IntegrationHelper.validateIntegration(this);

     //  MediationTestSuite.launch(RegrannMainActivity.this);
      //  MediationTestSuite.addTestDevice("B1D20D0F336796629655D59351F179F8");
      //  MediationTestSuite.addTestDevice("03B8364E84BB1446DA5C8FDFA9A4E356");


        preferences = PreferenceManager.getDefaultSharedPreferences(_this.getApplication()
                .getApplicationContext());



        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_this);

                        // set dialog message
                        alertDialogBuilder.setTitle("Upgrade Complete").setMessage(getString(R.string.purchase_complete)).setCancelable(false).setIcon(R.drawable.ic_launcher).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RegrannApp.sendEvent("ug_purchase_acknowledged");

                            }

                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


            }

        };

        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult responseCode, List<Purchase> purchases) {

                if (responseCode.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {

                    for (Purchase purchase : purchases) {
                        //When every a new purchase is made




                            AcknowledgePurchaseParams acknowledgePurchaseParams =
                                    AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(purchase.getPurchaseToken())
                                            .build();


                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {


                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("removeAds", true);

                                editor.commit();

                                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                            }


                        //  noAds = true;

                    }

                    // do something you want

                }


            }
        }).build();


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    billingReady = true;

                    queryPurchases();



                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });




        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

                Intent myService = new Intent(RegrannApp._this, ClipboardListenerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(_this, myService);
                } else {
                    _this.startService(myService);
                }
            }

        } catch (Exception e) {
        }

        noAds = preferences.getBoolean("removeAds", false);

        // public static InstaAPI instaAPI;
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        try {


            final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

            mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
            preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication().getApplicationContext());

            int minFetch = 3600 * 24;

            if (BuildConfig.DEBUG) {
                minFetch = 0;
            }
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(minFetch)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {



                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("multipost_videoid", mFirebaseRemoteConfig.getString("multipost_videoid"));
                                editor.putString("caption_prefix", mFirebaseRemoteConfig.getString("caption_prefix"));
                                editor.putString("upgrade_to_premium", mFirebaseRemoteConfig.getString("upgrade_to_premium"));
                                editor.putString("upgrade_header_text", mFirebaseRemoteConfig.getString("upgrade_header_text"));
                                editor.putString("upgrade_features", mFirebaseRemoteConfig.getString("upgrade_features"));
                                editor.putString("upgrade_button_text", mFirebaseRemoteConfig.getString("upgrade_button_text"));
                                editor.putBoolean("show_midrect",  mFirebaseRemoteConfig.getBoolean("show_midrect"));
                                editor.commit();

                            }

                        }
                    });


        } catch (Throwable w) {
        }





        Intent i = getIntent();
        try {
            Uri data = i.getData();
            String path = data != null ? data.getScheme() : null;
            if (path != null)
                if (path.equals("rg")) {
                    Uri uri = Uri.parse("https://instagram.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                    finish();
                    return;
                }
            //add more cases if multiple links into app.
        } catch (NullPointerException e) {
            // Catch if no path data is send
        }


        //       MobileAds.initialize(getApplicationContext(), "ca-app-pub-1489694459182078~6548979145");


        //    instaAPI = new InstaAPI(this);



        if (getIntent().hasExtra("update_notice")) {
            Log.d("regrann", "UPDATE NOTICE SEEN");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.jaredco.regrann"));
            startActivity(intent);
            finish();
            return;

        }

        if (getIntent().hasExtra("weburl")) {
            Log.d("regrann", "WEB NOTICE SEEN");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getIntent().getStringExtra("weburl")));
            startActivity(intent);
            finish();
            return;

        }


        firstRun = preferences.getBoolean("startShowTutorial", true);
        olderUser = preferences.getBoolean("oldUser", false);


        if (!firstRun && !olderUser)  // is this an old user before this new method
        {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("oldUser", true);

            boolean quickpost = preferences.getBoolean("quickpost", false);
            boolean quicksave = preferences.getBoolean("quicksave", false);
            boolean quickkeep = preferences.getBoolean("quickkeep", false);
            boolean normalMode = preferences.getBoolean("normalMode", true);


            editor.putBoolean("quickpost", quickpost);
            editor.putBoolean("quicksave", quicksave);
            editor.putBoolean("quickkeep", quickkeep);
            editor.putBoolean("normalMode", normalMode);


            if (quickpost)
                editor.putString("mode_list", "1");
            if (normalMode)
                editor.putString("mode_list", "2");
            if (quicksave)
                editor.putString("mode_list", "3");
            if (quickkeep)
                editor.putString("mode_list", "4");


            editor.commit();
        }

        boolean newUser = preferences.getBoolean("firstRun", true);


        if (newUser) {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.putBoolean("foreground_checkbox", false);
            editor.commit();


        }

        boolean firstCheckForForegroundSetting = preferences.getBoolean("firstCheckForForegroundSetting", true);


        if (firstCheckForForegroundSetting) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstCheckForForegroundSetting", false);
            editor.putBoolean("foreground_checkbox", true);
            editor.commit();


        }





        if ((firstRun && newUser)) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("startShowTutorial", false);
            editor.commit();

            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();
            return;
        }


        checkForInstagramURLinClipboard();


    }



    public void onClickBtnChangeLogin(View v) {

        Intent myIntent = new Intent(RegrannMainActivity.this, InstagramLogin.class);
        _this.startActivity(myIntent);

    }




    private boolean addPermission(List<String> permissionsList, String permission) {
        int i = ActivityCompat.checkSelfPermission(this, permission) ;

        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return false;
        }
        return true;
    }

    private void checkPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read SMS");



        if (permissionsNeeded.size() > 0) {



            Intent i;

            i = new Intent(this, CheckPermissions.class);


            startActivity(i);





        } else
            checkForInstagramURLinClipboard();

    }


    @Override
    protected void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT >= 23) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    checkPermissions();
                }
            }, 1000);
        }


        if (!BuildConfig.DEBUG)
            invalidateOptionsMenu();


    }


    public void onClickBtnSupport(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_this);

        // flurryAgent.logEvent("Rating Good or Bad Dialog");
        // set dialog message
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

        alertDialogBuilder.setMessage("Are you stuck?  Do you want to email support?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        emailSupport();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        // flurryAgent.logEvent("Good Selected");

                        dialog.cancel();
                    }
                });

        // create alert dialog
        alertDialogBuilder.create().show();

    }

    public void onClickBtnHelp(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.regrann.com/support"));
        startActivity(browserIntent);
        RegrannApp.sendEvent("rmain_help_btn");
    }

    public void onClickSettingsBtn(View v) {
        RegrannApp.sendEvent("rmain_settings_btn");
        // TODO Auto-generated method stub


        startActivity(new Intent(this, SettingsActivity2.class));

    }

    public void onClickUpgradeButton(View v) {

        Intent i = new Intent(_this, UpgradeActivity.class);
        i.putExtra("from_main_screen", true);
        startActivity(i);
        RegrannApp.sendEvent("rmain_upgrade_btn");
    }


    public void queryPurchases() {
        Runnable queryToExecute = new Runnable() {
            @Override
            public void run() {

                try {



                    RegrannApp.sendEvent("query_purchases", "", "");
                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);

                    if (purchasesResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        RegrannApp.sendEvent("query_purchases_ok", "", "");
                        if (purchasesResult.getPurchasesList().size() > 0) {



                            for (Purchase purchase : purchasesResult.getPurchasesList()) {

                                if (false) {


                                    ConsumeParams consumeParams = ConsumeParams.newBuilder()
                                            .setPurchaseToken(purchase.getPurchaseToken())
                                            .build();

                                    ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
                                        @Override
                                        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {

                                        }
                                    };

                                    SharedPreferences.Editor editor;
                                    editor = preferences.edit();
                                    editor.putBoolean("removeAds", false);

                                    editor.commit();

                                    billingClient.consumeAsync(consumeParams, consumeResponseListener);
                                }


                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                    RegrannApp.sendEvent("query_purchase_foundpurchase", "", "");
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("removeAds", true);

                                    editor.commit();



                                }
                            }

                        }


                    }
                } catch (Exception e) {
                    int i = 1 ;
                }
            }

        };

        try {
            executeServiceRequest(queryToExecute);
        } catch (Exception e) {
        }

    }



    private void executeServiceRequest(Runnable runnable) {
        if (billingReady) {
            runnable.run();
        }
    }



    private void checkForPostLaterPhotos() {
        KeptListAdapter dbHelper = KeptListAdapter.getInstance(this);
        Cursor cursor = dbHelper.fetchAllItems();

        try {


            if (cursor != null && cursor.getCount() > 0) {


                startActivity(new Intent(_this, KeptForLaterActivity.class));
                firstRun = false;
                finish();

            } else {

                boolean fromForegroundNotice = getIntent().getBooleanExtra("fromforegroundnotice", false);

                //   Intent i = new Intent(this, SettingsActivity2.class);
                //  i.putExtra("fromforegroundnotice", fromForegroundNotice);
                // startActivity(i);

                //  finish();

                showMainScreen();


                /**
                 boolean viewNotice001 = preferences.getBoolean("viewNotice001", false);

                 if (viewNotice001 == false && firstRun == false) {

                 Thread thread = new Thread() {

                @Override public void run() {

                // Block this thread for 2 seconds.
                try {
                Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                // After sleep finished blocking, create a Runnable to run on the UI Thread.
                runOnUiThread(new Runnable() {
                @Override public void run() {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("viewNotice001", true);
                editor.commit();
                Intent intent = new Intent(_this, WebViewActivity.class);
                intent.putExtra("url", "http://regrann.com/update_news");
                intent.putExtra("title", "Latest Regrann Information");
                intent.putExtra("help", true);
                startActivity(intent);

                Toast.makeText(_this, "Important Regrann Support News For You", Toast.LENGTH_LONG);

                }
                });

                }

                };

                 // Don't forget to start the thread.
                 thread.start();


                 }
                 **/


            }
        } catch (Exception e) {


        }


    }


    int numClicks  = 0 ;

    private void showMainScreen() {

        setContentView(R.layout.activity_mainscreen);

        //     Toolbar myToolbar = findViewById(R.id.my_toolbar);
        //   setSupportActionBar(myToolbar);


        noAds = preferences.getBoolean("removeAds", false);


        if (!noAds)
            findViewById(R.id.imgPatch).setVisibility(View.GONE);
        else
            findViewById(R.id.btnUpgrade).setVisibility(View.GONE);


    }


    private void emailSupport() {
        String version  = "";

        try {
            PackageManager manager = _this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    _this.getPackageName(), 0);
             version = info.versionName;
        }catch (Exception e){}


        String details = "APP VERSION: " +  version
                + "\nANDROID OS: " + Build.VERSION.RELEASE
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL;




        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@jaredco.com"});
        intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n" + details);


        intent.putExtra(Intent.EXTRA_SUBJECT, "Need help with Regrann Free");


        intent.setType("message/rfc822");

        Toast.makeText(_this, "Preparing email", Toast.LENGTH_LONG).show();
        startActivity(Intent.createChooser(intent, "Select Email Sending App :"));


        RegrannApp.sendEvent("main_email_support");


    }

    public void watchYoutubeVideo(String id) {


        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);


            RegrannApp.sendEvent("main_ytube_multi");


        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void onClickHeaderView(View view) {
        numClicks++;

        if (numClicks == 4) {


            preferences = PreferenceManager.getDefaultSharedPreferences(_this.getApplication().getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("removeAds", true);

            editor.commit();
            Toast.makeText(_this, "The Upgrade is Complete", Toast.LENGTH_LONG).show();

            noAds = true;


            findViewById(R.id.imgPatch).setVisibility(View.VISIBLE);

            findViewById(R.id.btnUpgrade).setVisibility(View.GONE);


        }

    }


    public void onClickHowToMulti(View view) {


        watchYoutubeVideo(preferences.getString("multipost_videoid", "rikrGCItVSw"));

    }


    public void onClickOpenSettings(View view) {
        RegrannApp.sendEvent("rmain_settings_btn");
        // TODO Auto-generated method stub


        startActivity(new Intent(this, SettingsActivity2.class));

    }


    private void checkForInstagramURLinClipboard() {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData = clipboard != null ? clipboard.getPrimaryClip() : null;

        if (clipData != null) {

            try {
                final ClipData.Item item = clipData.getItemAt(0);
                String text = item.coerceToText(RegrannMainActivity.this).toString();
                ClipData clip = ClipData.newPlainText("message", "");
                clipboard.setPrimaryClip(clip);

                if (text.length() > 18) {

                    //    if (text.indexOf("ig.me") > 1 ||text.indexOf("instagram.com/tv/") > 1 || text.indexOf("instagram.com/p/") > 1) {
                     if ((text.contains("ig.me") || text.contains("vm.tiktok")|| text.contains("instagram.com/reel/") || text.contains("instagram.com/tv/")) || (text.contains("/p/") && text.contains("instagram.com"))) {

                        Intent i;
                        i = new Intent(_this, ShareActivity.class);

                        if ( text.contains("vm.tiktok")) {
                            //   i.putExtra("tiktok", true);
                        }
                        else
                            text = text.substring(text.indexOf("https://www.instagram"));



                        i.putExtra("mediaUrl", text);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                        i.putExtra("isJPEG", "no");
                        System.out.println("***media url " + text);


                        startActivity(i);
                        finish();
                        return;

                    }

                }


            } catch (Exception e) {
            }
        }

        checkForPostLaterPhotos();


    }


    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm != null ? tm.getSimCountryIso() : null;
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }



}
