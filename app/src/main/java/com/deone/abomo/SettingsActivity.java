package com.deone.abomo;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.deconnecter;
import static com.deone.abomo.outils.MethodTools.loadSystemPreference;
import static com.deone.abomo.outils.MethodTools.saveAppLanguagePreference;
import static com.deone.abomo.outils.MethodTools.saveAppThemePreference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deone.abomo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseReference reference;
    private String uid;
    private String myuid;
    private boolean isTheme;
    private boolean isLanguage;
    private ImageView ivAvatar;
    private TextView tvNoms;
    private TextView tvTelephone;
    private TextView tvSignout;
    private SwitchCompat swTheme;
    private SwitchCompat swLanguage;
    private final ValueEventListener valuser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                User user = ds.getValue(User.class);
                assert user != null;
                tvNoms.setText(user.getUnoms());
                tvTelephone.setText(user.getUtelephone());
                Glide.with(SettingsActivity.this)
                        .load(user.getUavatar())
                        .placeholder(R.drawable.lion)
                        .error(R.drawable.ic_action_person)
                        .centerCrop()
                        .into(ivAvatar);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(SettingsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemPreference();
        setContentView(R.layout.activity_settings);
        checkuser();
    }

    public void systemPreference() {
        SharedPreferences preferences = getSharedPreferences("ABOMO_PREF", MODE_PRIVATE);
        isTheme = preferences.getBoolean("THEME", false);
        if (isTheme){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        }
        isLanguage = preferences.getBoolean("LANGUAGE", false);
        if (isLanguage){

        }else {

        }
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
        ivAvatar = findViewById(R.id.ivAvatar);
        tvNoms = findViewById(R.id.tvNoms);
        tvTelephone = findViewById(R.id.tvTelephone);
        swTheme = findViewById(R.id.swTheme);
        swTheme.setChecked(isTheme);
        swTheme.setText(getString(R.string.theme_mode, isTheme?getString(R.string.mode_dark):getString(R.string.mode_day)));
        swLanguage = findViewById(R.id.swLanguage);
        swLanguage.setChecked(isLanguage);
        reference.child(USERS).orderByKey().equalTo(myuid).addListenerForSingleValueEvent(valuser);
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
            swTheme.setText(getString(R.string.theme_mode, isChecked?getString(R.string.mode_dark):getString(R.string.mode_day)));
            saveAppThemePreference(preferences, isChecked);
        }else if (buttonView.getId() == R.id.swLanguage){
            if (isChecked){

            }else {

            }
            saveAppLanguagePreference(preferences, isChecked);
        }
    }




}