package com.jaredco.regrann.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jaredco.regrann.R;

public class InstagramLogin extends AppCompatActivity implements View.OnClickListener {

    Button btsubmit; // this button in your xml file
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_login);

        btsubmit = findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);


        RegrannApp.sendEvent("private_in_loginactivityV2");


        webview = findViewById(R.id.browser);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);


        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        this.webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.setWebViewClient(new WebViewClient());

        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webview.clearCache(true);


        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);
        }
        webview.loadUrl("https://www.instagram.com/accounts/login");
    }


    public void onClick(View v)
    {
        if(btsubmit==v) {
            try {
                ShareActivity._this.loadPage();
                RegrannApp.sendEvent("private_loginbtnclickV2");
            } catch (Exception e) {
                RegrannApp.sendEvent("InLoginRequest_logincompleteV2");
            }

            finish();
        }
    }

}
