package com.deone.abomo;


import static com.deone.abomo.outils.MethodTools.appPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ShowLoyerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences(this);
        setContentView(R.layout.activity_show_loyer);
    }
}