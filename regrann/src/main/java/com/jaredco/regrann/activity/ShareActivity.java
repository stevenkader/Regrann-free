package com.jaredco.regrann.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jaredco.regrann.BuildConfig;
import com.jaredco.regrann.R;
import com.jaredco.regrann.model.InstaItem;
import com.jaredco.regrann.sqlite.KeptListAdapter;
import com.jaredco.regrann.util.Util;
import com.ogury.ed.OguryThumbnailAd;
import com.ogury.ed.OguryThumbnailAdCallback;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.presage.Presage;
import io.presage.common.AdConfig;

import static com.jaredco.regrann.R.id.slider;
import static java.lang.Thread.sleep;

//import com.dragonmobile.sdk.api.Dragon;


//import com.aviary.android.feather.sdk.AviaryIntent;

//import com.google.android.gms.plus.PlusOneButton;


/*****
 *
 private void onInviteClicked() {
 Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
 .setMessage(getString(R.string.invitation_message))
 .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
 .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
 .setCallToActionText(getString(R.string.invitation_cta))
 .build();
 startActivityForResult(intent, REQUEST_INVITE);
 }

 @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
 Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

 if (requestCode == REQUEST_INVITE) {
 if (resultCode == RESULT_OK) {
 // Get the invitation IDs of all sent messages
 String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
 for (String id : ids) {
 Log.d(TAG, "onActivityResult: sent invitation " + id);
 }
 } else {
 // Sending failed or it was canceled, show failure message to the user
 // ...
 }
 }
 }
 **/


/**
 * import com.revmob.RevMob;
 * import com.revmob.RevMobAdsListener;
 **/
public class ShareActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, OnClickListener, OnSeekBarChangeListener, OnCompletionListener, OnPreparedListener {
    int buttonWidth;
    private ImageView postlater, btnCurrentToFeed, btnCurrentToStory, btnInstagramstories, scheduleBtn, settings, btnEmail, btndownloadphoto, btnTweet, btnFacebook, btnSMS, btnSetting, btnInviteFriends, btnInstagram, btnShare;
    private ImageView btnShareAppFB, videoIcon, btnShareAppTW, btnShareAppGooglePlus;
    private SeekBar seekBarOpacity;
    private String uriStr;
    private ConstraintLayout mainUI;
    ImageView previewImage;

    boolean btnStoriesClicked = false;
    boolean instagramLoggedIn = false;
    boolean is_private = false;
    static boolean launchedLogin = false;
    String currentURL = "";
    static String[] fnames = new String[30];
    Uri uri;
    private String instagram_activity = "com.instagram.share.common.ShareHandlerActivity";
    boolean tiktokLink = false;

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;

    private Button turnOffQuickPost;
    static boolean newIntentSeen = true;
    String saveToastMsg;
    private SliderLayout mDemoSlider = null;
    private boolean isMulti = false;

    boolean shouldLoadAds = true;

    private String app_api_key = "OGY-EF265F412C32";

    VideoView videoPlayer;

    private LinearLayout screen_ui;
    SkuDetails skuDetailsRemoveAds = null;
    ProgressDialog pdmulti;

    private int smallBannerPlacmentId = -1;
    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4eVlDAKokhC8AZEdsUsKkFJSvsX+d+J8zclWZ25ADxZYOjE+syRGRZo/dBnt5q5YgC4TmyDdF6UFqZ09mlFvwkpU03X+AJP7JadT2bz1jwELBrjsHVlpOFFMwzXrmmBScGybllC+9BBHbnZQDCTRa81GKTdMDSoV/9ez+fdmYy8uCYEOMJ0bCx1eRA3wHMKWiOx5RKoCqBn8PnNOH6JbuXSZOWc762Pkz1tUr2cSuuW7RotgnsMT02jvyALLVcCDiq+yVoRmHrPQCSgcm3Olwc5WjkBoAQMsvy9hn/dyL8a3MtUY0HBI8tN7VJ/r9yhs2JiXCf3jcmd80qF51XJyoQIDAQAB";

    private static final String TAG = ShareActivity.class.getName();

    boolean isVine = false;
    //   private PubnativeFeedBanner mFeedBanner;
    public static boolean updateScreenOn = false;
    static WebView webViewInsta;
    long photoDownloadID = 0;
    long videoDownloadID = 0;
    int sdkVersion;
    private AdView mAdView;
    boolean supressToast = false;
    static ShareActivity _this;
    public static final int ACTION_SMS_SEND = 0;
    public static final int ACTION_TWEET_SEND = 1;
    public static final int ACTION_FACEBOOK_POST = 2;
    public static final int MEDIA_IMAGE = 1;
    public static final int MEDIA_VIDEO = 2;

    private ProgressBar spinner;
    private String mTinyUrl = null;
    JSONObject jsonInstagramDetails;
    static String url, title, author;
    String internalPath;
    static File tempFile, tmpVideoFile;
    boolean photoReady = false;
    Bitmap photoBitmap;
    DownloadManager mgr;
    boolean optionHasBeenClicked = false;
    int inputMediaType = 0;
    String inputFileName = "";
    private TextView t;
    private String profile_pic_url;
    private Bitmap originalBitmapBeforeNoCrop;

    SharedPreferences sharedPref;
    private String caption_suffix = "";
    ProgressDialog pd;

    int numMultVideos = 0;

    boolean loadingMultiVideo = false;

    boolean isVideo = false;
    boolean isJPEG = false;
    boolean isQuickKeep = false;

    int numOfMultiVideos = 0;

    static String tempFileName;
    String tempVideoName = "temp/tmpvideo.mp4";
    File tempVideoFile;
    String tempFileFullPathName, tempVideoFullPathName = "";
    String regrannPictureFolder = null;
    String regrannMultiPostFolder = null;
    int count, count2;

    AlertDialog rateRequestDialog, msgDialog;
    //  PlusOneButton mPlusOneButton = null;
    // private RevMob revmob;
    boolean isQuickPost = false;
    boolean isAutoSave = false;
    boolean isKeepForLater = false;
    Button editPhotoBtn = null;

    TextView btnRemoveAds;
    String videoURL;
    SharedPreferences preferences;

    private InterstitialAd mInterstitialAd;

    Random rand = new Random();
    int nrand = rand.nextInt(3);


    boolean QuickSaveShare = false;

    private FirebaseAnalytics mFirebaseAnalytics;
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private Intent shareIntent = null;
    private boolean sendToInstagram = false;

    private int showAdProbability = 0;
    private boolean showInterstitial;
    private boolean showNativeAd = true;
    private boolean showNimmbleBanner = true;
    private boolean noAds = false;
    AdView adView;
    private boolean instagramBtnClicked = false;


    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-8534786486141147/7602271271";


    private UnifiedNativeAd nativeAd;
    boolean billingReady = false;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    private BillingClient billingClient;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


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


    private boolean addPermission(List<String> permissionsList, String permission) {
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
        //    if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
        //       permissionsNeeded.add("Read SMS");
        //   if (!addPermission(permissionsList, android.Manifest.permission.READ_SMS))
        //      permissionsNeeded.add("Read SMS");


        if (permissionsNeeded.size() > 0) {

            //     Intent test = new Intent(this, CheckPermissions.class);
            //    startActivity(test);

            Intent i;

            i = new Intent(this, CheckPermissions.class);
            i.putExtra("from_sharescreen", true);
            i.putExtra("mediaUrl", getIntent().getStringExtra("mediaUrl"));


            startActivity(i);

            RegrannApp.sendEvent("sc_request_permission");
            //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();


        }
    }


    private void executeServiceRequest(Runnable runnable) {
        if (billingReady == true) {
            runnable.run();
        }
    }


    void setupBilling() {


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

                } catch (Exception e) {
                }


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

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);

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
                }


            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_up_anim, R.anim.slide_down_anim);

        _this = this;

        //   System.setProperty("http.keepAlive", "false");

        numMultVideos = 0;

