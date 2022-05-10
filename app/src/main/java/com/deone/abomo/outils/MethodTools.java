package com.deone.abomo.outils;

import static com.deone.abomo.outils.ConstantsTools.CAMERA_REQUEST_CODE;
import static com.deone.abomo.outils.ConstantsTools.STORAGE_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.deone.abomo.MainActivity;
import com.deone.abomo.R;
import com.deone.abomo.SignInActivity;
import com.deone.abomo.models.Post;
import com.deone.abomo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MethodTools {
    public static boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    public static void saveAppLanguagePreference(SharedPreferences preferences, boolean isChecked) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("LANGUAGE", isChecked);
        editor.apply();
    }
    public static void saveAppThemePreference(SharedPreferences preferences, boolean isChecked) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("THEME", isChecked);
        editor.apply();
    }
    public static String timestampToString(Context context, String format, String date) {
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

    public static void creerUnCompte(Activity activity, Uri imageUri, String nomComplet, String telephone, String utilisateur, String motdepasse) {
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
                                    "",
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
                                                          String pnvues, String pnnotes,
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
        hashMap.put("pncommentaires", pncommentaires);
        hashMap.put("pnvues", pnvues);
        hashMap.put("pnnotes", pnnotes);
        hashMap.put("pdate", pdate);
        hashMap.put("uid", uid);
        hashMap.put("unoms", unoms);
        hashMap.put("uavatar", uavatar);
        return hashMap;
    }

    public static void creerUnPost(Activity activity, Uri imageUri, String titre, String description, String uid, String unoms, String uavatar) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        Post post = new Post(""+timestamp, ""+titre,
                "", ""+description,
                "", "",
                "", "",
                "", "",
                "", "",
                "", ""+timestamp,
                ""+uid, ""+unoms, ""+uavatar);
        if (imageUri != null){
            post.ajouterImage(activity, imageUri, "");
        }else {
            post.ajouterPost(activity);
        }
    }
}
