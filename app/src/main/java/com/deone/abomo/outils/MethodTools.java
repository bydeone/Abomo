package com.deone.abomo.outils;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.deone.abomo.outils.ConstantsTools.CAMERA_REQUEST_CODE;
import static com.deone.abomo.outils.ConstantsTools.FORMAT_DATE_FULL_FR;
import static com.deone.abomo.outils.ConstantsTools.FORMAT_DATE_SIMPLE;
import static com.deone.abomo.outils.ConstantsTools.FORMAT_DATE_SIMPLE_EN;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.STORAGE_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiContext;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.deone.abomo.MainActivity;
import com.deone.abomo.R;
import com.deone.abomo.SignInActivity;
import com.deone.abomo.models.Post;
import com.deone.abomo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MethodTools {
    public static String customTitre(Context context, String titre, String description, String date) {
        return context.getString(R.string.custom_post_titre, titre,
                description,
                timestampToString(
                        ""+FORMAT_DATE_FULL_FR,
                        ""+date));
    }

    public static String dataForActivity(Activity activity, String name) {
        return activity.getIntent().getStringExtra(name);
    }

    public static void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode.toLowerCase());
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(locale);
        resources.updateConfiguration(conf, resources.getDisplayMetrics());
    }

    public static void appPreferences(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("ABOMO_PREF", MODE_PRIVATE);
        initAppTheme(activity, preferences);
        initAppLanguage(activity, preferences);
        initAppLocalisation(activity, preferences);
    }

    private static void initAppLocalisation(Activity activity, SharedPreferences preferences) {
        String paysCode = null;
        try{
            final TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                paysCode = simCountry.toLowerCase(Locale.getDefault());
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    paysCode = networkCountry.toLowerCase(Locale.getDefault());
                }
            }
        }catch (Exception e){
            Log.d(""+activity.getString(R.string.app_name), ""+e.getMessage());
        }
        saveAppCountryPreference(preferences, paysCode);
    }

    private static void saveAppCountryPreference(SharedPreferences preferences, String paysCode) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("COUNTRY", paysCode);
        editor.apply();
    }

    private static void initAppLanguage(Activity activity, SharedPreferences preferences) {
        String langCode = preferences.getString("LANGUAGE", ""+Locale.getDefault().getLanguage());
        setLocal(activity, langCode);
        saveAppLanguagePreference(preferences, langCode);
        Log.d(""+activity.getString(R.string.app_name), "init App Language : Done");
    }

    private static void initAppTheme(Activity activity, SharedPreferences preferences) {
        boolean isTheme = preferences.getBoolean("THEME", false);
        AppCompatDelegate.setDefaultNightMode(isTheme ? MODE_NIGHT_YES : MODE_NIGHT_NO);
        saveAppThemePreference(preferences, isTheme);
        Log.d(""+activity.getString(R.string.app_name), "init App Theme : Done");
    }

    public static boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    public static void saveAppLanguagePreference(SharedPreferences preferences, String langCode) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LANGUAGE", langCode);
        editor.apply();
    }
    public static void saveAppThemePreference(SharedPreferences preferences, boolean isChecked) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("THEME", isChecked);
        editor.apply();
    }
    public static String timestampToString(String format, String date) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        cal.setTimeInMillis(Long.parseLong(date));
        return DateFormat.format(format, cal).toString();
    }

    public static ProgressDialog showProgressDialog(Activity activity, String titre, String message) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(titre);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public static void requestStoragePermissions(Activity activity, String[] storagePermissions) {
        ActivityCompat.requestPermissions(activity, storagePermissions, STORAGE_REQUEST_CODE);
    }

    public static boolean checkStoragePermissions(Context context) {
        boolean result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public static void requestCameraPermissions(Activity activity, String[] cameraPermissions) {
        ActivityCompat.requestPermissions(activity, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    public static boolean checkCameraPermissions(Context context) {
        boolean result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    public static HashMap<String, String> prepareUserData(String uid, String unoms, String ucover, String uavatar, String utelephone, String uemail, String ucni, String udelivrance, String ucodepostal, String uville, String uadresse, String upays, String udate) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("unoms", unoms);
        hashMap.put("ucover", ucover);
        hashMap.put("uavatar", uavatar);
        hashMap.put("utelephone", utelephone);
        hashMap.put("uemail", uemail);
        hashMap.put("ucni", ucni);
        hashMap.put("udelivrance", udelivrance);
        hashMap.put("ucodepostal", ucodepostal);
        hashMap.put("uville", uville);
        hashMap.put("uadresse", uadresse);
        hashMap.put("upays", upays);
        hashMap.put("udate", udate);
        return hashMap;
    }

    public static void creerUnCompte(Activity activity, Uri imageUri, String nomComplet, String telephone, String ville, String utilisateur, String motdepasse) {
        ProgressDialog progressDialog = showProgressDialog(
                activity,
                "" + activity.getString(R.string.app_name),
                "" + activity.getString(R.string.create_user));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(utilisateur, motdepasse)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        FirebaseUser fUser = mAuth.getCurrentUser();
                        if (fUser != null) {
                            String timestamp = String.valueOf(System.currentTimeMillis());
                            User user = new User(
                                    "" + fUser.getUid(),
                                    "" + nomComplet,
                                    "",
                                    "",
                                    "" + telephone,
                                    "" + utilisateur,
                                    "",
                                    "",
                                    "",
                                    ""+ville,
                                    "",
                                    "",
                                    "" + timestamp);
                            if (imageUri != null)
                                user.ajouterImage(activity, imageUri, "avatar");
                            else
                                user.ajouterUser(activity);
                        } else {
                            Toast.makeText(
                                    activity,
                                    "" + activity.getString(R.string.no_user),
                                    Toast.LENGTH_LONG
                            ).show();
                            activity.startActivity(new Intent(activity, SignInActivity.class));
                            activity.finish();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(
                                activity,
                                "" + activity.getString(R.string.create_account_error),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    public static void connecterUtilisateur(Activity activity, String utilisateur, String motdepasse) {
        ProgressDialog progressDialog = showProgressDialog(
                activity,
                "" + activity.getString(R.string.app_name),
                "" + activity.getString(R.string.connexion_user));
        FirebaseAuth.getInstance().signInWithEmailAndPassword(utilisateur, motdepasse)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                activity,
                                "" + activity.getString(R.string.sign_in_ok),
                                Toast.LENGTH_LONG
                        ).show();
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(
                                activity,
                                "" + activity.getString(R.string.sign_in_error),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    public static void deconnecter(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public static HashMap<String, String> preparePostData(String pid, String ptitre,
                                                          String pcover, String pdescription,
                                                          String pnlikes, String pnfavorites, String pnshares,
                                                          String pnsignales, String pncompares,
                                                          String pnimages, String pncommentaires,
                                                          String pnvues, String pnnotes, String pntravaux,
                                                          String pdate, String uid,
                                                          String unoms, String uavatar
                                                              ) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", pid);
        hashMap.put("ptitre", ptitre);
        hashMap.put("pcover", pcover);
        hashMap.put("pdescription", pdescription);
        hashMap.put("pnlikes", pnlikes);
        hashMap.put("pnfavorites", pnfavorites);
        hashMap.put("pnshares", pnshares);
        hashMap.put("pnsignales", pnsignales);
        hashMap.put("pncompares", pncompares);
        hashMap.put("pnimages", pnimages);
        hashMap.put("pntravaux", pntravaux);
        hashMap.put("pncommentaires", pncommentaires);
        hashMap.put("pnvues", pnvues);
        hashMap.put("pnnotes", pnnotes);
        hashMap.put("pdate", pdate);
        hashMap.put("uid", uid);
        hashMap.put("unoms", unoms);
        hashMap.put("uavatar", uavatar);
        return hashMap;
    }

    public static void creerUnPost(Activity activity, Uri imageUri, String titre, String description, String uid, String unoms, String uavatar, boolean isProprietaire) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        Post post = new Post(""+timestamp, ""+titre,
                "", ""+description,
                "0", "0",
                "0", "0",
                "0", "0",
                "0", "0", "0",
                "0", ""+timestamp,
                ""+uid, ""+unoms, ""+uavatar);
        if (imageUri != null){
            post.ajouterImage(activity, imageUri, "", isProprietaire);
        }else {
            post.ajouterPost(activity, isProprietaire);
        }
    }

    public static void supprimerUnPost(@UiContext @NonNull Activity activity, DatabaseReference reference, String pid) {
        reference.child(POSTS).child(pid).removeValue().addOnSuccessListener(unused -> {
            Toast.makeText(activity, ""+activity.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
            activity.finish();
        }).addOnFailureListener(e -> Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static String formatDetailsPost(Activity activity, String titre, String description, String date) {
        return "<h2>" +
                titre +
                "</h2>" +
                "<p>" +
                description +
                "</p>" +
                "<i>" +
                formatHeureJourAn(date)+
                "</i>";
    }

    public static String formatHeureJourAn(String date) {
        long timestamp = System.currentTimeMillis() - Long.parseLong(date);
        double seconde = timestamp * 0.001;
        if (seconde <= 59){
            return "à l'instant";
        } else if (seconde/60 <= 59){
            int result = (int) Math.ceil((seconde/60));
            return "il y'a " + result + " minutes";
        } else if (seconde/3600 <= 59){
            int result = (int) Math.ceil((seconde/3600));
            return "il y'a " + result + " heures";
        } else if (seconde/86400 <= 23){
            int result = (int) Math.ceil((seconde/86400));
            return "Publié il y'a " + result + " jours";
        } else if (seconde/2592000 <= 30){
            int result = (int) Math.ceil((seconde/2592000));
            return "il y'a " + result + " mois";
        } else if (seconde/31104000 >= 12){
            int result = (int) Math.ceil((seconde/31104000));
            return "il y'a " + result + " ans";
        }

        /*long seconds = TimeUnit.MILLISECONDS.toSeconds(timestamp);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timestamp);
        long hours = TimeUnit.MILLISECONDS.toHours(timestamp);
        long days = TimeUnit.MILLISECONDS.toDays(timestamp);*/

        return null;
    }

    public static String saveImageToSdCard(Activity activity) {
        File chemin = null;
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.lion);
        File sdCard = Environment.getExternalStorageDirectory();
        File repertoire = new File(sdCard.getAbsolutePath()+"/Expert/ExpertImage");
        if (repertoire.mkdirs()){
            chemin = new File(repertoire, "xLion.png");
            Toast.makeText(activity, "Image save to sd card", Toast.LENGTH_SHORT).show();
            try(OutputStream output = new FileOutputStream(chemin)){

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

                output.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert chemin != null;
        return chemin.getAbsolutePath();
    }

    public static String saveImageToInternalMemory(Activity activity) {
        ContextWrapper cw = new ContextWrapper(activity);
        File repertoire = cw.getDir("imagedir", MODE_PRIVATE);
        File chemin = new File(repertoire, "xLion.jpg");
        Bitmap bitmap = null;
        try(FileOutputStream fos = new FileOutputStream(chemin)){
            assert false;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chemin.getAbsolutePath();
    }

    public static String timestampToString(Context context, String date) {
        Locale current = context.getResources().getConfiguration().locale;
        Calendar cal = Calendar.getInstance(current == Locale.FRANCE ? Locale.FRANCE : Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(date));
        return DateFormat.format(current == Locale.FRANCE ? FORMAT_DATE_SIMPLE : FORMAT_DATE_SIMPLE_EN, cal).toString();
    }

    /*private final TextWatcher twTitre = new TextWatcher() {
        private int selPos;
        private String oldString, newString;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            selPos = edtvTitre.getSelectionStart();
            oldString = myFilter(s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            edtvTitre.removeTextChangedListener(this);
            newString = myFilter(s.toString());
            edtvTitre.setText(newString);
            int newPos = selPos + (newString.length() - oldString.length());
            if (newPos < 0) newPos = 0;
            edtvTitre.setSelection(newPos);
            edtvTitre.addTextChangedListener(this);
        }

        private String myFilter(String s) {
            String digits;
            digits = s.replaceAll("[^0-9]", "");
            if (s.equals("")) return "";
            return digits;
        }
    };*/

    public static float ratingNote(String note) {
        return Float.parseFloat(note)/4;
    }
    public static float noteRating(String note) {
        return Float.parseFloat(note)*4;
    }

}
