package com.deone.abomo;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.deone.abomo.outils.MethodTools.connecterUtilisateur;
import static com.deone.abomo.outils.MethodTools.initAppPreferences;
import static com.deone.abomo.outils.MethodTools.isNightMode;
import static com.deone.abomo.outils.MethodTools.saveAppThemePreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivLogo;
    private EditText edtvUtilisateur;
    private EditText edtvMotdepasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppPreferences(this);
        setContentView(R.layout.activity_sign_in);
        checkuser();
    }

    private void loadSystemPreference() {
        SharedPreferences preferences = getSharedPreferences("ABOMO_PREF", MODE_PRIVATE);
        boolean isTheme = isNightMode(this);
        if (isTheme){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        }
        saveAppThemePreference(preferences, isTheme);
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            initviews();
        }
    }

    private void initviews() {
        ivLogo = findViewById(R.id.ivLogo);
        edtvUtilisateur = findViewById(R.id.edtvUtilisateur);
        edtvMotdepasse = findViewById(R.id.edtvMotdepasse);
        findViewById(R.id.tvCreteAccoount).setOnClickListener(this);
        findViewById(R.id.tvForgetPassword).setOnClickListener(this);
        findViewById(R.id.btSignIn).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCreteAccoount){
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }else if (v.getId() == R.id.tvForgetPassword){
            resetMotdepasse();
        }else if (v.getId() == R.id.btSignIn){
            verificationDesChamps();
        }
    }

    private void resetMotdepasse() {

    }

    private void verificationDesChamps() {
        String utilisateur = edtvUtilisateur.getText().toString().trim();
        String motdepasse = edtvMotdepasse.getText().toString().trim();
        if (utilisateur.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.utilisateur_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (motdepasse.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.motdepasse_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        connecterUtilisateur(
                this,
                ""+utilisateur,
                ""+motdepasse
        );
    }

}