/**
 Intent myService = new Intent(RegrannApp._this, ClipboardListenerService.class);
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 ContextCompat.startForegroundService(_this, myService);
 } else {
 _this.startService(myService);
 }
 **/

        //   setupBilling();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);


        preferences = PreferenceManager.getDefaultSharedPreferences(_this.getApplication().getApplicationContext());
        isVine = getIntent().getBooleanExtra("vine", false);

        tiktokLink = getIntent().getBooleanExtra("tiktok", false);
        noAds = preferences.getBoolean("removeAds", false);

        if (BuildConfig.DEBUG) {
            //       noAds = false;
        }


        showInterstitial = !noAds;


        if (noAds) {

            RegrannApp.sendEvent("sc_paiduser");
        } else {
            Presage.getInstance().start(app_api_key, this);
        }

        //   AppGrowDataMonetizationSDKFactory.getSDK().init(ShareActivity.this, ShareActivity.this);


        //Dragon.engage(this);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get Remote Config instance.
        // [START get_remote_config_instance]


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
                                editor.putString("caption_prefix", mFirebaseRemoteConfig.getString("caption_prefix"));
                                editor.putString("upgrade_to_premium", mFirebaseRemoteConfig.getString("upgrade_to_premium"));
                                editor.putString("upgrade_header_text", mFirebaseRemoteConfig.getString("upgrade_header_text"));
                                editor.putString("upgrade_features", mFirebaseRemoteConfig.getString("upgrade_features"));
                                editor.putString("upgrade_button_text", mFirebaseRemoteConfig.getString("upgrade_button_text"));
                                editor.putBoolean("show_midrect", mFirebaseRemoteConfig.getBoolean("show_midrect"));
                                editor.commit();

                            }

                        }
                    });


        } catch (Throwable w) {
        }


        // [END get_remote_config_instance]

        // Create Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        // [START enable_dev_mode]

        numOfMultiVideos = 0;


        _this = this;


        updateScreenOn = false;
        // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Log.d(TAG, "Refreshed firebase token: " + refreshedToken);


        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read SMS");


        if (Build.VERSION.SDK_INT >= 23 && permissionsNeeded.size() > 0) {

            checkPermissions();

            return;

        }


        if (android.os.Build.VERSION.RELEASE.startsWith("5.")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("onOS_5", true);

            editor.apply();


        } else {
            if (android.os.Build.VERSION.RELEASE.startsWith("6.")) {

                if (preferences.getBoolean("onOS_5", false)) {
                    // we were on 5 and now on 6 need to warn the USER
                    try {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);

                        // set dialog message
                        alertDialogBuilder.setTitle(R.string.instagram_bug_title);
                        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

                        alertDialogBuilder.setMessage(R.string.instagram_bug)
                                .setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }

                        });
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("onOS_5", false);

                        editor.apply();

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    } catch (Exception e) {
                    }

                }

            }
        }


        boolean madeItToShareScreen = preferences.getBoolean("madeItToShareScreen", false);
        if (!madeItToShareScreen) {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("madeItToShareScreen", true);


            editor.apply();
            RegrannApp.sendEvent("sc_first_time");

        } else
            RegrannApp.sendEvent("sc_entered");


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    deleteOldFiles("temp");
                } catch (Exception e) {
                }
            }
        });
        thread.start();

        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), "temp");

        if (!file.mkdirs()) {
            Log.e("error", "Directory not created");
        }

        try {


            File output = new File(file.getPath(), ".nomedia");
            boolean fileCreated = output.createNewFile();
        } catch (Exception e) {
        }

        tempFileName = "temp_regrann_" + System.currentTimeMillis() + ".jpg";
        tempVideoName = "/temp/temp_regrann_" + System.currentTimeMillis() + ".mp4";

        tempFileFullPathName = file.toString() + File.separator + tempFileName;

        regrannPictureFolder = getAlbumStorageDir("Regrann").getAbsolutePath();

        regrannMultiPostFolder = getAlbumStorageDir("Regrann - Multi Post").getAbsolutePath();

        try {
            // Clear the multipost folder
            File dir = new File(regrannMultiPostFolder);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    try {
                        File toDelete = new File(dir, children[i]);
                        //  RegrannApp._this.getApplicationContext().getContentResolver().delete(Uri.fromFile(toDelete), null, null);
                        toDelete.delete();

                        _this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(toDelete)));

                    } catch (Exception e) {
                        int i4 = 1;
                    }


                    //  getContentResolver().delete(
                    // MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    //MediaStore.Images.Media.DATA + "='"
                    //      + new File(dir, children[i]).getPath() + "'", null);

                }

            }
        } catch (Exception e8) {
        }

        //   scanMultiPostFolder() ;

        isQuickKeep = false;
        isQuickPost = false;
        isAutoSave = false;

        newIntentSeen = false;

        try {

            WVersionManager versionManager = new WVersionManager(this);
            // versionManager.setUpdateUrl("http://jred.co/download-Regrann");
            // // this is the link will execute when update now clicked.
            // default will go to google play based on your package
            // name.
            versionManager.setReminderTimer(60); // this mean
            // checkVersion()
            // will not take
            // effect within 10
            // minutes
            versionManager.setVersionContentUrl("http://r.regrann.com/android/update_info.txt");
            // versionManager.setVersionContentUrl("http://r.regrann.com/testing/update_info.txt"); // your
            // update
            // content
            // url,
            // see
            // the
            // response
            // format
            // below
            versionManager.checkVersion();
        } catch (Exception e) {
        }


        // shareIntent.putExtra("fromExtension", true);
        //shareIntent.putExtra("tempFileName", tempFileFullPathName);

        //   shareIntent.putExtra("mediaType", mediaType);/


        if (getIntent().getBooleanExtra("fromExtension", false)) {


            tempFileFullPathName = getIntent().getStringExtra("tempFileFullPathName");


        }


        inputMediaType = getIntent().getIntExtra("mediaType", 0);
        inputFileName = getIntent().getStringExtra("fileName");
        QuickSaveShare = getIntent().getBooleanExtra("quicksave", false);


        String mode_list = preferences.getString("mode_list", "");

        boolean oldUser = preferences.getBoolean("oldUser", false);

        //   oldUser = true;

        if (mode_list.equals("")) {

            isQuickPost = false;


        } else {

            if (inputMediaType == 0) {

                isAutoSave = preferences.getBoolean("quicksave", false);
                isQuickPost = preferences.getBoolean("quickpost", false);
                isQuickKeep = preferences.getBoolean("quickkeep", false);


            }

        }


        if (isQuickPost)
            RegrannApp.sendEvent("sc_quickpost");
        if (isAutoSave)
            RegrannApp.sendEvent("sc_autosave");
        if (isQuickKeep)
            RegrannApp.sendEvent("sc_quickkeep");


        if (QuickSaveShare)
            isAutoSave = true;

        photoReady = false;

        sdkVersion = android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;


        // Setup up Billing System


        if (isQuickPost || ((inputMediaType == 0) && (isAutoSave || isQuickKeep))) {


            String title = null;


            if (1 == 1 || noAds) {
                setContentView(R.layout.activity_autosave2);
                Toolbar myToolbar = findViewById(R.id.my_toolbar);


                setSupportActionBar(myToolbar);
                getSupportActionBar().setTitle("Regrann");

                startAutoSaveMultiProgress();
                //   Toast toast = Toast.makeText(ShareActivity.this, "Regrann is processing...please wait until completed!", Toast.LENGTH_LONG);
                //  toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                // toast.show();
            } else {


                if (isAutoSave) {

                    showInterstitial = true;

                    setContentView(R.layout.activity_autosave);

                    initAdinCube();

                    Toolbar myToolbar = findViewById(R.id.my_toolbar);


                    setSupportActionBar(myToolbar);
                    getSupportActionBar().setTitle("Regrann Quick Save");


                    spinner = findViewById(R.id.loading_bar);
                    findViewById(R.id.upgradeBtn).setVisibility(View.INVISIBLE);
                    findViewById(R.id.backBtn).setVisibility(View.INVISIBLE);

                    //   mAdView = findViewById(R.id.rect_ad_view);


                    //    AdRequest adRequest = new AdRequest.Builder().
                    //           addTestDevice("B1D20D0F336796629655D59351F179F8").addTestDevice("03B8364E84BB1446DA5C8FDFA9A4E356").build();
                    //   mAdView.loadAd(adRequest);


                }
            }


            //     if (isQuickPost)
            //   setContentView(R.layout.activity_quickpost);


            // if (isQuickKeep)
            // setContentView(R.layout.activity_quickkeep);


            //      spinner = (ProgressBar) findViewById(R.id.progressBar1);
            //    spinner.setVisibility(View.VISIBLE);


            //     settings = (ImageView) findViewById(R.id.settingsView);
            //    settings.setOnClickListener(this);


        } else {


            int numSessions = preferences.getInt("count_sessions", 0);
            if (numSessions < 4)
                noAds = true;

            //    if (BuildConfig.DEBUG)
            //      noAds = false;


            setContentView(R.layout.activity_share_main);


            Toolbar myToolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);


            long prevAdShown = preferences.getLong("instagramAdShownTime", 0);

            long diff = (System.currentTimeMillis() - prevAdShown) / 1000 / 60;
            Log.d("app5", "DIFF save Button :" + diff);


            showInterstitial = !noAds;

            //      if (BuildConfig.DEBUG)
            //         showInterstitial = true ;

            if (showInterstitial) {
                initAdinCube();
                //   findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
                //  refreshAd();
            } else {
                mAdView = findViewById(R.id.adView);
                mAdView.setVisibility(View.GONE);
            }

            //else
            //  findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);


            smallBannerPlacmentId = ((RegrannApp) getApplication()).getBannerPlacementId();


            spinner = findViewById(R.id.loading_bar);
            spinner.setVisibility(View.VISIBLE);

            /**

             try {

             webView = findViewById(R.id.newsView);

             if (noAds) {
             webView.setVisibility(View.GONE);


             } else {
             //   webView.setVisibility(View.VISIBLE);


             try {
             webView.getSettings().setJavaScriptEnabled(true);
             } catch (Exception e7) {
             }


             webView.clearCache(true);

             webView.setOnTouchListener(new View.OnTouchListener() {

            @Override public boolean onTouch(View arg0, MotionEvent event) {
            // TODO Auto-generated method stub
            return (event.getAction() == MotionEvent.ACTION_MOVE);
            }

            });


             webView.setWebViewClient(new WebViewClient() {
             public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {

             Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlx));
             startActivity(browserIntent);


             return true;
             }
             });

             webView.setVerticalScrollBarEnabled(false);
             webView.setHorizontalScrollBarEnabled(false);

             String versionName = "";
             try {
             versionName = ShareActivity.this.getPackageManager()
             .getPackageInfo(ShareActivity.this.getPackageName(), 0).versionName;

             } catch (Exception e5) {
             }


             if (noAds == false)
             webView.loadUrl("http://r.regrann.com/news.html?ver=" + versionName + "&OS=" + android.os.Build.VERSION.SDK_INT);
             else
             webView.loadUrl("http://r.regrann.com/news-noads.html?ver=" + versionName + "&OS=" + android.os.Build.VERSION.SDK_INT);
             }
             } catch (Exception e) {

             RegrannApp.sendEvent("WebView_Error", "", "");
             }
             **/

            //---------------------
            // Code for using webview instagram for private photos

            launchedLogin = false;


            launchedLogin = false;
