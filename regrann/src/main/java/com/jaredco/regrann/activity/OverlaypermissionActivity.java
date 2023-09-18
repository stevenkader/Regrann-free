package com.jaredco.regrann.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jaredco.regrann.R;

public class OverlaypermissionActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlayexplain);


    }


    public void onClickScreen(View v) {

        finish();
    }

}