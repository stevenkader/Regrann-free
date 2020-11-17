package com.jaredco.regrann.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.SkuDetails;
import com.jaredco.regrann.R;

import java.util.Random;


public class QuickSaveActivity extends AppCompatActivity {

    Uri uri;
    //    private String instagram_activity = "com.instagram.share.common.ShareHandlerActivity" ;
    private BillingClient billingClient;



    private LinearLayout screen_ui;
    SkuDetails skuDetailsRemoveAds = null;



    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4eVlDAKokhC8AZEdsUsKkFJSvsX+d+J8zclWZ25ADxZYOjE+syRGRZo/dBnt5q5YgC4TmyDdF6UFqZ09mlFvwkpU03X+AJP7JadT2bz1jwELBrjsHVlpOFFMwzXrmmBScGybllC+9BBHbnZQDCTRa81GKTdMDSoV/9ez+fdmYy8uCYEOMJ0bCx1eRA3wHMKWiOx5RKoCqBn8PnNOH6JbuXSZOWc762Pkz1tUr2cSuuW7RotgnsMT02jvyALLVcCDiq+yVoRmHrPQCSgcm3Olwc5WjkBoAQMsvy9hn/dyL8a3MtUY0HBI8tN7VJ/r9yhs2JiXCf3jcmd80qF51XJyoQIDAQAB";

    private static final String TAG = QuickSaveActivity.class.getName();

    public static boolean updateScreenOn = false;

    static QuickSaveActivity _this;


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
        t.setText(preferences.getString("upgrade_features",getString(R.string.upgrade_features)));

        Button b =  findViewById(R.id.upgradeBtn);
        b.setText(preferences.getString("upgrade_button_text",getString(R.string.upgrade_button_text)));







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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuickSaveActivity.this);

                    // set dialog message
                    alertDialogBuilder.setMessage(displayMsg).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                                finish();
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