/**

 class JsObject {
@JavascriptInterface public String toString() {
return "injectedObject";
}
}

 webViewInsta = findViewById(R.id.browser);

 webViewInsta.setVisibility(View.INVISIBLE);
 webViewInsta.getSettings().setJavaScriptEnabled(true);

 webViewInsta.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
 webViewInsta.addJavascriptInterface(new JsObject(), "HtmlViewer");
 webViewInsta.getSettings().setDomStorageEnabled(true);
 webViewInsta.getSettings().setDatabaseEnabled(true);
 webViewInsta.getSettings().setAllowFileAccess(true);
 webViewInsta.getSettings().setAppCacheEnabled(true);
 webViewInsta.addJavascriptInterface(new WebViewJavaScriptInterface(this), "HTMLOUT");


 webViewInsta.setWebViewClient(new WebViewClient() {
 public void onPageFinished(WebView view, String url) {
 webViewInsta.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

 }
 });
 **/

            //---------------------

            caption_suffix = "";


            SharedPreferences.Editor editor = preferences.edit();

            editor.putInt("count_sessions", numSessions + 1);

            editor.apply();


            sharedPref = this.getPreferences(Context.MODE_PRIVATE);


            mainUI = findViewById(R.id.UI_Layout);
            mainUI.setVisibility(View.VISIBLE);


            previewImage = findViewById(R.id.previewImage);
            previewImage.setImageDrawable(null);

             previewImage.setVisibility(View.GONE);

            btnInstagram = findViewById(R.id.instagrambtn);
            btnInstagram.setOnClickListener(this);


            btnInstagramstories = findViewById(R.id.instagramstoriesbtn);
            btnInstagramstories.setOnClickListener(this);


            videoIcon = findViewById(R.id.videoicon);
            videoIcon.setVisibility(View.GONE);

            btnShare = findViewById(R.id.sharebtn);
            btnShare.setOnClickListener(this);

            postlater = findViewById(R.id.postlater);
            postlater.setOnClickListener(this);

            btnCurrentToFeed = findViewById(R.id.currentToFeed);
            btnCurrentToFeed.setOnClickListener(this);

            //     btnCurrentToStory = findViewById(R.id.currentToStory);
            //   btnCurrentToStory.setOnClickListener(this);


            scheduleBtn = findViewById(R.id.scheduleBtn);
            scheduleBtn.setOnClickListener(this);


            btnShare = findViewById(R.id.sharebtn);
            btnShare.setOnClickListener(this);


            btndownloadphoto = findViewById(R.id.downloadphoto);
            btndownloadphoto.setOnClickListener(this);

            mDemoSlider = findViewById(slider);

            mDemoSlider.setVisibility(View.GONE);

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;


            if (inputMediaType != 0) {
                isQuickPost = true;

            }


        }

        if (!isExternalStorageWritable()) {
            showErrorToast(
                    "error",
                    "Storage media is not available. Perhaps you have the phone plugged in as a USB device.  This app needs access to write file. Please try again later.",
                    true);

        } else {

            try {

                title = "";
                if (inputMediaType != 0) {

                    if (Util.isKeepCaption(_this) == false)
                        title = "@Regrann no-crop...";
                    tempFileName = inputFileName;
                    if (inputMediaType == 1)
                        tempFileFullPathName = tempFileName;

                    //   tempVideoFullPathName = tempFileName ;

                    tempFile = new File(tempFileFullPathName);


                    processPhotoImage();


                } else {

                    tempFile = new File(tempFileFullPathName);
                    // Thread thread = new Thread(new Runnable() {
                    // @Override
                    // public void run() {


                    final Intent iv = getIntent();

                    // isJPEG = (iv.getStringExtra("isJPEG").compareTo("yes") == 0);

                    // if (iv.getStringExtra("isJPEG").compareTo("no") == 0) {
                    //    new RetrieveFeedTask().execute();

                    try {


                        if (getIntent().getBooleanExtra("fromExtension", false) == false) {
                            Log.d("mediaURL", iv.getStringExtra("mediaUrl"));
                            String url = iv.getStringExtra("mediaUrl");
                            // String result = GET (t);
                            if (tiktokLink)
                                FetchDownloadUrlService.startFetchDownloadUrlService(_this, url);

                            else
                                startProcessURL(url);
                        } else {

                            handleImageFromExtension();
                        }

                    } catch (Exception e) {
                        showErrorToast("#1 - " + e.getMessage(), getString(R.string.porblemfindingphoto), true);

                    }
                }
                // }

            } catch (Exception e) {
                showErrorToast("#2 - " + e.getMessage(), getString(R.string.therewasproblem), true);

            }
        }


    }


    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (mgr != null) {
                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        long savedJobId = Helper.getDownloadRequestId(context);


                        if (id == savedJobId) {
                            removeProgressDialog();

                            RegrannApp.sendEvent("TikTok Video");
                            if (isAutoSave | isQuickPost | isQuickKeep) {
                                removeProgressDialog();
                                //    copyAllMultiToSave();
                                if (isAutoSave)
                                    copyTempToSave();


                                //     cleanUp();

                                finish();


                                return;

                            }

                            DownloadManager.Query query = new DownloadManager.Query();
                            query.setFilterById(savedJobId);
                            Cursor c = mgr.query(query);
                            if (c.moveToFirst()) {
                                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                                    /**
                                     if (mContext != null) {
                                     historyAdapter.notifyDataSetChanged();
                                     historyRecycler.setVisibility(historyVideos.isEmpty() ? View.GONE : View.VISIBLE);
                                     emptyViewHistory.setVisibility(historyVideos.isEmpty() ? View.VISIBLE : View.GONE);

                                     linkEditText.setText("");
                                     setHomeLayout(0);
                                     setActiveTab(1);
                                     historyRecycler.scrollToPosition(0);
                                     Toast.makeText(mContext, R.string.download_success, Toast.LENGTH_SHORT).show();
                                     }
                                     **/
                                }
                            }
                        }


                    }
                }
            }
        }
    };


    private TiktokPost tiktokPost;


    private BroadcastReceiver myDownloadLinkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            tiktokPost = intent.getParcelableExtra("data");
            if (tiktokPost != null && tiktokPost.getDownloadUrl() != null) {
                if (spinner != null)
                    spinner.setVisibility(View.GONE);
                startProgressDialog();
                isVideo = true;

                tempVideoFile = new File(Environment.getExternalStorageDirectory() + tempVideoName);

                tempVideoFullPathName = tempVideoFile.getPath();


                Helper.downloadTiktokPost(_this, tiktokPost, tempVideoName);

                author = tiktokPost.getUserName();
                title = tiktokPost.getCaption();


                if (isVideo && !isAutoSave && !isQuickKeep && !isQuickPost) {
                    Glide.with(_this).load(tiktokPost.getThumbnail()).apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16))).into(previewImage);

                    videoIcon.setVisibility(View.VISIBLE);
                }

                photoReady = true;


                //  setupPostView();
                int i = 1;
            }
        }
    };


    public void loadPage() {

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    instagramLoggedIn = true;
                    GET(currentURL);
                    //     webViewInsta.loadUrl(currentURL);
                    gotHTML = false;

                } catch (Exception e) {
                }
            }
        });
        //   webViewInsta.loadUrl("https://www.instagram.com/");
        launchedLogin = false;
    }


    String privateHTML = "";

    boolean gotHTML = false;

    /**
     * public class WebViewJavaScriptInterface {
     * <p>
     * private Context context;
     * <p>
     * <p>
     * public WebViewJavaScriptInterface(Context context) {
     * this.context = context;
     * }
     * <p>
     * private AlertDialog myAlertdialog;
     *
     * @JavascriptInterface
     * @SuppressWarnings("unused") public void processHTML(String html) {
     * // process the html as needed by the app
     * <p>
     * if (html.contains("Create an account or log in to Instagram")) {
     * if (launchedLogin == false) {
     * <p>
     * launchedLogin = true;
     * <p>
     * <p>
     * AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
     * <p>
     * builder.setTitle("Private media : You need to login to Instagram!");
     * builder.setMessage("To repost private media you need to first login to your account.");
     * <p>
     * builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
     * <p>
     * public void onClick(DialogInterface dialog, int which) {
     * // Do nothing but close the dialog
     * <p>
     * myAlertdialog.dismiss();
     * Intent myIntent = new Intent(ShareActivity.this, InstagramLogin.class);
     * _this.startActivity(myIntent);
     * <p>
     * <p>
     * }
     * });
     * <p>
     * builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
     * @Override public void onClick(DialogInterface dialog, int which) {
     * launchedLogin = false;
     * // Do nothing
     * myAlertdialog.dismiss();
     * }
     * });
     * <p>
     * myAlertdialog = builder.create();
     * myAlertdialog.show();
     * <p>
     * <p>
     * }
     * } else {
     * if (instagramLoggedIn) {
     * <p>
     * if (gotHTML == false) {
     * gotHTML = true;
     * <p>
     * privateHTML = html;
     * <p>
     * startProcessURL("private");
     * <p>
     * <p>
     * }
     * <p>
     * } else {
     * runOnUiThread(new Runnable() {
     * public void run() {
     * try {
     * instagramLoggedIn = true;
     * webViewInsta.loadUrl(currentURL);
     * gotHTML = false;
     * <p>
     * } catch (Exception e) {
     * }
     * }
     * });
     * <p>
     * <p>
     * }
     * }
     * <p>
     * <p>
     * }
     * @JavascriptInterface public void showMessage(String message) {
     * Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
     * }
     * }
     **/


    @Override
    public void onSliderClick(BaseSliderView slider) {
        //  Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    private void initAdinCube() {

        try {

            if (showInterstitial) {
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId("ca-app-pub-8534786486141147/7512440239");


                boolean show_midrect = preferences.getBoolean("show_midrect", true);
                mAdView = findViewById(R.id.adView);
                if (1 == 2 && isAutoSave == false && show_midrect) {


                    AdRequest adRequest = new AdRequest.Builder().addTestDevice(" U").addTestDevice("03B8364E84BB1446DA5C8FDFA9A4E356").build();
                    mAdView.loadAd(adRequest);
                } else if (isAutoSave == false)
                    mAdView.setVisibility(View.GONE);


                try {


                    AdConfig adConfig = new AdConfig("d966f410-ef78-0137-14e5-0242ac120003");
                    final OguryThumbnailAd floatingAd = new OguryThumbnailAd(ShareActivity.this, adConfig);
                    floatingAd.load(250, 140);


                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x / 2;
                    int height = size.y;

                    Log.d("ogury", "" + width + "  :  " + height);

                    floatingAd.setCallback(new OguryThumbnailAdCallback() {
                        @Override
                        public void onAdNotLoaded() {
                            Log.i("Ogury", "on ad not loaded");
                        }

                        @Override
                        public void onAdLoaded() {
                            Log.i("Ogury", "on ad loaded");
                            if (floatingAd.isLoaded()) {


                                Display display = getWindowManager().getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                int width = size.x / 2;
                                int height = size.y;

                                Log.d("ogury", "" + width + "  :  " + height);


                                floatingAd.show(ShareActivity.this, 20, 450);
                            }
                        }

                        @Override
                        public void onAdNotAvailable() {
                            Log.i("Ogury", "on ad not available");
                        }

                        @Override
                        public void onAdAvailable() {
                            Log.i("Ogury", "on ad available");
                        }

                        @Override
                        public void onAdError(int code) {
                            Log.i("Ogury", "on ad error " + code);

                        }

                        @Override
                        public void onAdClosed() {
                            Log.i("Ogury", "on ad closed");
                        }

                        @Override
                        public void onAdDisplayed() {
                            Log.i("Ogury", "on ad displayed");
                        }
                    });

                } catch (Exception e3) {
                }

                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                        if (autoSaving) {
                            autoSaving = false;
                            finish();
                        }


                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when the interstitial ad is closed.


                        if (autoSaving) {
                            autoSaving = false;
                            finish();
                        }

                        if (instagramBtnClicked) {
                            sendToInstagam();

                        }
                    }
                });


            }
        } catch (Exception e6) {
        }


    }

    final long MAXFILEAGE = 86400000; // 1 day in milliseconds

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        if (mDemoSlider != null)
            mDemoSlider.stopAutoCycle();
        super.onStop();

        shouldLoadAds = false;
    }


    @Override
    public void onResume() {

        super.onResume();


        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(myDownloadLinkReceiver, new IntentFilter("POST_DATA"));
        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        //   IronSource.onResume(this);

        if (mAdView != null) {
            mAdView.resume();
        }

        /**
         if (autoSaving == false) {
         try {
         mInterstitialAd.setAdListener(new AdListener() {
        @Override public void onAdClosed() {
        //  Toast toast = Toast.makeText(ShareActivity.this, saveToastMsg, Toast.LENGTH_LONG);
        // toast.show();
        Log.d("TAG", "Ad Closed");
        sendToInstagam();

        }
        });


         } catch (Exception e) {
         }
         }
         **/


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private void scanMultiPostFolder() {

        try {
            Log.d("app5", "in scanmultipostfolder");
            if (regrannMultiPostFolder != null) {

                File dir = new File(regrannMultiPostFolder);
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    final File[] sortedFileName = dir.listFiles();
                    Arrays.sort(sortedFileName, new Comparator<File>() {
                        @Override
                        public int compare(File object1, File object2) {
                            return object1.getName().compareTo(object2.getName());
                        }
                    });



                    for (int i = sortedFileName.length - 1 ; i >= 0 ; i--) {
                        try {
                            sleep(100) ;
                            File toScan = new File(dir, children[i]);
                            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{toScan.toString()}, null, null);

                            Date lastModDate = new Date(toScan.lastModified());
                            Log.d("app5", "scanning : " + i + ":     " + toScan.toString() + "   " +  lastModDate.toString());

                            //  RegrannApp._this.getApplicationContext().getContentResolver().delete(Uri.fromFile(toDelete), null, null);


                        } catch (Exception e) {
                            int i4 = 1;
                        }

                    }
                }
            }
        } catch (Exception e) {
        }


    }


    private void scanRegrannFolder() {


        try {
            if (regrannPictureFolder != null) {
                File dir = new File(regrannPictureFolder);
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {
                        try {
                            File toScan = new File(dir, children[i]);
                            Long lastmodified = toScan.lastModified();

                            if (lastmodified + MAXFILEAGE > System.currentTimeMillis()) {
                                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{toScan.toString()}, null, null);
                            }

                        } catch (Exception e) {
                            int i4 = 1;
                        }

                    }
                }
            }
        } catch (Exception e) {
        }


    }


    private void addWatermarkToVideo() {
/**

 Videowatermark videoProcessing2 = new Videowatermark(this, bundle2.getString("source"), bundle2.getString("destination"), bundle2.getString(C.BUNDLE_WATERMARK), new OnVideoProcessingListener() {
 public void onComplete() {
 ShareActivity.this.runOnUiThread(new Runnable() {
 public void run() {

 }
 });
 }

 public void onError(final String str) {
 ShareActivity.this.runOnUiThread(new Runnable() {
 public void run() {
 try {

 } catch (Exception unused) {
 }
 }
 });
 }

 public void onProgressUpdate(final int i) {
 ShareActivity.this.runOnUiThread(new Runnable() {
 public void run() {

 }
 });
 }
 });
 **/
    }

    private void showMultiDialog() {


        int numWarnings = preferences.getInt("multiWarning", 0);
        String caption = Util.prepareCaption(title, author, _this.getApplication().getApplicationContext(), caption_suffix, false);


        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Post caption", caption);

        Objects.requireNonNull(clipboard).setPrimaryClip(clip);

        if (numWarnings < 8) {

            Log.d("regrann", "Numwarnings  2 : " + numWarnings);
            numWarnings++;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("multiWarning", numWarnings);

            editor.commit();

            final Dialog dialog = new Dialog(_this, R.style.FullHeightDialog);
            dialog.setContentView(R.layout.multi_dialog);

            dialog.setCancelable(true);
            //there are a lot of settings, for dialog, check them all out!


            //set up button
            Button button = dialog.findViewById(R.id.okbtn);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //  cleanUp();
                            finish();
                        }
                    }, 500);
                    dialog.dismiss();


                }
            });
            //now that the dialog is set up, it's time to show it
            dialog.show();

        } else {
            //    cleanUp();
            finish();
            return;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.full_menu, menu);


        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (noAds || isAutoSave)
            menu.findItem(R.id.action_removeads).setVisible(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_removeads:
                Intent i = new Intent(_this, UpgradeActivity.class);
                i.putExtra("from_qs_screen", true);
                startActivity(i);

                RegrannApp.sendEvent("rmain_upgrade_btn");
                return true;
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                RegrannApp.sendEvent("rmain_settings_btn");
                // TODO Auto-generated method stub


                startActivity(new Intent(this, SettingsActivity2.class));


                return true;

            case R.id.action_help:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.regrann.com/support"));
                startActivity(browserIntent);
                RegrannApp.sendEvent("rmain_help_btn");

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void finish() {
        cleanUp();
        super.finish();


    }


    @Override
    public void onBackPressed() {
        try {

            if (noAds == false && isAutoSave)
                return;

            super.onBackPressed();

            //   overridePendingTransition(R.anim.slide_up_anim, R.anim.slide_down_anim);


        } catch (Exception e) {
        }
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void deleteOldFiles(String dir) {

        KeptListAdapter dbHelper;

        dbHelper = KeptListAdapter.getInstance(this);

        Cursor c = dbHelper.fetchAllItems();
        c.moveToFirst();
        ArrayList<String> postLaterFilenames = new ArrayList<String>();
        for (int i = 0; i < c.getCount(); i++) {
            postLaterFilenames.add(c.getString(c.getColumnIndex("photo")));
            c.moveToNext();


        }
        c.close();


        File folder = new File(Environment.getExternalStorageDirectory() + "/" + dir);

        try {
            File[] filenamestemp = folder.listFiles();
            Calendar oneDayAgo = Calendar.getInstance();


            oneDayAgo.add(Calendar.HOUR, -24);

            for (int i = 0; i < filenamestemp.length; i++) {


                if (filenamestemp[i].getAbsolutePath().contains("temp_regrann")) {

                    Date lastModDate = new Date(filenamestemp[i].lastModified());

                    if (lastModDate.before(oneDayAgo.getTime())) {
                        if (!postLaterFilenames.contains(filenamestemp[i].getPath())) {
                            Log.d("TAG", "Deleted");
                            filenamestemp[i].delete();
                        }

                    }
                }
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.

        ArrayList<String> toBeScanned = new ArrayList<String>();
        toBeScanned.add(folder.getPath());

        String[] toBeScannedStr = new String[toBeScanned.size()];
        toBeScannedStr = toBeScanned.toArray(toBeScannedStr);

        MediaScannerConnection.scanFile(getApplicationContext(), toBeScannedStr, null, null);

    }


    private void handleImageFromExtension() {
        String uriStr = tempFile.getAbsolutePath();

        title = getIntent().getStringExtra("caption");
        author = getIntent().getStringExtra("author");

        final Uri uri = Uri.parse(uriStr);
        final String path = uri.getPath();


        //  final Drawable drawable = Drawable.createFromPath(path);
        previewImage.setImageBitmap(Util.decodeFile(new File(path)));
        //  if (isVideo)
        //     LoadVideo();
        //  else

        //    checkForRatingRequest();


        photoReady = true;

        if (spinner != null)
            spinner.setVisibility(View.GONE);


        mainUI.setVisibility(View.VISIBLE);


    }

    private void showPasteDialog(final Intent intent) {

        final Dialog dialog = new Dialog(_this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.paste_dialog);

        dialog.setCancelable(true);
        //there are a lot of settings, for dialog, check them all out!


        //set up button
        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(intent);
                } catch (Exception e) {

                    final Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setPackage("com.instagram.android");

                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);


                    startActivity(shareIntent);
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
                dialog.dismiss();


            }
        });
        //now that the dialog is set up, it's time to show it
        dialog.show();
    }


    private void quickPostSendToInstagram() {


        try {

            // flurryAgent.logEvent("Instagram button pressed");
            // FlurryAgent.logEvent("Click Instagram");
            Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
            if (intent != null) {

                Handler h = new Handler(_this.getMainLooper());
                h.post(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            final Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);

                            shareIntent.setPackage("com.instagram.android");
                            //    shareIntent.setClassName("com.instagram.android",instagram_activity);


                            shareIntent.setClassName(
                                    "com.instagram.android",
                                    "com.instagram.share.handleractivity.ShareHandlerActivity");


                            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);


                            if (inputMediaType == 0) {
                                String caption = Util.prepareCaption(title, author, caption_suffix, _this.getApplication().getApplicationContext(), tiktokLink);

                                shareIntent.putExtra(Intent.EXTRA_TEXT, caption);


                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Post caption", caption);
                                Objects.requireNonNull(clipboard).setPrimaryClip(clip);

                            } else {
                                String caption = "";
                                if (Util.isKeepCaption(_this) == false)
                                    caption = "@regrann no-crop:";


                                shareIntent.putExtra(Intent.EXTRA_TEXT, caption);


                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Post caption", caption);
                                Objects.requireNonNull(clipboard).setPrimaryClip(clip);


                            }


                            if (isVideo) {
                                shareIntent.setType("video/*");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempVideoFile));
                            } else {
                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));

                                shareIntent.setType("image/*");
                            }

                            //     shareIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //       shareIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


                            int numWarnings = preferences.getInt("captionWarning", 0);

                            if (numWarnings < 3 && inputMediaType == 0) {

                                numWarnings++;
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("captionWarning", numWarnings);

                                editor.apply();

                                showPasteDialog(shareIntent);

                            } else {
                                startActivity(shareIntent);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 2000);

                            }


                        } catch (Exception e) {
                        }
                    }
                });


            } else {
                // bring user to the market to download the app.
                showErrorToast("#3 - ", getString(R.string.therewasproblem));

            }

        } catch (Exception e) {
            showErrorToast("#4 - " + e.getMessage(), getString(R.string.therewasproblem));

        }

    }


    private void showGetRatingDialog() {

        try {


            RegrannApp.sendEvent("sc_rating_show");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);

            // set dialog message
            alertDialogBuilder.setTitle("Rate Regrann");
            alertDialogBuilder.setIcon(R.drawable.ic_launcher);

            alertDialogBuilder.setMessage(_this.getString(R.string.rateText))
                    .setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, close
                    // current activity
                    // flurryAgent.logEvent("Ok to rate selected");
                    try {

                        RegrannApp.sendEvent("sc_rating_ok");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=com.jaredco.regrann"));
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }

            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        } catch (Exception e) {
        }

    }

    private void addToCount(int num) {

        count = sharedPref.getInt("countOfRuns", 0);

        count = count + num;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("countOfRuns", count);

        count2 = sharedPref.getInt("countOfRuns2", 0);

        count2 = count2 + num;

        editor.putInt("countOfRuns2", count2);

        editor.apply();

    }

    private void checkForRatingRequest() {
        try {

            sharedPref = this.getPreferences(Context.MODE_PRIVATE);

            count = sharedPref.getInt("countOfRuns", 0);
            count2 = sharedPref.getInt("countOfRuns2", 0);

            addToCount(1);

            Log.d("app5", "Count of Runs :" + count);

            if (count != 4 && count != 16) {

                // check for update every second time


                return;
            }

            RegrannApp.sendEvent("sc_rating_howisexp");


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);

            // flurryAgent.logEvent("Rating Good or Bad Dialog");
            // set dialog message
            alertDialogBuilder.setIcon(R.drawable.ic_launcher);

            alertDialogBuilder.setMessage(_this.getString(R.string.rateHowIsExperience)).setCancelable(false)
                    .setNegativeButton(_this.getString(R.string.bad), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing

                            RegrannApp.sendEvent("sc_rate_badbtn");

                            dialog.cancel();
                        }
                    })
                    .setPositiveButton(_this.getString(R.string.good), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            // flurryAgent.logEvent("Good Selected");

                            RegrannApp.sendEvent("sc_rate_goodbtn");


                            showGetRatingDialog();
                        }
                    });

            // create alert dialog
            rateRequestDialog = alertDialogBuilder.create();

            // show it
            rateRequestDialog.show();


        } catch (Exception e) {
        }

    }


    private Bitmap mark(Bitmap src, String watermark, Point location, int color, int alpha,
                        int size, boolean underline) {
        Bitmap result = null;
        try {


            boolean customWatermark = preferences.getBoolean("custom_watermark", false);
            Bitmap wm5 = null;
            if (customWatermark) {

                Uri imageUri = Uri.parse(preferences.getString("watermark_imagefile", ""));
                wm5 = BitmapFactory.decodeFile(imageUri.getPath());

            }


            int w = src.getWidth();
            int h = src.getHeight();
            result = Bitmap.createBitmap(w, h, src.getConfig());

            Canvas canvas = new Canvas(result);

            Paint paint = new Paint();


            canvas.drawBitmap(src, 0, 0, null);


            if (customWatermark == false) {
                int stripHeight = (int) (0.08 * src.getHeight());
                // get profile picture of author
                URL profilePicURL = new URL(profile_pic_url);
                Bitmap profilePicBMP = BitmapFactory.decodeStream(profilePicURL.openConnection().getInputStream());
                profilePicBMP = Bitmap.createScaledBitmap(profilePicBMP, stripHeight, stripHeight, false);


                Rect bounds = new Rect();
                int textSize = (int) (0.04 * src.getHeight());

                paint.setTextSize(textSize);

                paint.getTextBounds(watermark, 0, watermark.length(), bounds);

                int bw = bounds.width();


                Rect r = new Rect(0, src.getHeight(), stripHeight + bw + 45, src.getHeight() - stripHeight);


                paint.setStyle(Paint.Style.FILL);

                paint.setColor(Color.BLACK);

                if (preferences.getString("alpha_choice", "Transparent").equals("Transparent")) {
                    paint.setAlpha(30);
                }

                canvas.drawRect(r, paint);


                canvas.drawBitmap(profilePicBMP, 0, result.getHeight() - stripHeight, null);
                paint.setColor(Color.WHITE);

                // paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                paint.setTextSize(textSize);

                paint.setAntiAlias(true);
                paint.setUnderlineText(underline);

                canvas.drawText(watermark, stripHeight + 20, result.getHeight() - stripHeight / 2 + textSize / 3, paint);

            } else {
                RegrannApp.sendEvent("custom_watermark_shown");

                //     showErrorToast ("Attemping to add watermark", "Preparing Watermark", false);


                int stripHeight = (int) (0.15 * src.getHeight());

                float scale = (float) stripHeight / (float) wm5.getHeight();

                float xTranslation = 15.0f;
                float yTranslation = src.getHeight() - (stripHeight / 2.0f) - src.getHeight() * 0.08f;

                Matrix transformation = new Matrix();
                transformation.postTranslate(xTranslation, yTranslation);
                transformation.preScale(scale, scale);

                Paint paint2 = new Paint();
                paint2.setFilterBitmap(true);

                canvas.drawBitmap(wm5, transformation, paint2);
            }


        } catch (Throwable e) {
            //   showErrorToast ("Problem", "Error: "+ e.getMessage(), false);
            return src;
        }


        return result;
    }


    private boolean createVideoWatermark(Bitmap src, String watermark, Point location,
                                         int color, int alpha, int size, boolean underline) {
        Bitmap result = null;
        try {
            int stripHeight = (int) (0.08 * src.getHeight());
            // get profile picture of author
            URL profilePicURL = new URL(profile_pic_url);
            Bitmap profilePicBMP = BitmapFactory.decodeStream(profilePicURL.openConnection().getInputStream());
            profilePicBMP = Bitmap.createScaledBitmap(profilePicBMP, stripHeight, stripHeight, false);


            int w = src.getWidth();
            int h = stripHeight;
            result = Bitmap.createBitmap(w, h, src.getConfig());

            Canvas canvas = new Canvas(result);

            Paint paint = new Paint();


            canvas.drawBitmap(src, 0, 0, null);


            Rect bounds = new Rect();
            int textSize = (int) (0.04 * src.getHeight());

            paint.setTextSize(textSize);

            paint.getTextBounds(watermark, 0, watermark.length(), bounds);

            int bw = bounds.width();


            Rect r = new Rect(0, src.getHeight(), stripHeight + bw + 45, src.getHeight() - stripHeight);

            paint.setStyle(Paint.Style.FILL);

            paint.setColor(Color.BLACK);
            paint.setAlpha(30);
            canvas.drawRect(r, paint);

            canvas.drawBitmap(profilePicBMP, 0, result.getHeight() - stripHeight, null);


            paint.setColor(Color.WHITE);

            // paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            paint.setTextSize(textSize);

            paint.setAntiAlias(true);
            paint.setUnderlineText(underline);
            canvas.drawText(watermark, stripHeight + 20, result.getHeight() - stripHeight / 2 + textSize / 3, paint);


            try (FileOutputStream out = new FileOutputStream("watermark.png")) {
                result.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {

            return false;
        }


        return true;
    }


    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
    private void refreshAd() {
        //   refresh.setEnabled(false);


        // if ( 1 == 1)
        //   return ;
//  ca-app-pub-3940256099942544/2247696110

        AdLoader.Builder builder;

        if (BuildConfig.DEBUG) {
            builder = new AdLoader.Builder(this, "/6499/example/native");

        } else {
            builder = new AdLoader.Builder(this, "ca-app-pub-8534786486141147/9421739461");
        }


        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout =
                        findViewById(R.id.fl_adplaceholder);

                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {


            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().addTestDevice("B1D20D0F336796629655D59351F179F8").addTestDevice("03B8364E84BB1446DA5C8FDFA9A4E356").build());


    }


    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView   the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView
            adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        //  MediaView mediaView = adView.findViewById(R.id.ad_media);
        //  adView.setMediaView(mediaView);

        // Set other ad assets.
        try {
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline2));
            adView.setBodyView(adView.findViewById(R.id.ad_body2));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            //   adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            //   adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            // The headline is guaranteed to be in every UnifiedNativeAd.
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            adView.getHeadlineView().setVisibility(View.VISIBLE);

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.GONE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }


            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.GONE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.GONE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad. The SDK will populate the adView's MediaView
            // with the media content from this native ad.
            adView.setNativeAd(nativeAd);
        } catch (Exception e) {
        }


    }


    @Override
    protected void onDestroy() {


        try {
            unregisterReceiver(onComplete);
        } catch (Exception e) {
        }


        if (nativeAd != null) {
            nativeAd.destroy();
        }

        if (mAdView != null) {
            mAdView.destroy();
        }


        if (adView != null) {
            adView.destroy();
        }


        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    static int k;


    private void startProcessURL(String url) {

        String jsonRes = "";
        JSONObject json = null;


        currentURL = url;

        GET(url);


    }


    protected void postExecute(final String result) {

        try {


            runOnUiThread(new Runnable() {
                public void run() {


                    if (result.compareTo("private") == 0)
                        return;


                    if (result.compareTo("error") != 0) {
                        if (result.compareTo("multi") == 0) {
                            photoReady = true;

                            if (isAutoSave | isQuickPost | isQuickKeep) {
                                removeProgressDialog();
                                //    copyAllMultiToSave();
                                showMultiDialog();


                                return;

                            }

                            if (spinner != null)
                                spinner.setVisibility(View.GONE);

                            if (previewImage != null) {
                                previewImage.setVisibility(View.GONE);
                            }

                            if (mDemoSlider != null)

                                mDemoSlider.setVisibility(View.VISIBLE);

                            if (mainUI != null)
                                mainUI.setVisibility(View.VISIBLE);


                            if (isQuickPost) {
                                showMultiDialog();


                                return;
                            }
                            if (isQuickKeep) {
                                Toast toast = Toast.makeText(ShareActivity.this, "Sorry. Quick Post Later not available for Multi-Posts", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                                toast.show();
                                finish();
                                return;
                            }
                            isQuickKeep = false;
                            isQuickPost = false;


                        } else {


                            try {
                                //     if (isVideo)
                                //       if (editPhotoBtn != null)
                                //         editPhotoBtn.setVisibility(View.GONE);


                                if (isQuickKeep) {

                                    if (!isVideo) {

                                        KeptListAdapter db = KeptListAdapter.getInstance(_this);


                                        db.addItem(new InstaItem(title, tempFileFullPathName, tempVideoFullPathName, author));

                                        clearClipboard();

                                        Toast toast = Toast.makeText(ShareActivity.this, R.string.postlaterconfirmtoast, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                                        toast.show();
                                        finish();
                                    } else
                                        LoadVideo();


                                } else if (isAutoSave) {
                                    if (!isVideo) {
                                        copyTempToSave();
                                        //   finish();
                                    } else
                                        LoadVideo();

                                } else if (isQuickPost) {
                                    if (!isVideo) {

                                        quickPostSendToInstagram();
                                    } else
                                        LoadVideo();


                                } else {

                                    String uriStr = tempFile.getAbsolutePath();

                                    final Uri uri = Uri.parse(uriStr);
                                    final String path = uri.getPath();


                                    //  final Drawable drawable = Drawable.createFromPath(path);
                                    previewImage.setImageBitmap(Util.decodeFile(new File(path)));
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                                        findViewById(R.id.useShareLink).setVisibility(View.VISIBLE);

                                    }

                                    if (isVideo) {
                                        LoadVideo();
                                        videoIcon.setVisibility(View.VISIBLE);
                                    }

                                    photoReady = true;


                                    //  diff = 8 ;
                                    if (showInterstitial) {




                                                        runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                try {

                                                                    mainUI.setVisibility(View.VISIBLE);



                                                                    if (spinner != null) {

                                                                        spinner.setVisibility(View.GONE);

                                                                    }

                                                                    previewImage.setVisibility(View.VISIBLE);

                                                                    checkForRatingRequest();
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        });



                                    } else {
                                        if (spinner != null) {

                                            spinner.setVisibility(View.GONE);

                                        }

                                        mainUI.setVisibility(View.VISIBLE);
                                        previewImage.setVisibility(View.VISIBLE);
                                        checkForRatingRequest();
                                    }


                                }


                            } catch (Exception e) {

                                showErrorToast("#6 - " + e.getMessage(), "#6 " + getString(R.string.therewasproblem), true);

                            }
                        }


                    }


                }
            });
        } catch (Exception e) {

            showErrorToast("#7 - " + e.getMessage(), "#7 " + getString(R.string.therewasproblem), true);

        }

        //    if (spinner != null)
        //     spinner.setVisibility(View.GONE);

    }


    private void processJSON(String jsonRes) {

        JSONObject json = null;

        try {

            if (jsonRes.equals("private")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);

                builder.setTitle("You need to login to Instagram!");
                builder.setMessage("To repost private media you need to first login to Instagram.  The app will not see your username or password");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dialog.dismiss();
                        Intent myIntent = new Intent(ShareActivity.this, InstagramLogin.class);
                        _this.startActivity(myIntent);

                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        launchedLogin = false;
                        // Do nothing
                        dialog.dismiss();
                        finish();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return;
            }


            String thumbURL = null;


            json = new JSONObject(jsonRes);


            author = json.getJSONObject("owner").getString("username");

            profile_pic_url = json.getJSONObject("owner").getString("profile_pic_url");


            title = "";
            try {
                JSONObject json6 = json.getJSONObject("edge_media_to_caption");
                JSONArray json7 = json6.getJSONArray("edges");
                JSONObject json8 = json7.getJSONObject(0);
                JSONObject json9 = json8.getJSONObject("node");

                title = json9.getString("text");
            } catch (Exception e) {
            }

            url = json.getString("display_url");

            isVideo = json.getBoolean("is_video");


            if (isVideo) {


                videoURL = json.getString("video_url");

            }


        } catch (Exception e3) {


            // [START initialize_database_ref]
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // [END initialize_database_ref]

// Create a new user with a first and last name
            Map<String, Object> error = new HashMap<>();
            error.put("message", e3.getMessage());
            error.put("HTML", jsonRes.toString());
            error.put("url", currentURL);


// Add a new document with a generated ID
            db.collection("errors")
                    .add(error)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            showErrorToast("Problem", "There seems to be a problem.  Please try again later.", true);

            //   return "error";

        }


        if (json != null)
            if (json.isNull("edge_sidecar_to_children") == false) {
                try {
                    isMulti = true;


                    final JSONArray pics = json.getJSONObject("edge_sidecar_to_children").getJSONArray("edges");

                    final Long currTime = System.currentTimeMillis();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                boolean isScreen = true;

                                if (isQuickPost || isAutoSave || isQuickKeep) {
                                    isScreen = false;
                                }
                                if (isScreen) {

                                    btnShare.setVisibility(View.GONE);
                                    spinner.setVisibility(View.GONE);

                                    postlater.setVisibility(View.INVISIBLE);

                                    btnInstagramstories.setVisibility(View.GONE);

                                    btnCurrentToFeed.setVisibility(View.VISIBLE);
                                    //       btnCurrentToStory.setVisibility((View.VISIBLE));


                                    previewImage.setVisibility(View.GONE);
                                    mDemoSlider.setVisibility(View.VISIBLE);
                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);


                                }


                                String folder = regrannMultiPostFolder;


                                for (int i = 0; i < pics.length(); i++) {
                                    TextSliderView sliderView = new TextSliderView(_this);

                                    if (isScreen)
                                        sliderView.image(pics.getJSONObject(i).getJSONObject("node").getString("display_url"));


                                    String fname = "";

                                    if (pics.getJSONObject(i).getJSONObject("node").getString("is_video").equals("true")) {
                                        if (isScreen)
                                            sliderView.description("Video #" + (i + 1));

                                        fname = folder + File.separator + author + "-" + currTime + i + ".mp4";

                                    } else {
                                        if (isScreen)
                                            sliderView.description("Photo #" + (i + 1));

                                        fname = folder + File.separator + author + "-" + currTime + i + ".jpg";

                                    }
                                    Log.d("app5", " fname  = " + fname);
                                    if (isScreen) {
                                        sliderView.bundle(new Bundle());
                                        sliderView.getBundle()
                                                .putString("url", pics.getJSONObject(i).getJSONObject("node").getString("display_url"));
                                        sliderView.getBundle()
                                                .putString("fname", fname);
                                        sliderView.getBundle()
                                                .putString("is_video", pics.getJSONObject(i).getJSONObject("node").getString("is_video"));


                                        sliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);


                                        mDemoSlider.addSlider(sliderView);
                                    }


                                }
                            } catch (Exception e) {
                                int i9 = 43;
                            }
                        }
                    });

                    // final int index = i;
                    //  final File tmpFile = new File(fname);
                    try {
                        String folder = regrannMultiPostFolder;


                        if (isAutoSave) {


                            folder = regrannPictureFolder;

                        }

                        String fname;

                        //   sleep (2000);
                        for (int i = pics.length() - 1; i >= 0; i--) {
                            //  sleep(30);
                            Log.d("app5", "in loop " + i);


                            if (pics.getJSONObject(i).getJSONObject("node").getString("is_video").equals("true")) {

                                fname = folder + File.separator + author + "-" + currTime + (pics.length() - i - 1) + ".mp4";

                            } else {

                                fname = folder + File.separator + author + "-" + currTime + (pics.length() - i - 1) + ".jpg";

                            }

                            Log.d("app5", " fnames " + i + "   " + fname);
                            final File tmpFile = new File(fname);
                            if (pics.getJSONObject(i).getJSONObject("node").getString("is_video") == "false") {

                                downloadImage(pics.getJSONObject(i).getJSONObject("node").getString("display_url"), tmpFile);
                            } else {


                                numOfMultiVideos++;

                                LoadMultiVideo(pics.getJSONObject(i).getJSONObject("node").getString("video_url"), tmpFile);


                            }
                        }
                    } catch (Exception e) {
                    }


                    RegrannApp.sendEvent("sc_multiphoto");


                    postExecute("multi");
                    return;
                } catch (Exception e) {

                    showErrorToast("#5a - " + e.getMessage(), getString(R.string.porblemfindingphoto), true);

                    return;
                }
            }


        try {
            Bitmap bitmap = null;
            try {
                URL imageurl = new URL(url);
                originalBitmapBeforeNoCrop = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
                bitmap = originalBitmapBeforeNoCrop;
            } catch (Throwable e) {
                showErrorToast("Out of memory", "Sorry not enough memory to continue", true);

            }

            try {
                if (preferences.getBoolean("watermark_checkbox", false) ||
                        preferences.getBoolean("custom_watermark", false)) {
                    Point p = new Point(10, (bitmap != null ? bitmap.getHeight() : 0) - 10);
                    int textSize = 20;
                    if (bitmap.getHeight() > 640)
                        textSize = 50;
                    bitmap = mark(bitmap, author, p, Color.YELLOW, 180, textSize, false);
                }
            } catch (Exception e99) {

            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, bytes);


            // no crop if not square ??


            // write the bytes in file
            FileOutputStream fo = new FileOutputStream(tempFile);
            fo.write(bytes.toByteArray());

            // remember close de FileOutput
            fo.close();


            if (isVideo) {
                RegrannApp.sendEvent("sc_video");

                if (preferences.getBoolean("watermark_checkbox", false) ||
                        preferences.getBoolean("custom_watermark", false)) {
                    Point p = new Point(10, bitmap.getHeight() - 10);
                    int textSize = 20;
                    if (bitmap.getHeight() > 640)
                        textSize = 50;
                    createVideoWatermark(bitmap, author, p, Color.YELLOW, 180, textSize, false);
                }


            } else

                RegrannApp.sendEvent("sc_photo");


        } catch (
                Exception e) {

            showErrorToast("#5b - " + e.getMessage(), getString(R.string.porblemfindingphoto), true);

            return;
        }

        postExecute("");

    }


    private void downloadImage(String url, final File tmpfile) {
        try {
            //    URL imageurl = new URL(url);

            Log.d("app5", "Downloading : " + url);

            FutureTarget<Bitmap> futureBitmap = Glide.with(RegrannApp._this)
                    .asBitmap()
                    .load(url)
                    .submit();

            if (futureBitmap == null) {

                showErrorToast("#72 - ", "There was a problem downloading the photo.  Please try again.", true);
                return;
            }

            try {
                originalBitmapBeforeNoCrop = futureBitmap.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            try {

                // originalBitmapBeforeNoCrop = resource;
                Bitmap bitmap = originalBitmapBeforeNoCrop;


                if (preferences.getBoolean("watermark_checkbox", false) ||
                        preferences.getBoolean("custom_watermark", false)) {
                    Point p = new Point(10, bitmap.getHeight() - 10);
                    int textSize = 20;
                    if (bitmap.getHeight() > 640)
                        textSize = 50;
                    bitmap = mark(bitmap, author, p, Color.YELLOW, 180, textSize, false);
                }


                bitmap.compress(Bitmap.CompressFormat.JPEG, 99, bytes);

            } catch (Exception e) {
                RegrannApp.sendEvent("sc_bitmap null");

                showErrorToast("#72 - ", "There was a problem downloading the photo.  Please try again.", true);
                return;
            }

            // no crop if not square ??

            try {
                // write the bytes in file
                FileOutputStream fo = new FileOutputStream(tmpfile);
                fo.write(bytes.toByteArray());

                // remember close de FileOutput
                fo.close();
            } catch (Exception e) {
            }



            //   MediaStore.Images.Media.insertImage( RegrannApp._this.getApplicationContext().getContentResolver(), tmpfile.getPath().toString(),
            //         tmpfile.getName(), "Regrann MultiPost");


            //   originalBitmapBeforeNoCrop = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());



        } catch (OutOfMemoryError e) {
            int y = 1;

        }


    }

    boolean loadingFinished = true;
    boolean redirect = false;
    private boolean alreadyFinished;

    WebView webview;

    public void GET(final String urlIn) {

        class MyJavaScriptInterface {

            private Context ctx;

            MyJavaScriptInterface(Context ctx) {
                this.ctx = ctx;
            }

            @JavascriptInterface
            public void showHTML(String html) {
                try {

                    Log.d("app5", "in showhtml ");
                    int start_shortcode_media = html.indexOf("shortcode_media");

                    if (start_shortcode_media > -1) {
                        try {
                            int end_pos = html.indexOf(";</script>", start_shortcode_media);
                            final String json = html.substring((start_shortcode_media + 17), end_pos);


                            Log.d("app5", "JSON : ");


                            processJSON(json);
                        } catch (Exception e) {
                            showErrorToast("shortcode_media error: ", "There was a probilem : " + e.getMessage().toString(), true);
                        }
                    } else {
                        processJSON("private");
                    }
                } catch (Exception e) {
                    showErrorToast("There was a problem ", "There was a problem : " + html, true);
                }


            }

        }


        InputStream inputStream = null;
        // String result = "";
        try {

            runOnUiThread(new Runnable() {
                public void run() {

                    Log.d("app5", "I am the UI thread ");


                    if (isAutoSave | isQuickKeep | isQuickPost)
                        webview = (WebView) findViewById(R.id.browser2);
                    else

                        webview = (WebView) findViewById(R.id.browser);

                    webview.getSettings().setLoadWithOverviewMode(true);
                    webview.getSettings().setUseWideViewPort(true);
                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.addJavascriptInterface(new MyJavaScriptInterface(ShareActivity.this), "HtmlViewer");
                    webview.getSettings().setLoadWithOverviewMode(true);


                    webview.setWebViewClient(new WebViewClient() {


                        @Override
                        public void onPageStarted(WebView view, String url,
                                                  android.graphics.Bitmap favicon) {
                            Log.d("app5", "in page started ");


                        }


                        @Override
                        public void onPageFinished(WebView view, String url) {

                            Log.d("app5", "in page finisihed ");
                            if (alreadyFinished == false) {
                                Log.d("app5", "in already finisihed");
                                webview.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                            }
                            alreadyFinished = true;


                        }
                    });


                    webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    alreadyFinished = false;

                    webview.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            webview.loadUrl(urlIn);
                        }
                    }, 500);


                }
            });


        } catch (Exception e) {
            Log.d("app5", e.getMessage());
            //  return "is_private";


        }

    }


    private void clearClipboard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", "");
            Objects.requireNonNull(clipboard).setPrimaryClip(clip);
        } catch (Exception e) {
        }
    }

    /* 3) Handle the results */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                /* 4) Make a case for the request code we passed to startActivityForResult() */
                case 1:

                    try {

                        Uri mImageUri = data.getData();


                        previewImage.setImageBitmap(Util.decodeFile(new File(mImageUri != null ? mImageUri.getPath() : null)));

                        //  final Drawable drawable = Drawable.createFromPath(mImageUri.getPath());
                        //    previewImage.setImageDrawable(drawable);


                    } catch (Exception e) {
                    }


                    break;
            }
        }
    }


    public void onClickUpgradeButton(View v) {


        RegrannApp.sendEvent("qs_upgrade_btn_clk", "", "");


        Intent i = new Intent(_this, UpgradeActivity.class);
        i.putExtra("from_qs_screen", true);
        startActivity(i);
        finish();


    }


    public void onClickBackToInstagram(View v) {
        RegrannApp.sendEvent("qs_back_to_instagram", "", "");
        finish();
    }


    private void cleanUp() {


        try {
            if (originalBitmapBeforeNoCrop != null)
                originalBitmapBeforeNoCrop.recycle();

            if (pd != null)
                pd.cancel();
        } catch (Exception e) {
        }

        try {
            removeProgressDialog();
            scanRegrannFolder();
            scanMultiPostFolder();

            if (webViewInsta != null)
                webViewInsta.destroy();
        } catch (Exception e) {
        }
    }

    @Override
    public void onPause() {

        super.onPause();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(myDownloadLinkReceiver);
        unregisterReceiver(downloadCompleteReceiver);


        //     IronSource.onPause(this);

        if (mAdView != null) {
            mAdView.pause();
        }


        if (adView != null) {
            adView.pause();
        }


    }


    private void processPhotoImage() {


        try {


            if (isAutoSave) {
                copyTempToSave();
                //   finish();

            } else if (isQuickPost) {


                if (inputMediaType == MEDIA_IMAGE)
                    quickPostSendToInstagram();
                else {
                    isVideo = true;

                    String uriStr = tempFile.getAbsolutePath();
                    final Uri uri = Uri.parse(uriStr);
                    final String path = uri.getPath();
                    tempVideoFile = new File(path);

                    quickPostSendToInstagram();
                }


            } else {
                //  btndownloadphoto.setVisibility(View.GONE);


                String uriStr = tempFile.getAbsolutePath();
                final Uri uri = Uri.parse(uriStr);
                final String path = uri.getPath();
                if (inputMediaType == MEDIA_IMAGE) {

                    previewImage.setImageBitmap(Util.decodeFile(new File(path)));


                    //   final Drawable drawable = Drawable.createFromPath(path);
                    // previewImage.setImageDrawable(drawable);
                } else {
                    isVideo = true;

                    tempVideoFile = new File(path);

                    //    videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
                    //  videoPlayer.setOnPreparedListener(_this);
                    // videoPlayer.setOnCompletionListener(_this);
                    //  videoPlayer.setKeepScreenOn(true);

//                    videoPlayer.setVideoPath(path);
                    //                  videoPlayer.setVisibility(View.VISIBLE);
                    //    previewImage.setVisibility(View.GONE);

                }


                if (spinner != null)
                    spinner.setVisibility(View.GONE);
                photoReady = true;
            }

        } catch (Exception e) {
            showErrorToast("#9 - " + e.getMessage(), "#9 " + getString(R.string.therewasproblem));

        }

    }


    /**
     * This callback will be invoked when the file is ready to play
     */
    @Override
    public void onPrepared(MediaPlayer vp) {

        // Don't start until ready to play.  The arg of seekTo(arg) is the start point in
        // milliseconds from the beginning. Normally we would start at the beginning but,
        // for purposes of illustration, in this example we start playing 1/5 of
        // the way through the video if the player can do forward seeks on the video.

        if (videoPlayer.canSeekForward()) videoPlayer.seekTo(videoPlayer.getDuration() / 5);
        //videoPlayer.start();
        vp.setVolume(0f, 0f);
    }

    /**
     * This callback will be invoked when the file is finished playing
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        // Statements to be executed when the video finishes.
        this.finish();
    }

    /**
     * Use screen touches to toggle the video between playing and paused.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }


    public File getVideoStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), albumName);

        if (!file.mkdirs()) {
            Log.e("error", "Directory not created");
        }

        return file;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

        if (!file.mkdirs()) {
            Log.e("error", "Directory not created");
        }

        return file;
    }

    private void showErrorToast(final String error, final String displayMsg) {
        if (updateScreenOn == false)
            showErrorToast(error, displayMsg, false);
    }

    private void showErrorToast(final String error, final String displayMsg,
                                final boolean doFinish) {

        runOnUiThread(new Runnable() {
            public void run() {
                try {


                    try {
                        if (pd != null) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                                pd = null;

                            }
                        }
                    } catch (Exception e) {
                    }


                    sleep(1000);
                    if (updateScreenOn == true)
                        return;

                    if (spinner != null)
                        spinner.setVisibility(View.GONE);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);

                    // set dialog message
                    alertDialogBuilder.setMessage(displayMsg).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (doFinish)
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


    private void LoadMultiVideo(final String videoURL, final File tmpFile) {

/**
 if (videoURL != null) {


 try {

 loadingMultiVideo = true;

 runOnUiThread(new Runnable() {
 public void run() {
 try {
 if (numMultVideos == 0)
 startProgressDialog();


 } catch (Exception e4) {
 }
 }
 });

 numMultVideos += 1;
 if (!isNetworkAvailable()) {
 showErrorToast("", _this.getString(R.string.noInternet), true);
 return;

 }

 Download_Uri = Uri.parse(videoURL);

 DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
 request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
 request.setAllowedOverRoaming(true);


 Log.d("tag", tmpFile.getAbsolutePath() + "   " + tmpFile.getName());

 File filePath = new File("/Pictures/Regrann - Multi Post");


 request.setDestinationInExternalPublicDir(filePath.getAbsolutePath(), tmpFile.getName());


 refid = downloadManager.enqueue(request);


 } catch (Exception e) {

 showErrorToast("#16 - " + e.getMessage(), getString(R.string.problemfindingvideo), true);
 isVideo = false;
 }

 }
 **/


        try {
            if (videoURL != null) {

                AsyncTask<Void, Void, Void> videoLoader = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {

                            if (!isNetworkAvailable()) {
                                showErrorToast("", _this.getString(R.string.noInternet), true);
                                return null;

                            }


                            final int TIMEOUT_CONNECTION = 10000;// 5sec
                            final int TIMEOUT_SOCKET = 40000;// 30sec

                            URL url = null;
                            try {
                                url = new URL(videoURL);
                            } catch (MalformedURLException e3) {
                                e3.printStackTrace();
                            }
                            long startTime = System.currentTimeMillis();
                            Log.i("info", "image download beginning: " + videoURL);

                            HttpURLConnection ucon = null;

                            try {
                                ucon = (HttpURLConnection) url.openConnection();

                            } catch (IOException e2) {
                             //  sendEvent("E11_" + e2.getMessage(), "", "");
                                showErrorToast("#11 - " + e2.getMessage(), getString(R.string.problemfindingvideo), true);

                                e2.printStackTrace();
                            }

                            assert ucon != null;
                            ucon.setReadTimeout(TIMEOUT_CONNECTION);
                            ucon.setConnectTimeout(TIMEOUT_SOCKET);

                            InputStream is = null;
                            try {
                                is = ucon.getInputStream();
                            } catch (IOException e1) { // TODO
                                // Auto-generated
                                // catch block
                                e1.printStackTrace();
                            }
                            assert is != null;
                            BufferedInputStream inStream = new BufferedInputStream(is != null ? is : null, 1024 * 5);
                            FileOutputStream outStream = null;
                            try {
                                outStream = new FileOutputStream(tmpFile.getPath());

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            byte[] buff = new byte[5 * 1024];

                            // Read bytes (and store them) until there is
                            // nothing // more to //

                            int len;
                            try {
                                while ((len = inStream.read(buff)) != -1) {
                                    assert outStream != null;
                                    outStream.write(buff, 0, len);
                                }
                            } catch (IOException e) {
                           //     sendEvent("E12_" + e.getMessage(), "", "");
                                showErrorToast("#12 - " + e.getMessage(), getString(R.string.problemfindingvideo), true);

                                e.printStackTrace();
                            }

                            // clean up
                            try {

                                assert outStream != null;
                                outStream.flush();
                                outStream.close();
                                inStream.close();
                                ucon.disconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                        //    sendEvent("E13_" + e.getMessage(), "", "");
                            showErrorToast("#13 - " + e.getMessage(), getString(R.string.problemfindingvideo), true);

                        }

                        return null;
                    }

                    //show progress in onPre
                    @Override
                    protected void onPreExecute() {

                        if (numMultVideos == 0)
                            startProgressDialog();

                        numMultVideos++ ;

                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                               //         pd[0] = ProgressDialog.show(ShareActivity.this, _this.getString(R.string.progress_dialog_msg), _this.getString(R.string.downloadingVideo), true, false);
                                    } catch (Exception e4) {
                                    }
                                }
                            });
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        try {




                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        numMultVideos -= 1;

                                        if (numMultVideos == 0 && pd != null) {
                                            removeProgressDialog();
                                        }
                                    } catch (Exception e4) {
                                    }

                                    if (isAutoSave) {

                                        copyAllMultiToSave();


                                    }
                                    //  scanMultiPostFolder();
                                    return;
                                }

                            });


                         //   scanMultiPostFolder();


                        } catch (Exception e) {
                        }

                    }
                };


                videoLoader.execute((Void[]) null);

            }
        } catch (Exception e) {
        }


    }

    private void startAutoSaveMultiProgress() {

        if (pd != null)
            pd = ProgressDialog.show(ShareActivity.this, _this.getString(R.string.progress_dialog_msg), _this.getString(R.string.auto_saving_progress), true, true);

    }

    private void startProgressDialog() {


        if (noAds && isAutoSave)
            return;
        else
            pd = ProgressDialog.show(ShareActivity.this, _this.getString(R.string.progress_dialog_msg), _this.getString(R.string.downloadingVideo), true, true);


    }

    private void removeProgressDialog() {


        try {
            if (pd != null) {
                if (pd.isShowing()) {



                                pd.dismiss();
                                pd = null;





                }
            }
        } catch (Exception e) {
        }


    }


    private void LoadVideo() {

        try {
            if (videoURL != null) {
                loadingMultiVideo = false;


                AsyncTask<Void, Void, Void> videoLoader = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {

                            if (!isNetworkAvailable()) {
                                showErrorToast("", _this.getString(R.string.noInternet), true);
                                return null;

                            }


                            final int TIMEOUT_CONNECTION = 5000;// 5sec
                            final int TIMEOUT_SOCKET = 30000;// 30sec

                            URL url = null;
                            try {
                                url = new URL(videoURL);
                            } catch (MalformedURLException e3) {
                                e3.printStackTrace();
                            }
                            long startTime = System.currentTimeMillis();
                            Log.i("info", "image download beginning: " + videoURL);

                            // Open a connection to that URL.
                            URLConnection ucon = null;
                            try {
                                ucon = url != null ? url.openConnection() : null;
                            } catch (IOException e2) {
                                showErrorToast("#11 - " + e2.getMessage(), getString(R.string.problemfindingvideo), true);

                                e2.printStackTrace();
                            }

                            assert ucon != null;
                            ucon.setReadTimeout(TIMEOUT_CONNECTION);
                            ucon.setConnectTimeout(TIMEOUT_SOCKET);

                            InputStream is = null;
                            try {
                                is = ucon.getInputStream();
                            } catch (IOException e1) { // TODO
                                // Auto-generated
                                // catch block
                                e1.printStackTrace();
                            }
                            assert is != null;
                            BufferedInputStream inStream = new BufferedInputStream(is != null ? is : null, 1024 * 5);
                            FileOutputStream outStream = null;
                            try {
                                outStream = new FileOutputStream(Environment.getExternalStorageDirectory() + tempVideoName);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            byte[] buff = new byte[5 * 1024];

                            // Read bytes (and store them) until there is
                            // nothing // more to //

                            int len;
                            try {
                                while ((len = inStream.read(buff)) != -1) {
                                    assert outStream != null;
                                    outStream.write(buff, 0, len);
                                }
                            } catch (IOException e) {

                                showErrorToast("#10 - " + e.getMessage(), getString(R.string.problemfindingvideo), true);

                                e.printStackTrace();
                            }

                            // clean up
                            try {
                                assert outStream != null;
                                outStream.flush();
                                outStream.close();
                                inStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            tempVideoFile = new File(Environment.getExternalStorageDirectory() + tempVideoName);

                            tempVideoFullPathName = tempVideoFile.getPath();


                            // isVideo = true;

                            try {
                                if (rateRequestDialog != null) {
                                    if (rateRequestDialog.isShowing()) {
                                        rateRequestDialog.dismiss();
                                    }
                                }


                            } catch (Exception e) {
                            }

                        } catch (Exception e) {
                            showErrorToast("#12 - " + e.getMessage(), getString(R.string.problemfindingvideo), true);
                            isVideo = false;
                        }

                        return null;
                    }

                    //show progress in onPre
                    @Override
                    protected void onPreExecute() {
                        try {

                            pd = ProgressDialog.show(ShareActivity.this, _this.getString(R.string.progress_dialog_msg), _this.getString(R.string.downloadingVideo), true, true);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        try {

                            removeProgressDialog();

                            if (isAutoSave) {
                                copyTempToSave();
                                finish();
                            }

                            if (isQuickPost) {
                                if (isVideo) {

                                    quickPostSendToInstagram();

                                }
                            }

                            if (isQuickKeep) {

                                KeptListAdapter db = KeptListAdapter.getInstance(_this);
                                clearClipboard();

                                db.addItem(new InstaItem(title, tempFileFullPathName, tempVideoFullPathName, author));

                                Toast toast = Toast.makeText(ShareActivity.this, R.string.postlaterconfirmtoast, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                                toast.show();
                                finish();

                            }

                        } catch (Exception e) {
                        }

                    }
                };


                // }
                // });

                videoLoader.execute((Void[]) null);

            }
        } catch (Exception e) {
        }

    }


    /**
     * private void LoadVideo() {
     * <p>
     * <p>
     * if (videoURL != null) {
     * <p>
     * loadingMultiVideo = false;
     * <p>
     * try {
     * <p>
     * runOnUiThread(new Runnable() {
     * public void run() {
     * try {
     * <p>
     * <p>
     * startProgressDialog();
     * } catch (Exception e4) {
     * }
     * <p>
     * <p>
     * }
     * <p>
     * });
     * <p>
     * <p>
     * if (!isNetworkAvailable()) {
     * showErrorToast("", _this.getString(R.string.noInternet), true);
     * return;
     * <p>
     * }
     * <p>
     * Download_Uri = Uri.parse(videoURL);
     * <p>
     * DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
     * request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
     * request.setAllowedOverRoaming(true);
     * <p>
     * request.setDestinationInExternalPublicDir("/", tempVideoName);
     * <p>
     * <p>
     * refid = downloadManager.enqueue(request);
     * tempVideoFile = new File(Environment.getExternalStorageDirectory() + tempVideoName);
     * <p>
     * tempVideoFullPathName = tempVideoFile.getPath();
     * <p>
     * <p>
     * // isVideo = true;
     * <p>
     * try {
     * if (rateRequestDialog != null) {
     * if (rateRequestDialog.isShowing()) {
     * rateRequestDialog.dismiss();
     * }
     * }
     * <p>
     * <p>
     * } catch (Exception e) {
     * }
     * <p>
     * } catch (Exception e) {
     * <p>
     * showErrorToast("#16 - " + e.getMessage(), getString(R.string.problemfindingvideo), true);
     * isVideo = false;
     * }
     * <p>
     * }
     * <p>
     * }
     **/


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {


            if (loadingMultiVideo) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            numMultVideos -= 1;

                            if (numMultVideos == 0 && pd != null) {

                                removeProgressDialog();
                                //   scanMultiPostFolder();
                                // scanRegrannFolder();

                            }
                        } catch (Exception e4) {
                        }

                        if (isAutoSave) {
                            //      numOfMultiVideos--;
                            //       copyAllMultiToSave();


                        }

                        return;
                    }

                });


            } else {


                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


                removeProgressDialog();

                //      scanMultiPostFolder();
                scanRegrannFolder();

                if (isAutoSave) {
                    copyTempToSave();
                    //   finish();
                }

                if (isQuickPost) {
                    if (isVideo) {

                        quickPostSendToInstagram();

                    }
                }

                if (isQuickKeep) {

                    KeptListAdapter db = KeptListAdapter.getInstance(_this);
                    clearClipboard();

                    db.addItem(new InstaItem(title, tempFileFullPathName, tempVideoFullPathName, author));

                    Toast toast = Toast.makeText(ShareActivity.this, R.string.postlaterconfirmtoastvideo, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                    toast.show();
                    finish();

                }


            }
        }
    };


    private static String convertInputStreamToString(InputStream inputStream) throws
            IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private void downloadSinglePhotoToTemp(String url) {
        try {
            //    URL imageurl = new URL(url);

            FutureTarget<Bitmap> futureBitmap = Glide.with(RegrannApp._this)
                    .asBitmap()
                    .load(url)
                    .submit();

            if (futureBitmap == null) {

                showErrorToast("#73 - ", "There was a problem downloading the photo.  Please try again.", true);
                return;
            }

            try {
                originalBitmapBeforeNoCrop = futureBitmap.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // originalBitmapBeforeNoCrop = resource;
            Bitmap bitmap = originalBitmapBeforeNoCrop;


            if (preferences.getBoolean("watermark_checkbox", false)) {
                Point p = new Point(10, bitmap.getHeight() - 10);
                int textSize = 20;
                if (bitmap.getHeight() > 640)
                    textSize = 50;
                bitmap = mark(bitmap, author, p, Color.YELLOW, 180, textSize, false);
            }


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, bytes);


            // no crop if not square ??

            try {
                // write the bytes in file
                FileOutputStream fo = new FileOutputStream(tempFile);
                fo.write(bytes.toByteArray());

                // remember close de FileOutput
                fo.close();
            } catch (Exception e) {
            }

        } catch (OutOfMemoryError e) {
            int y = 1;

        }

    }

    /**
     * private void downloadSinglePhotoToTemp(String picURL) {
     * try {
     * <p>
     * <p>
     * <p>
     * <p>
     * URL imageurl = new URL(picURL);
     * originalBitmapBeforeNoCrop = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
     * Bitmap bitmap = originalBitmapBeforeNoCrop;
     * <p>
     * <p>
     * if (preferences.getBoolean("watermark_checkbox", false)) {
     * Point p = new Point(10, bitmap.getHeight() - 10);
     * int textSize = 20;
     * if (bitmap.getHeight() > 640)
     * textSize = 50;
     * bitmap = mark(bitmap, author, p, Color.YELLOW, 180, textSize, false);
     * }
     * <p>
     * <p>
     * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
     * bitmap.compress(Bitmap.CompressFormat.JPEG, 99, bytes);
     * <p>
     * <p>
     * // no crop if not square ??
     * <p>
     * <p>
     * // write the bytes in file
     * FileOutputStream fo = new FileOutputStream(tempFile);
     * fo.write(bytes.toByteArray());
     * <p>
     * // remember close de FileOutput
     * fo.close();
     * <p>
     * } catch (Exception e) {
     * showErrorToast("In downloadSinglePhotoToTemp","Problem getting photo", true);
     * <p>
     * }
     * <p>
     * }
     **/

    private void copyAllMultiToSave() {

        clearClipboard();
        new LongOperation().execute("");


    }


    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            File dir = new File(regrannMultiPostFolder);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    try {

                        File source = new File(dir, children[i]);


                        File destination = new File(regrannPictureFolder + File.separator + author + "-" + children[i]);
                        try {
                            copy(source, destination);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        int i4 = 1;
                    }


                }
            }
            scanRegrannFolder();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("app5", "done copy multi");

            if (noAds && isAutoSave) {
                // user is premium and we are in quick save mode
                removeProgressDialog();
                Toast toast = Toast.makeText(ShareActivity.this, "Saving multi-post complete.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                if (spinner != null)
                    spinner.setVisibility(View.GONE);

                //  finish();
                return;
            }


            if (isAutoSave) {
                // user is premium and we are in quick save mode
                if (1 == 2 && showInterstitial) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //   findViewById(R.id.upgradeBtn).setVisibility(View.VISIBLE);
                            //  findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
                            TextView t = findViewById(R.id.autosaveText);
                            t.setText("Quick Save Done");


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    spinner.setVisibility(View.GONE);


                                    RegrannApp.sendEvent("sc_admob8_needed");


                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();


                                        RegrannApp.sendEvent("sc_admob8_shown");

                                        autoSaving = true;


                                    } else
                                        finish();

                                }
                            }, 2000);

                        }
                    }, 3000);
                } else {
                    if (spinner != null)
                        spinner.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(ShareActivity.this, "Saving multi-post complete.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                }


                return;

            }


            changeSaveButton();

            if (showInterstitial) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        removeProgressDialog();
                        Toast toast = Toast.makeText(ShareActivity.this, "Saving multi-post complete.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }, 3000);


            } else {


                removeProgressDialog();
                Toast toast = Toast.makeText(ShareActivity.this, "Saving multi-post complete.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();


            }


            //    Toast toast = Toast.makeText(ShareActivity.this, "Saving multi-post photos and videos.", Toast.LENGTH_LONG);
            //  toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            //  toast.show();


            int numWarnings = sharedPref.getInt("countOfRuns", 0);

            //    numWarnings = 5;

            if (numWarnings < 4)
                return;


            if (showInterstitial) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        RegrannApp.sendEvent("sc_admob8_needed");

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();


                            RegrannApp.sendEvent("sc_admob8_shown");
                        }
                    }
                }, 1000);


            }


        }

        @Override
        protected void onPreExecute() {


            if (isAutoSave == false)
                pd = ProgressDialog.show(ShareActivity.this, _this.getString(R.string.progress_dialog_msg), "Saving.....", true, false);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


    boolean autoSaving = false;

    public void copy(File src, File dst) throws IOException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)

            Files.copy(src.toPath(), dst.toPath());
        else {


            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();

        }
    }

    public void copyTempToSave() {
        try {

            File src;

            Long currTime = System.currentTimeMillis();


            clearClipboard();

            String fname;
            Toast toast;
            String msg;
            if (isVideo) {
                src = new File(Environment.getExternalStorageDirectory() + tempVideoName);
                fname = regrannPictureFolder + File.separator + author + "_video_" + currTime + ".mp4";
                saveToastMsg = "Video was saved in /Pictures/Regrann/Video-" + currTime + ".mp4";
            } else {


                // Get the directory for the user's public pictures directory.
                File file = new File(Environment.getExternalStorageDirectory(), "temp");


                tempFileName = "temp_regrann-" + System.currentTimeMillis() + ".jpg";


                tempFileFullPathName = file.toString() + File.separator + tempFileName;

                File tempFileSave = new File(tempFileFullPathName);


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                originalBitmapBeforeNoCrop.compress(Bitmap.CompressFormat.JPEG, 99, bytes);

                // no crop if not square ??


                // write the bytes in file
                FileOutputStream fo = new FileOutputStream(tempFileSave);
                fo.write(bytes.toByteArray());

                // remember close de FileOutput
                fo.close();

                //  cleanUp();

                src = tempFileSave;
                fname = regrannPictureFolder + File.separator + author + "-" + currTime + ".jpg";
                saveToastMsg = "Photo was saved in /Pictures/Regrann/" + author + "  -" + currTime + ".jpg";
            }


            File dst = new File(fname);

            copy(src, dst);


            Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri fileContentUri = Uri.fromFile(dst); // With 'permFile' being the File object
            mediaScannerIntent.setData(fileContentUri);
            this.sendBroadcast(mediaScannerIntent);


            if (noAds && isAutoSave) {
                // user is premium and we are in quick save mode

                toast = Toast.makeText(getBaseContext(), saveToastMsg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();

                finish();
                return;
            }


            if (isAutoSave) {
                // user is premium and we are in quick save mode
                if (1 == 2 && showInterstitial) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            //   findViewById(R.id.upgradeBtn).setVisibility(View.VISIBLE);
                            //  findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
                            TextView t = findViewById(R.id.autosaveText);
                            t.setText("Quick Save Done");


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    spinner.setVisibility(View.GONE);


                                    RegrannApp.sendEvent("sc_admob8_needed");

                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                        autoSaving = true;

                                        RegrannApp.sendEvent("sc_admob8_shown");
                                    } else
                                        finish();

                                }
                            }, 1000);


                        }
                    }, 3000);


                }


                return;

            }


            // toast = Toast.makeText(getBaseContext(), saveToastMsg, Toast.LENGTH_LONG);
            //   toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            //  toast.show();


            changeSaveButton();

            int numWarnings = sharedPref.getInt("countOfRuns", 0);


            if (numWarnings < 4) {
                return;
            }


            if (showInterstitial) {

                RegrannApp.sendEvent("sc_admob8_needed");

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();


                    RegrannApp.sendEvent("sc_admob8_shown");


                }
            }


        } catch (
                Exception e) {
            int i = 1;
        }

    }


    @Override
    public void onClick(View v) {
        try {


            if (!photoReady)
                return;


            if (v == btndownloadphoto) {

                if (isMulti) {
                    //DefaultSliderView slider = (DefaultSliderView) mDemoSlider.getCurrentSlider();
                    //   Toast.makeText(_this, slider.getBundle().get("url") + "   \n IS_VIDEO :  " + slider.getBundle().get("is_video"), Toast.LENGTH_SHORT).show();
                    //  if (slider.getBundle().get("is_video").toString() == "false")
                    //    downloadSinglePhotoToTemp(slider.getBundle().get("url").toString());

                    copyAllMultiToSave();
                    RegrannApp.sendEvent("sc_savebtn_multi");

                    return;
                }

                RegrannApp.sendEvent("sc_savebtn");


                // flurryAgent.logEvent("Save Button Pressed");
                copyTempToSave();

                //     if (Build.VERSION.SDK_INT >= 21) {
                //        CookieManager.getInstance().setAcceptThirdPartyCookies(webViewInsta, true);
                //       webViewInsta.loadUrl("https://www.instagram.com/");
                //  }

                return;

            }


            if (v == btnShare) {


                RegrannApp.sendEvent("sc_sharebtn");


                try {

                    // flurryAgent.logEvent("Share button pressed");
                    // Create the new Intent using the 'Send' action.
                    Intent share = new Intent(Intent.ACTION_SEND);

                    share.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    String txt;

                    if (inputMediaType == 0) {
                        String caption;
                        //count how many hashtags

                        if (title != null && (Util.isKeepCaption(_this) == false)) {


                            share.putExtra(Intent.EXTRA_SUBJECT, "Regrann from @" + author);
                            txt = "@Regrann from @" + author + "  -  " + title + "  -  " + getIntent().getStringExtra("mediaUrl")
                                    + "\n\nRegrann App - Repost without leaving Instagram - Download Here : http://regrann.com/download";
                        } else {
                            share.putExtra(Intent.EXTRA_SUBJECT, "Photo share");

                            txt = "  - via @Regrann app";
                        }

                        caption = txt;
                        share.putExtra(Intent.EXTRA_TEXT, caption);
                        clearClipboard();


                    } else {
                        String caption = "";
                        if (Util.isKeepCaption(_this) == false)
                            caption = "@regrann no-crop:";


                        share.putExtra(Intent.EXTRA_TEXT, caption);

                        clearClipboard();

                    }


                    if (isVideo) {
                        share.setType("video/*");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempVideoFile));
                    } else {
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));

                        share.setType("image/*");
                    }
                    // Broadcast the Intent.
                    startActivity(Intent.createChooser(share, "Share to"));

                    cleanUp();


                    finish();
                } catch (Exception e) {
                    showErrorToast(e.getMessage(), getString(R.string.therewasproblem));

                }

                return;


            }

