package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.CAMERA_REQUEST_CODE;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.IMAGE_PICK_CAMERA_CODE;
import static com.deone.abomo.outils.ConstantsTools.IMAGE_PICK_GALLERY_CODE;
import static com.deone.abomo.outils.ConstantsTools.STORAGE_REQUEST_CODE;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.appPreferences;
import static com.deone.abomo.outils.MethodTools.checkCameraPermissions;
import static com.deone.abomo.outils.MethodTools.checkStoragePermissions;
import static com.deone.abomo.outils.MethodTools.creerUnPost;
import static com.deone.abomo.outils.MethodTools.requestCameraPermissions;
import static com.deone.abomo.outils.MethodTools.requestStoragePermissions;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.deone.abomo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCover;
    private EditText edtvTitre;
    private EditText edtvDescription;
    private Uri imageUri;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private String myuid;
    private User user;
    private DatabaseReference reference;
    private final ValueEventListener valuser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                user = ds.getValue(User.class);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(AddPostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private final TextWatcher twTitre = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Toast.makeText(AddPostActivity.this, "Yes, yes - " + s, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void afterTextChanged(Editable s) {
            //edtvTitre.removeTextChangedListener(twTitre);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences(this);
        setContentView(R.layout.activity_add_post);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
            Query query = reference.child(USERS).orderByKey().equalTo(myuid);
            query.addListenerForSingleValueEvent(valuser);
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
        findViewById(R.id.btAddImmeuble).setOnClickListener(this);
        ivCover.setOnClickListener(this);
        edtvTitre.addTextChangedListener(twTitre);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btAddImmeuble){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.submit_conditions_message))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        verificationDesChamps();
                    }).create().show();
        }else if (v.getId() == R.id.ivCover){
            showImageDialog();
        }
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
        values.put(MediaStore.Images.Media.TITLE, "Post cover Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Post cover Description");
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
        creerUnPost(this, imageUri, ""+titre, ""+description,
                ""+myuid, ""+user.getUnoms(), ""+user.getUavatar());
    }

}