package com.deone.abomo.models;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.MethodTools.preparePostData;
import static com.deone.abomo.outils.MethodTools.showProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.deone.abomo.MainActivity;
import com.deone.abomo.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Post implements Comparable<Post>{
    private String pid;
    private String ptitre;
    private String pcover;
    private String pdescription;
    private String pnlikes;
    private String pnfavorites;
    private String pnshares;
    private String pnsignales;
    private String pncompares;
    private String pnimages;
    private String pncommentaires;
    private String pnvues;
    private String pnnotes;
    private String pntravaux;
    private String pdate;
    private String uid;
    private String unoms;
    private String uavatar;

    public Post() {
    }

    public Post(String pid, String ptitre, String pcover, String pdescription, String pnlikes, String pnfavorites, String pnshares, String pnsignales, String pncompares, String pnimages, String pncommentaires, String pnvues, String pnnotes, String pntravaux, String pdate, String uid, String unoms, String uavatar) {
        this.pid = pid;
        this.ptitre = ptitre;
        this.pcover = pcover;
        this.pdescription = pdescription;
        this.pnlikes = pnlikes;
        this.pnfavorites = pnfavorites;
        this.pnshares = pnshares;
        this.pnsignales = pnsignales;
        this.pncompares = pncompares;
        this.pnimages = pnimages;
        this.pncommentaires = pncommentaires;
        this.pnvues = pnvues;
        this.pnnotes = pnnotes;
        this.pntravaux = pntravaux;
        this.pdate = pdate;
        this.uid = uid;
        this.unoms = unoms;
        this.uavatar = uavatar;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPtitre() {
        return ptitre;
    }

    public void setPtitre(String ptitre) {
        this.ptitre = ptitre;
    }

    public String getPcover() {
        return pcover;
    }

    public void setPcover(String pcover) {
        this.pcover = pcover;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getPnlikes() {
        return pnlikes;
    }

    public void setPnlikes(String pnlikes) {
        this.pnlikes = pnlikes;
    }

    public String getPnfavorites() {
        return pnfavorites;
    }

    public void setPnfavorites(String pnfavorites) {
        this.pnfavorites = pnfavorites;
    }

    public String getPnshares() {
        return pnshares;
    }

    public void setPnshares(String pnshares) {
        this.pnshares = pnshares;
    }

    public String getPnsignales() {
        return pnsignales;
    }

    public void setPnsignales(String pnsignales) {
        this.pnsignales = pnsignales;
    }

    public String getPncompares() {
        return pncompares;
    }

    public void setPncompares(String pncompares) {
        this.pncompares = pncompares;
    }

    public String getPnimages() {
        return pnimages;
    }

    public void setPnimages(String pnimages) {
        this.pnimages = pnimages;
    }

    public String getPncommentaires() {
        return pncommentaires;
    }

    public void setPncommentaires(String pncommentaires) {
        this.pncommentaires = pncommentaires;
    }

    public String getPnvues() {
        return pnvues;
    }

    public void setPnvues(String pnvues) {
        this.pnvues = pnvues;
    }

    public String getPnnotes() {
        return pnnotes;
    }

    public void setPnnotes(String pnnotes) {
        this.pnnotes = pnnotes;
    }

    public String getPntravaux() {
        return pntravaux;
    }

    public void setPntravaux(String pntravaux) {
        this.pntravaux = pntravaux;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUnoms() {
        return unoms;
    }

    public void setUnoms(String unoms) {
        this.unoms = unoms;
    }

    public String getUavatar() {
        return uavatar;
    }

    public void setUavatar(String uavatar) {
        this.uavatar = uavatar;
    }

    @Override
    public int compareTo(Post o) {
        return 0;
    }

    public void ajouterPost(Activity activity){
        ProgressDialog progressDialog = showProgressDialog(
                activity,
                ""+activity.getString(R.string.app_name),
                ""+activity.getString(R.string.save_user));
        HashMap<String, String> hashMap = preparePostData(
                pid, ""+ ptitre, ""+ pcover, ""+ pdescription,
                "0", "0","0", "0",
                "0", "0", "0", "0", "0",
                "0", ""+ pdate, ""+uid,  ""+unoms, ""+uavatar);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
        reference.child(POSTS).child(pid).setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(
                            activity,
                            ""+activity.getString(R.string.save_user_info_ok),
                            Toast.LENGTH_SHORT
                    ).show();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(
                    activity,
                    ""+e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    public void ajouterImage(Activity activity, Uri imageUri, String field){
        String filePathAndName = "Immeubles/"+"immeuble_" + pid + "_" +  field + "_" + pdate;
        ProgressDialog progressDialog = showProgressDialog(
                activity,
                ""+activity.getString(R.string.app_name),
                ""+activity.getString(R.string.add_image_message, filePathAndName));
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful());

            String downloadUri = uriTask.getResult().toString();
            if (uriTask.isSuccessful()){
                progressDialog.dismiss();
                Toast.makeText(
                        activity,
                        ""+activity.getString(R.string.save_image_ok, filePathAndName),
                        Toast.LENGTH_SHORT
                ).show();
                this.setPcover(downloadUri);
                this.ajouterPost(activity);
            }else
                progressDialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(
                    activity,
                    ""+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        });
    }
}
