package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.CAMERA_REQUEST_CODE;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.IMAGES;
import static com.deone.abomo.outils.ConstantsTools.IMAGE_PICK_CAMERA_CODE;
import static com.deone.abomo.outils.ConstantsTools.IMAGE_PICK_GALLERY_CODE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.STORAGE_REQUEST_CODE;
import static com.deone.abomo.outils.MethodTools.appPreferences;
import static com.deone.abomo.outils.MethodTools.checkCameraPermissions;
import static com.deone.abomo.outils.MethodTools.checkStoragePermissions;
import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.requestCameraPermissions;
import static com.deone.abomo.outils.MethodTools.requestStoragePermissions;
import static com.deone.abomo.outils.MethodTools.showProgressDialog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class AddImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCover;
    private EditText edtvTitre;
    private EditText edtvDescription;
    private Uri imageUri = null;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private String pid;
    private String pnimages;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences(this);
        setContentView(R.layout.activity_add_image);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            pid = dataForActivity(this, "pid");
            pnimages = dataForActivity(this, "pnimages");
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        ivCover = findViewById(R.id.ivCover);
        edtvTitre = findViewById(R.id.edtvTitre);
        edtvDescription = findViewById(R.id.edtvDescription);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        findViewById(R.id.btAddImage).setOnClickListener(this);
        ivCover.setOnClickListener(this);
    }

    private void showImageDialog() {
        String[] options = {getString(R.string.camera), getString(R.string.gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectionner_une_image));
        builder.setItems(options, (dialog, which) -> {
            switch(which){
                case 0 :
                    if (!checkCameraPermissions(this)){
                        requestCameraPermissions(this, cameraPermissions);
                    }else{
                        pickFromCamera();
                    }
                    break;
                case 1 :
                    if (!checkStoragePermissions(this)){
                        requestStoragePermissions(this, storagePermissions);
                    }else{
                        pickFromGallery();
                    }
                    break;
                default:
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Post gallery Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Post gallery Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void verificationDesChamps() {
        String titre = edtvTitre.getText().toString().trim();
        String description = edtvDescription.getText().toString().trim();
        if (titre.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.titre_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (description.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.description_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        ajouterUneImage(""+titre, ""+description);
    }

    private void ajouterUneImage(String titre, String description) {
        if (imageUri == null){
            Toast.makeText(
                    this,
                    ""+getString(R.string.image_error),
                    Toast.LENGTH_SHORT
            ).show();
        }else {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String filePathAndName = "Post/" + pid + "/Images/"  + titre.replace(" ", "") + "_" + timestamp;
            ProgressDialog progressDialog = showProgressDialog(
                    this,
                    ""+getString(R.string.app_name),
                    ""+getString(R.string.add_image_message, filePathAndName));
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());

                String downloadUri = uriTask.getResult().toString();
                if (uriTask.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(
                            this,
                            ""+getString(R.string.save_image_ok, filePathAndName),
                            Toast.LENGTH_SHORT
                    ).show();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("iid", timestamp);
                    hashMap.put("icover", downloadUri);
                    hashMap.put("ititre", titre);
                    hashMap.put("idescription", description);
                    hashMap.put("idate", timestamp);

                    saveData(timestamp, hashMap);
                }else
                    progressDialog.dismiss();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(
                        this,
                        ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveData(String timestamp, HashMap<String, String> hashMap) {
        reference.child(POSTS).child(pid)
                .child(IMAGES).child(timestamp)
                .setValue(hashMap).addOnSuccessListener(unused -> reference.child(POSTS).child(pid)
                        .child("pnimages")
                        .setValue(""+ (Integer.parseInt(pnimages) + 1))
                        .addOnSuccessListener(unused1 -> {
                            Toast.makeText(AddImageActivity.this, ""+getString(R.string.success_image), Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(AddImageActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> Toast.makeText(AddImageActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btAddImage){
            verificationDesChamps();
        } else if (id == R.id.ivCover){
            showImageDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }else {
                        Toast.makeText(
                                this,
                                ""+getString(R.string.enable_camera_storage_permissions),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickFromGallery();
                    }else {
                        Toast.makeText(
                                this,
                                ""+getString(R.string.enable_storage_permissions),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
            break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                assert data != null;
                imageUri = data.getData();
                ivCover.setImageURI(imageUri);
            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                ivCover.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}