/**
 if (v == scheduleBtn) {


 if (inputMediaType != 0) {
 title = "";
 author = "";

 }
 KeptListAdapter db = KeptListAdapter.getInstance(_this);

 InstaItem instaItem = new InstaItem(title, tempFileFullPathName, tempVideoFullPathName, author);
 int itemID = (int) db.addItem(instaItem, 0);


 clearClipboard();


 Intent i = new Intent(ShareActivity.this, PostPhoto.class);
 i.putExtra("itemid", itemID);
 i.putExtra("fileName", tempFileFullPathName);
 i.putExtra("videoURL", tempVideoFullPathName);
 i.putExtra("caption", title);
 i.putExtra("author", author);

 i.putExtra("scheduledTime", Long.valueOf(0));
 i.putExtra("isScheduled", 0);
 i.putExtra("fromschedulebtn", true);

 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
 //	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
 //   i.setAction("com.jaredco.action.fromkept");
 startActivity(i);

 cleanUp();


 finish();

 }
 **/

            if (v == btnCurrentToFeed) {

                RegrannApp.sendEvent("sc_current_to_feed");

                if (isMulti) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (Objects.requireNonNull(mDemoSlider.getCurrentSlider().getBundle().get("is_video")).toString() == "false") {
                                    //   downloadSinglePhotoToTemp(Objects.requireNonNull(mDemoSlider.getCurrentSlider().getBundle().get("url")).toString());
                                    tempFile = new File(mDemoSlider.getCurrentSlider().getBundle().get("fname").toString());
                                    isVideo = false;
                                } else {

                                    isVideo = true;
                                    tempVideoFile = new File(mDemoSlider.getCurrentSlider().getBundle().get("fname").toString());
                                }
                                isMulti = false;

                                onClick(btnInstagram);

                            } catch (Exception e) {
                            }
                        }
                    });


                    return;
                }

            }


