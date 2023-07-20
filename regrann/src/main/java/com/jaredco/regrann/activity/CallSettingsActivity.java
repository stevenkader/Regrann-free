package com.jaredco.regrann.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.calldorado.sdk.Calldorado;

public class CallSettingsActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calldorado.showSettings(this);
        finish();
    }


}
