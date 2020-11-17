package com.jaredco.regrann.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.jaredco.regrann.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class UpgradeActivity extends AppCompatActivity {

    Uri uri;
    //    private String instagram_activity = "com.instagram.share.common.ShareHandlerActivity" ;
    private BillingClient billingClient;



    private LinearLayout screen_ui;
    SkuDetails skuDetailsRemoveAds = null;



    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4eVlDAKokhC8AZEdsUsKkFJSvsX+d+J8zclWZ25ADxZYOjE+syRGRZo/dBnt5q5YgC4TmyDdF6UFqZ09mlFvwkpU03X+AJP7JadT2bz1jwELBrjsHVlpOFFMwzXrmmBScGybllC+9BBHbnZQDCTRa81GKTdMDSoV/9ez+fdmYy8uCYEOMJ0bCx1eRA3wHMKWiOx5RKoCqBn8PnNOH6JbuXSZOWc762Pkz1tUr2cSuuW7RotgnsMT02jvyALLVcCDiq+yVoRmHrPQCSgcm3Olwc5WjkBoAQMsvy9hn/dyL8a3MtUY0HBI8tN7VJ/r9yhs2JiXCf3jcmd80qF51XJyoQIDAQAB";

    private static final String TAG = UpgradeActivity.class.getName();

    public static boolean updateScreenOn = false;

    static UpgradeActivity _this;


    static String url, title, author;


    SharedPreferences sharedPref;

    ProgressDialog pd;


    TextView btnRemoveAds;

    SharedPreferences preferences;


    Random rand = new Random();

    // The request code must be 0 or greater.


    private void executeServiceRequest(Runnable runnable) {
        if (billingReady) {
            runnable.run();
        }
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

                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                    RegrannApp.sendEvent("query_purchase_foundpurchase", "", "");
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("removeAds", true);

                                    editor.commit();


                                }
                            }
                        }
                        Log.i(TAG, "Skipped subscription purchases query since they are not supported");
                    }
                } catch (Exception e) {
                }
            }

        };

        try {
            executeServiceRequest(queryToExecute);
        } catch (Exception e) {
        }

    }

    boolean billingReady = false;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    ProgressBar spinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        _this = this;
        setContentView(R.layout.activity_upgrade);


        spinner = findViewById(R.id.loading_bar);
        spinner.setVisibility(View.VISIBLE);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);


        preferences = PreferenceManager.getDefaultSharedPreferences(_this.getApplication().getApplicationContext());


      //  noAds = preferences.getBoolean("removeAds", false);


       // mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnRemoveAds = findViewById(R.id.upgradeBtn);

        btnRemoveAds.setVisibility(View.INVISIBLE);



        TextView t =  findViewById(R.id.upgrade_header_text);
        t.setText(preferences.getString("upgrade_header_text",getString(R.string.upgrade_header_text)));

        t =  findViewById(R.id.upgrade_features);
//        t.setText(preferences.getString("upgrade_features",getString(R.string.upgrade_features)));

        Button b =  findViewById(R.id.upgradeBtn);
        b.setText(preferences.getString("upgrade_button_text",getString(R.string.upgrade_button_text)));





        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

                try {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpgradeActivity._this);

                    // set dialog message
                    alertDialogBuilder.setTitle("Upgrade Complete").setMessage(getString(R.string.purchase_complete)).setCancelable(false).setIcon(R.drawable.ic_launcher).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RegrannApp.sendEvent("ug_purchase_acknowledged");
                            finish();
                        }

                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }catch (Exception e) {}



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



                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                            // Here you can confirm to the user that they've started the pending
                            // purchase, and to complete it, they should follow instructions that
                            // are given to them. You can also choose to remind the user in the
                            // future to complete the purchase if you detect that it is still
                            // pending.

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpgradeActivity.this);

                            // set dialog message
                            alertDialogBuilder.setMessage(getString(R.string.purchase_pending)).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    finish();
                                }

                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }
                        else {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

                                if (getIntent().getBooleanExtra("from_main_screen", false))
                                    RegrannApp.sendEvent("ug_pur_mn_complete");

                                if (getIntent().getBooleanExtra("from_qs_screen", false))
                                    RegrannApp.sendEvent("ug_pur_qs_complete");


                                if (getIntent().getBooleanExtra("from_ms_screen", false))
                                    RegrannApp.sendEvent("ug_pur_ms_complete");

                                RegrannApp.sendEvent("ug_purchase_complete");

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("removeAds", true);

                                editor.commit();

                                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                            }
                        }

                        //  noAds = true;

                    }

                    // do something you want

                } else {
                    RegrannApp.sendEvent("ug_purchase_error_"+responseCode.getResponseCode() );


                    showErrorToast("Purchasing / Payment problem", "There was a problem, please try again later. You should not have been charged.", false);
                    // Toast.makeText(_this, "There was a problem with purchase, please try again later", Toast.LENGTH_LONG);

                    return;
                }


            }
        }).build();


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    billingReady = true;
                    Log.d("app5", "Billing Client Ready");

                    RegrannApp.sendEvent("ug_billing_ready");





                    queryPurchases();

                    List<String> skuList = new ArrayList<>();
                    // skuList.add("remove_ads");
                    skuList.add("remove_ads_299");

                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    // Process the result.
                                    try {
                                        Log.d("app5", "inst sku details");

                                        if (skuDetailsList.size() > 0)
                                            skuDetailsRemoveAds = skuDetailsList.get(0);

                                        btnRemoveAds.setVisibility(View.VISIBLE);

                                        spinner.setVisibility(View.GONE);

                                    } catch (Exception e) {
                                    }


                                }
                            });

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

            }
        });





    }




    private void showErrorToast(final String error, final String displayMsg) {
        if (!updateScreenOn)
            showErrorToast(error, displayMsg, false);
    }

    private void showErrorToast(final String error, final String displayMsg, final boolean doFinish) {

        runOnUiThread(new Runnable() {
            public void run() {
                try {


                    spinner.setVisibility(View.GONE);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpgradeActivity.this);

                    // set dialog message
                    alertDialogBuilder.setMessage(displayMsg).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                        }

                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                } catch (Exception e) {
                }
            }
        });

    }


    public void onClickUpgradeNow(View view) {

        if (false) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("removeAds", true);

            editor.commit();
            return;
        }


        if (!billingReady) {
            RegrannApp.sendEvent("ug_removeads_billingnotready");
            showErrorToast("Purchasing system isn't ready", "Please try again later", true);
        } else {
// Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().

            RegrannApp.sendEvent("ug_removeAds_beginflow", "", "");
            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetailsRemoveAds).build();
            BillingResult responseCode = billingClient.launchBillingFlow(_this, flowParams);

            if (responseCode != null) {

                int i = 1;
            }
        }
    }



    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();

         //   overridePendingTransition(R.anim.slide_out, R.anim.slide_in);




        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {

        super.onResume();


    }


}