/**
 if (v == btnCurrentToStory) {

 RegrannApp.sendEvent("sc_current_to_story");

 if (isMulti) {

 runOnUiThread(new Runnable() {
 public void run() {
 try {

 if (Objects.requireNonNull(mDemoSlider.getCurrentSlider().getBundle().get("is_video")).toString() == "false") {
 downloadSinglePhotoToTemp(Objects.requireNonNull(mDemoSlider.getCurrentSlider().getBundle().get("url")).toString());
 }
 isMulti = false;

 onClick(btnInstagramstories);

 } catch (Exception e) {
 }
 }
 });


 return;
 }

 }
 **/

            if (v == postlater) {

                RegrannApp.sendEvent("sc_postlaterbtn");


                if (inputMediaType != 0) {
                    title = "";
                    author = "";

                }


                KeptListAdapter db = KeptListAdapter.getInstance(_this);


                try {
                    File videoDst = null;

                    File folderDst = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES) + "/regrann_postlater/" + new File(tempFileFullPathName).getName());


                    FileUtils.copyFile(new File(tempFileFullPathName), folderDst);


                    if (isVideo) {

                        videoDst = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES) + "/regrann_postlater/" + new File(tempVideoFullPathName).getName());


                        FileUtils.copyFile(new File(tempVideoFullPathName), videoDst);
                        db.addItem(new InstaItem(title, folderDst.getAbsolutePath(), videoDst.getAbsolutePath(), author));

                    } else {
                        db.addItem(new InstaItem(title, folderDst.getAbsolutePath(), "", author));
                    }
                } catch (Exception e) {
                    Log.d("tag", e.getMessage());
                }


                clearClipboard();

                Toast toast;
                if (isVideo) {
                    toast = Toast.makeText(ShareActivity.this, R.string.postlaterconfirmtoastvideo, Toast.LENGTH_SHORT);
                } else

                    toast = Toast.makeText(ShareActivity.this, R.string.postlaterconfirmtoast, Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

                toast.show();

                //    cleanUp();

                finish();


                return;

            }


            if (v == btnInstagram || v == btnInstagramstories) {
                instagramBtnClicked = true;

                if (v == btnInstagramstories) {
                    btnStoriesClicked = true;
                    RegrannApp.sendEvent("sc_storiesbtn");
                } else {
                    btnStoriesClicked = false;
                    RegrannApp.sendEvent("sc_postfeedbtn");
                }

                if (isMulti) {
                    // DefaultSliderView slider = (DefaultSliderView) mDemoSlider.getCurrentSlider();
                    //   Toast.makeText(_this, slider.getBundle().get("url") + "   \n IS_VIDEO :  " + slider.getBundle().get("is_video"), Toast.LENGTH_SHORT).show();
                    //   if (slider.getBundle().get("is_video").toString() == "false")
                    //     downloadSinglePhotoToTemp(slider.getBundle().get("url").toString());

                    /**
                     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);

                     // set dialog message
                     alertDialogBuilder.setMessage("The multi-post pics are in a new folder [Regrann Multi-Post]. To post them you need to switch to Instagram and upload them manually.").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                     String caption = Util.prepareCaption(title, author, _this.getApplication().getApplicationContext(), caption_suffix, isVine);
                     ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                     ClipData clip = ClipData.newPlainText("Post caption", caption);
                     clipboard.setPrimaryClip(clip);
                     //  if (doFinish)
                     finish();
                     }

                     });

                     // create alert dialog
                     AlertDialog alertDialog = alertDialogBuilder.create();

                     // show it
                     alertDialog.show();
                     **/


                    showMultiDialog();


                    return;
                }


                //   cleanUp();


                final int numWarnings = preferences.getInt("captionWarning", 0);

//                numWarnings = 5;


                if (numWarnings < 3 && inputMediaType == 0) {


                    sendToInstagam();


                } else {

                    long prevAdShown = preferences.getLong("instagramAdShownTime", 0);

                    long diff = (System.currentTimeMillis() - prevAdShown) / 1000 / 60;


                    Log.d("app", "DIFF Inst. Button :" + diff);

                    //    diff = 8 ;
                    if (showInterstitial) {

                        RegrannApp.sendEvent("sc_admob8_needed");

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();


                            RegrannApp.sendEvent("sc_admob8_shown");


                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                            sendToInstagam();

                        }
                    } else
                        sendToInstagam();


                }


                /**
                 if (btnStoriesClicked) {


                 final String newVideo = tempVideoFullPathName + "z" ;


                 new Mp4Composer(tempVideoFullPathName, newVideo)

                 .size(1080, 1920)
                 .fillMode(FillMode.PRESERVE_ASPECT_FIT)

                 .listener(new Mp4Composer.Listener() {
                @Override public void onProgress(double progress) {
                Log.d(TAG, "onProgress = " + progress);
                }

                @Override public void onCompleted() {
                Log.d(TAG, "onCompleted()");
                runOnUiThread(new Runnable() {
                public void run() {


                String newFileName = tempVideoFullPathName ;
                // copy new video to old name
                File file = new File(tempVideoFullPathName);
                boolean deleted = file.delete();

                if (deleted)
                {

                File file2 = new File(newVideo);
                file2.renameTo(new File(newFileName));
                }

                }

                });

                }

                @Override public void onCanceled() {
                Log.d(TAG, "onCanceled");
                }

                @Override public void onFailed(Exception exception) {
                Log.e(TAG, "onFailed()", exception);

                onCompleted();
                }
                })
                 .start();
                 }
                 **/

            }


        } catch (Exception e) {
            showErrorToast(e.getMessage(), "#10 " + getString(R.string.therewasproblem));

        }

    }


    private void changeSaveButton() {


        btndownloadphoto.setImageResource(R.drawable.savedicon);
        btndownloadphoto.setEnabled(false);


    }


    private void sendToInstagam() {


        try {

            // flurryAgent.logEvent("Instagram button pressed");
            // FlurryAgent.logEvent("Click Instagram");
            Intent intent = this.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
            String caption = Util.prepareCaption(title, author, _this.getApplication().getApplicationContext(), caption_suffix, tiktokLink);


            if (intent != null) {
                shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.instagram.android");
                //  shareIntent.setClassName("com.instagram.android",instagram_activity);

                if (btnStoriesClicked) {
                    RegrannApp.sendEvent("ClickStoriesBtn", "", "");
                    shareIntent.setClassName(
                            "com.instagram.android",
                            "com.instagram.share.handleractivity.StoryShareHandlerActivity");
                } else {
                    shareIntent.setClassName(
                            "com.instagram.android",
                            "com.instagram.share.handleractivity.ShareHandlerActivity");
                }


                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);


                if (inputMediaType == 0) {
                    //        shareIntent.putExtra(Intent.EXTRA_TEXT, caption);


                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Post caption", caption);
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                } else {
                    caption = "";
                    if (Util.isKeepCaption(_this) == false)
                        caption = "@regrann no-crop:";


                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Post caption", caption);
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);


                }

                if (isVideo) {
                    shareIntent.setType("video/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempVideoFile));
                } else {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));

                    shareIntent.setType("image/jpeg");
                }


                int numWarnings = preferences.getInt("captionWarning", 0);

                Log.d("regrann", "Numwarnings : " + numWarnings);

                if (numWarnings < 3 && inputMediaType == 0) {

                    Log.d("regrann", "Numwarnings  2 : " + numWarnings);
                    numWarnings++;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("captionWarning", numWarnings);

                    editor.commit();

                    showPasteDialog(shareIntent);

                } else {

                    sendToInstagram = true;


                    Log.d("regrann", "No warnings;");
                    // Do something after 5s = 5000ms


/**
 PackageManager packManager = getPackageManager();
 List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(shareIntent,  PackageManager.MATCH_DEFAULT_ONLY);

 boolean resolved = false;
 for(ResolveInfo resolveInfo: resolvedInfoList){
 if(resolveInfo.activityInfo.packageName.startsWith("com.instagram.android")){



 Log.d("app5", resolveInfo.activityInfo.packageName + "    " + resolveInfo.activityInfo.name);

 //  break;
 }
 }
 **/


                    startActivity(shareIntent);

                    //   cleanUp();


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                finish();
                            } catch (Exception e) {
                                Log.d("app5", "on finish");
                            }
                        }
                    }, 2000);


                }


            } else {
                // bring user to the market to download the app.

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
                startActivity(intent);
            }

        } catch (Exception e) {
            showErrorToast(e.getMessage(), getString(R.string.therewasproblem));

        }
        // FlurryAgent.onEndSession(serviceCtx);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }


