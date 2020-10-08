package com.jaredco.regrann.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
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

        btsubmit= findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);


        RegrannApp.sendEvent("private_in_loginactivity");


        webview = findViewById(R.id.browser);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        this.webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);


        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
               // webview.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true);
        }
        webview.loadUrl("https://www.instagram.com/accounts/login");
    }


    public void onClick(View v)
    {
        if(btsubmit==v)
        {
            try {
                ShareActivity._this.loadPage();
                RegrannApp.sendEvent("private_loginbtnclick");
            }catch (Exception e){}
           finish();
        }
    }

}
