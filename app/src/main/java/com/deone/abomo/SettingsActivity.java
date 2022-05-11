package com.deone.abomo;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.MethodTools.deconnecter;
import static com.deone.abomo.outils.MethodTools.loadSystemPreference;
import static com.deone.abomo.outils.MethodTools.saveAppLanguagePreference;
import static com.deone.abomo.outils.MethodTools.saveAppThemePreference;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseReference reference;
    private String uid;
    private String myuid;
    private boolean isTheme;
    private boolean isLanguage;
    private TextView tvSignout;
    private SwitchCompat swTheme;
    private SwitchCompat swLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSystemPreference(this);
        setContentView(R.layout.activity_settings);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null){
            uid = getIntent().getStringExtra("uid");
            myuid = fuser.getUid();
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        swTheme = findViewById(R.id.swTheme);
        swTheme.setChecked(isTheme);
        swLanguage = findViewById(R.id.swLanguage);
        swLanguage.setChecked(isLanguage);
        findViewById(R.id.tvSignout).setOnClickListener(this);
        swTheme.setOnCheckedChangeListener(this);
        swLanguage.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSignout){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.sign_out))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, which) -> deconnecter(this))
                    .create().show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences preferences = getSharedPreferences("ABOMO_PREF", MODE_PRIVATE);
        if (buttonView.getId() == R.id.swTheme){
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            }
            saveAppThemePreference(preferences, isChecked);
        }else if (buttonView.getId() == R.id.swLanguage){
            if (isChecked){

            }else {

            }
            saveAppLanguagePreference(preferences, isChecked);
        }
    }




}