/**

 //==============================================================================================
 // Callbacks
 //==============================================================================================
 // PubnativeFeedBanner.Listener
 //----------------------------------------------------------------------------------------------

 @Override public void onPubnativeFeedBannerLoadFinish(PubnativeFeedBanner feedBanner) {
 try {
 Log.v(TAG, "onPubnativeFeedBannerLoadFinish");
 if (mFeedBanner == feedBanner) {
 feedBanner.show(mFeedBannerContainer);
 // mLoaderContainer.setVisibility(View.GONE);
 }
 } catch (Exception e){}
 }

 @Override public void onPubnativeFeedBannerLoadFailed(PubnativeFeedBanner feedBanner, Exception exception) {
 Log.v(TAG, "onPubnativeFeedBannerLoadFailed");
 //   mLoaderContainer.setVisibility(View.GONE);
 }

 @Override public void onPubnativeFeedBannerShow(PubnativeFeedBanner feedBanner) {
 Log.v(TAG, "onPubnativeFeedBannerShow");
 }

 @Override public void onPubnativeFeedBannerImpressionConfirmed(PubnativeFeedBanner feedBanner) {
 Log.v(TAG, "onPubnativeFeedBannerImpressionConfirmed");
 }

 @Override public void onPubnativeFeedBannerClick(PubnativeFeedBanner feedBanner) {
 Log.v(TAG, "onPubnativeFeedBannerClick");
 }
 **/
}
