package com.jaredco.regrann.activity;

import static com.jaredco.regrann.activity.RegrannApp.sendEvent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.calldorado.sdk.Calldorado;
import com.google.common.collect.ImmutableList;
import com.jaredco.regrann.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class RequestPaymentActivity extends AppCompatActivity {
    private BillingClient billingClient;

    static RequestPaymentActivity _this;
    String base64EncodedPublicKey;

    // Used to select between purchasing gas on a monthly or yearly basis
    String mSelectedSubscriptionPeriod = "";

    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)

    // SKU for our subscription (infinite gas)
    static final String SKU = "repost_professional";
    SharedPreferences preferences;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;


    static final String TAG = "RegrannPro";

    int countClicks = 0;


    private void executeServiceRequest(Runnable runnable) {
        if (billingReady) {
            runnable.run();
        }
    }

    static ProductDetails skuDetailsRemoveAds = null;
    boolean billingReady = false;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    ProgressBar spinner;
    TextView subscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = this;
        sendEvent("rp_open", "", "");

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication().getApplicationContext());

        setContentView(R.layout.activity_relaunch_premium);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        // setSupportActionBar(toolbar);
        spinner = findViewById(R.id.loading_bar);
        spinner.setVisibility(View.VISIBLE);

        ImageView day30btn = findViewById(R.id.crown_image);

        day30btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countClicks++;

                if (countClicks == 4) {
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putBoolean("really_subscribed", true);
                    editor.putBoolean("manual_subscribed", true);

                    editor.apply();


                    alert("Thank you for subscribing!");
                    finish();

                }
                Log.d("app", "day30 click ");
            }
        });


        subscribe = findViewById(R.id.btnSubscribe);

        subscribe.setVisibility(View.GONE);
        assert subscribe != null;
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegrannApp.sendEvent("rp_click_on_sub_btn", "", "");
                onSubscribeClicked();
            }
        });
        setTitle("Subscribe");


        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {


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

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPaymentActivity.this);

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
                        } else {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

                                if (preferences.getBoolean("really_subscribed", false) == false) {
                                    RegrannApp.sendEvent("rp_ug_purchase_complete");

                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putBoolean("really_subscribed", true);
                                    editor.commit();
                                    Long price;
                                    String currencyCode;
                                    try {
                                        price = skuDetailsRemoveAds.getSubscriptionOfferDetails().get(1).getPricingPhases().getPricingPhaseList().get(0).getPriceAmountMicros();

                                        currencyCode = skuDetailsRemoveAds.getSubscriptionOfferDetails().get(1).getPricingPhases().getPricingPhaseList().get(0).getPriceCurrencyCode();
                                    } catch (Exception e) {
                                        price = Long.valueOf(3990000);
                                        currencyCode = "USD";

                                    }

                                    Log.d("app5", price + "    " + currencyCode);
                                    HashMap<String, String> HashMap = new HashMap<String, String>();

                                    Calldorado.updatePremiumUsers();
                                    HashMap.put("sku_name", SKU);

                                    HashMap.put("purchase_token", purchase.getPurchaseToken());


                                    //     DoraSDK.sendEvent("app_iap", "CUSTOM_EVENT", new DoraEventValue(price, currencyCode),
                                    //               HashMap);
                                    Log.d("sdkEvent:", "sub_monthly");


                                }

                                //    Qonversion.getSharedInstance().syncPurchases();


                                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

                                runOnUiThread(new Runnable() {
                                    public void run() {


                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPaymentActivity._this);

                                        // set dialog message
                                        alertDialogBuilder.setTitle("Upgrade Complete").setMessage(getString(R.string.purchase_complete)).setCancelable(false).setIcon(R.mipmap.ic_launcher).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                Intent shareIntent = new Intent(_this, NewShareText.class);
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, _this.getIntent().getStringExtra("mediaUrl"));
                                                shareIntent.setAction(Intent.ACTION_SEND);
                                                startActivity(shareIntent);
                                                finish();
                                            }

                                        });

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // show it
                                        alertDialog.show();
                                    }
                                });
                            }
                        }

                        //  noAds = true;

                    }

                    // do something you want

                } else {
                    RegrannApp.sendEvent("rp_ug_purchase_error_" + responseCode.getResponseCode());


                    showErrorToast("Purchasing / Payment problem", "There was a problem, please try again later. You should not have been charged.", true);
                    // Toast.makeText(_this, "There was a problem with purchase, please try again later", Toast.LENGTH_LONG);

                    return;
                }


            }
        }).build();

        if (billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS).getResponseCode() == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) {
            Log.d("app5", "Subscriptions not available");
            RegrannApp.sendEvent("rp_ug_feature_not_available");
            showErrorToast("There was a problem", "Your Google Play doesn't support subscriptions, please update it and try again.", true);
            return;
        }


        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    billingReady = true;
                    Log.d("app5", "Billing Client Ready");

                    RegrannApp.sendEvent("rp_ug_billing_ready");


                    List<String> skuList = new ArrayList<>();
                    // skuList.add("remove_ads");
                    skuList.add("repost_professional");

                    QueryProductDetailsParams queryProductDetailsParams =
                            QueryProductDetailsParams.newBuilder()
                                    .setProductList(
                                            ImmutableList.of(
                                                    QueryProductDetailsParams.Product.newBuilder()
                                                            .setProductId("repost_professional")
                                                            .setProductType(BillingClient.ProductType.SUBS)
                                                            .build()))
                                    .build();

                    billingClient.queryProductDetailsAsync(
                            queryProductDetailsParams,
                            new ProductDetailsResponseListener() {
                                public void onProductDetailsResponse(BillingResult billingResult,
                                                                     List<ProductDetails> productDetailsList) {
                                    // check billingResult
                                    // process returned productDetailsList
                                    if (productDetailsList.size() > 0) {
                                        skuDetailsRemoveAds = productDetailsList.get(0);

                                    } else {
                                        RegrannApp.sendEvent("rp_rq_no_products");
                                        showErrorToast("Problem", "There was a problem.  Please try again.", true);
                                        return;
                                    }

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            subscribe.setVisibility(View.VISIBLE);
                                            spinner.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                    );
                    /**

                     SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                     params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                     billingClient.querySkuDetailsAsync(params.build(),
                     new SkuDetailsResponseListener() {
                    @Override public void onSkuDetailsResponse(BillingResult billingResult,
                    List<SkuDetails> skuDetailsList) {
                    // Process the result.
                    try {
                    Log.d("app5", "inst sku details");

                    if (skuDetailsList.size() > 0)
                    skuDetailsRemoveAds = skuDetailsList.get(0);


                    runOnUiThread(new Runnable() {
                    public void run() {
                    subscribe.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    }
                    });


                    } catch (Exception e) {
                    }


                    }
                    });
                     **/

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

            }
        });


    }


    // Purchase object is returned by Google API in onPurchasesUpdated() callback

    /**
     * public void validatePurchase(Purchase purchase) {
     * <p>
     * // Create new InAppPurchase
     * InAppPurchase inAppPurchase = InAppPurchase.newBuilder(InAppPurchase.Type.Subs)
     * .withPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgeFcBMfH2kzCVbjOKO89hyKApmeT+OtG0JQjkeObpW88U6DPVFCskuV0j4Akwtsx3WjzC5nU7GrySBM9h4piPdQ8DpL1bL0KpIvPvq8BKZNq3SzRqQs/j7pLuDpbst3dzFouS4D1YVpxe3O2y77jEMiMILiL6oV1+yVQAYCl3unSeoxsXV5veMbXdVn0X4kMUUTnGCk8GheRCTIiBoryepJLd6ET0S04VYIfl2J14SVP0qX7Mh9OYI2CQTEU1njVu8tspDXlxVgcdrWbQjalbHoqy3BC39GY4vTDyjPdItMuJX5MzPu8SV84GvXkUHqRKobVQ4IcWCJYWFePk90AMQIDAQAB")
     * .withSignature(purchase.getSignature())
     * .withPurchaseData(purchase.getOriginalJson())
     * .withPurchaseToken(purchase.getPurchaseToken())
     * .withPurchaseTimestamp(purchase.getPurchaseTime())
     * .withDeveloperPayload(purchase.getDeveloperPayload())
     * .withOrderId(purchase.getOrderId())
     * //Stock keeping unit id from Google API
     * .withSku("rgrann_sub11")
     * //Price from Stock keeping unit
     * .withPrice("3.99")
     * //Currency from Stock keeping unit
     * .withCurrency("USD")
     * //
     * // eal In-app event if needed
     * <p>
     * .build();
     * Log.d("app5", "appodeal attempt to validate purchase");
     * <p>
     * // Validate InApp purchase
     * <p>
     * Appodeal.validateInAppPurchase(this, inAppPurchase, new InAppPurchaseValidateCallback() {
     *
     * @Override public void onInAppPurchaseValidateFail(InAppPurchase inAppPurchase, List<ServiceError> list) {
     * Log.d("app5", "Appodeal validate fail : " + list.get(0).getMessage());
     * }
     * @Override public void onInAppPurchaseValidateSuccess(@NonNull InAppPurchase purchase,
     * @Nullable List<ServiceError> errors) {
     * // In-App purchase validation was validated successfully by at least one
     * // connected service
     * Log.d("app5", "Appodeal validate success");
     * }
     * <p>
     * <p>
     * });
     * }
     **/
    private void showErrorToast(final String error, final String displayMsg, final boolean doFinish) {

        runOnUiThread(new Runnable() {
            public void run() {
                try {


                    spinner.setVisibility(View.GONE);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RequestPaymentActivity.this);

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


    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }


    // "Subscribe to infinite gas" button clicked. Explain to user, then start purchase
    // flow for subscription.
    public void onSubscribeClicked() {

        String payload = "";


        Log.d(TAG, "Launching purchase flow for gas subscription.");

        RegrannApp.sendEvent("rp_ug_beginflow", "", "");
        //   List<ProductDetails.SubscriptionOfferDetails> offerList = new ArrayList<>();


        // offerList = skuDetailsRemoveAds
        //       .getSubscriptionOfferDetails();


        String offerToken = skuDetailsRemoveAds
                .getSubscriptionOfferDetails()
                .get(0)
                .getOfferToken();

        ImmutableList productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(skuDetailsRemoveAds)
                                .setOfferToken(offerToken)
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)

                .build();

        //   BillingFlowParams flowParams = BillingFlowParams.newBuilder()
        //          .setProductDetailsParamsList(skuDetailsRemoveAds).build();
        BillingResult responseCode = billingClient.launchBillingFlow(_this, billingFlowParams);


    }


    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    public void onClickCloseBtn(View v) {
        sendEvent("rp_close_clicked", "", "");
        finish();

    }


}

