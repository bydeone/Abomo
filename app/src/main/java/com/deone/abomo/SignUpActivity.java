package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.CAMERA_REQUEST_CODE;
import static com.deone.abomo.outils.ConstantsTools.IMAGE_PICK_CAMERA_CODE;
import static com.deone.abomo.outils.ConstantsTools.IMAGE_PICK_GALLERY_CODE;
import static com.deone.abomo.outils.ConstantsTools.STORAGE_REQUEST_CODE;
import static com.deone.abomo.outils.MethodTools.appPreferences;
import static com.deone.abomo.outils.MethodTools.checkCameraPermissions;
import static com.deone.abomo.outils.MethodTools.checkStoragePermissions;
import static com.deone.abomo.outils.MethodTools.creerUnCompte;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivAvatar;
    private EditText edtvNons;
    private EditText edtvTelephone;
    private EditText edtvVille;
    private EditText edtvUtilisateur;
    private EditText edtvMotdepasse;
    private EditText edtvConfmotdepasse;
    private Button btSignUp;
    private Uri imageUri;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private final TextWatcher twUser = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nom = edtvNons.getText().toString().trim();
            String telephone = edtvTelephone.getText().toString().trim();
            String login = edtvUtilisateur.getText().toString().trim();
            String motdepasse = edtvMotdepasse.getText().toString().trim();
            btSignUp.setEnabled(!nom.isEmpty() && !telephone.isEmpty() && !login.isEmpty() && ! motdepasse.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences(this);
        setContentView(R.layout.activity_sign_up);
        checkuser();
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
        ivAvatar = findViewById(R.id.ivAvatar);
        edtvNons = findViewById(R.id.edtvNons);
        edtvTelephone = findViewById(R.id.edtvTelephone);
        edtvVille = findViewById(R.id.edtvVille);
        edtvUtilisateur = findViewById(R.id.edtvUtilisateur);
        edtvMotdepasse = findViewById(R.id.edtvMotdepasse);
        edtvConfmotdepasse = findViewById(R.id.edtvConfmotdepasse);
        cameraPermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        btSignUp = findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        edtvNons.addTextChangedListener(twUser);
        edtvMotdepasse.addTextChangedListener(twUser);
        edtvUtilisateur.addTextChangedListener(twUser);
        edtvTelephone.addTextChangedListener(twUser);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSignUp){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.submit_conditions_message))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        verificationDesChamps();
                    }).create().show();
        }else if (v.getId() == R.id.ivAvatar){
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

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "User Avatar Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "User Avatar Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                ivAvatar.setImageURI(imageUri);
            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                ivAvatar.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void verificationDesChamps() {
        String nomComplet = edtvNons.getText().toString().trim();
        String telephone = edtvTelephone.getText().toString().trim();
        String ville = edtvVille.getText().toString().trim();
        String utilisateur = edtvUtilisateur.getText().toString().trim();
        String motdepasse = edtvMotdepasse.getText().toString().trim();
        String confMotdepasse = edtvConfmotdepasse.getText().toString().trim();
        if (nomComplet.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.nomComplet_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (telephone.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.telephone_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (ville.isEmpty()){
            Toast.makeText(
                    this,
                    ""+getString(R.string.ville_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
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
        if (!confMotdepasse.equals(motdepasse)){
            Toast.makeText(
                    this,
                    ""+getString(R.string.confMotdepasse_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        creerUnCompte(
                this,
                imageUri,
                ""+nomComplet,
                ""+telephone,
                ""+ville,
                ""+utilisateur,
                ""+motdepasse
        );
    }
}