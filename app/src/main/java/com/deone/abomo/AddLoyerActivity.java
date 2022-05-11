package com.deone.abomo;

import static com.deone.abomo.outils.MethodTools.loadSystemPreference;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddLoyerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSystemPreference(this);
        setContentView(R.layout.activity_add_loyer);
    }
}