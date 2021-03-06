package com.deone.abomo;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.appPreferences;
import static com.deone.abomo.outils.MethodTools.deconnecter;
import static com.deone.abomo.outils.MethodTools.saveAppLanguagePreference;
import static com.deone.abomo.outils.MethodTools.saveAppThemePreference;
import static com.deone.abomo.outils.MethodTools.setLocal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseReference reference;
    private SharedPreferences preferences;
    private String uid;
    private String myuid;
    private boolean isTheme;
    private String langCode;
    private Toolbar toolbar;
    private ImageView ivAvatar;
    private TextView tvNoms;
    private TextView tvTelephone;
    private TextView tvLanguage;
    private TextView tvXpert;
    private TextView tvSignout;
    private SwitchCompat swTheme;
    private final ValueEventListener valuser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                User user = ds.getValue(User.class);
                assert user != null;
                tvNoms.setText(user.getUnoms());
                tvTelephone.setText(user.getUtelephone());
                Glide.with(getApplicationContext())
                        .load(user.getUavatar())
                        .placeholder(R.drawable.lion)
                        .error(R.drawable.ic_action_person)
                        .centerCrop()
                        .into(ivAvatar);
                toolbar.setTitle(user.getUnoms());
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
        appPreferences(this);
        setContentView(R.layout.activity_settings);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser != null){
            uid = getIntent().getStringExtra("uid");
            myuid = fuser.getUid();
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            preferences = getSharedPreferences("ABOMO_PREF", MODE_PRIVATE);
            isTheme = preferences.getBoolean("THEME", false);
            langCode = preferences.getString("LANGUAGE", "fr");
            initviews();
            Query query = reference.child(USERS).orderByKey().equalTo(myuid);
            query.addListenerForSingleValueEvent(valuser);
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getString(R.string.param_tres));
        ivAvatar = findViewById(R.id.ivAvatar);
        tvNoms = findViewById(R.id.tvNoms);
        tvTelephone = findViewById(R.id.tvTelephone);
        tvXpert = findViewById(R.id.tvXpert);
        swTheme = findViewById(R.id.swTheme);
        swTheme.setChecked(isTheme);
        swTheme.setText(getString(R.string.theme_mode, isTheme?getString(R.string.mode_dark):getString(R.string.mode_day)));
        tvLanguage = findViewById(R.id.tvLanguage);
        tvLanguage.setText(getString(R.string.language_mode, langCode.equals("fr")?getString(R.string.francais):getString(R.string.anglais)));
        findViewById(R.id.tvSignout).setOnClickListener(this);
        tvLanguage.setOnClickListener(this);
        tvXpert.setOnClickListener(this);
        swTheme.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvSignout){
            appDeconnexxion();
        } else if (id == R.id.tvXpert){
            openDialogModeExpert();
        }else if (id == R.id.tvLanguage){
            showSelectLanguageDialog();
        }
    }

    private void showSelectLanguageDialog() {
        String[] languages = {getString(R.string.select_language), getString(R.string.anglais), getString(R.string.francais)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_language));
        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (languages[which].equals(getString(R.string.select_language))){
                    Toast.makeText(SettingsActivity.this, ""+getString(R.string.no_language_selected), Toast.LENGTH_SHORT).show();
                } else if (languages[which].equals(getString(R.string.anglais))){
                    saveAppLanguagePreference(preferences, "en");
                    setLocal(SettingsActivity.this, "en");
                    finish();
                    startActivity(getIntent());
                }else if (languages[which].equals(getString(R.string.francais))){
                    saveAppLanguagePreference(preferences, "fr");
                    setLocal(SettingsActivity.this, "fr");
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void appDeconnexxion() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.sign_out))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        (dialog, which) -> deconnecter(this))
                .create().show();
    }

    private void openDialogModeExpert() {
        final CharSequence[] items = {"Zone", "Mercuriale"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.choose_expert));
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    // pour les zone
                }else if (which == 1){
                    startActivity(new Intent(SettingsActivity.this, MercurialeActivity.class));
                }
            }

        });
        dialogBuilder.create().show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.swTheme){
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            }
            swTheme.setText(getString(R.string.theme_mode, isChecked?getString(R.string.mode_dark):getString(R.string.mode_day)));
            saveAppThemePreference(preferences, isChecked);
        }
    }

}