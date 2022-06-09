package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.COMMENTS;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.SIGNALES;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.supprimerUnPost;
import static com.deone.abomo.outils.MethodTools.timestampToString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deone.abomo.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseReference reference;
    private Toolbar toolbar;
    private String myuid;
    private String pid;
    private String uid;
    private String nom;
    private String avatar;
    private String manote = "";
    private Post post;
    private boolean signaleProcess = false;
    private ImageView ivAvatar;
    private TextView tvPostTitre;
    private TextView tvPostVues;
    private TextView tvRapidGallery;
    private TextView tvRapidWork;
    private TextView tvRapidJaime;
    private TextView tvRapidFavorite;
    private TextView tvRapidSignalement;
    private TextView tvPostUsername;
    private TextView tvPostDescription;
    private TextView tvVisibility;
    private TextView tvNotations;
    private TextView tvComparaison;
    private TextView tvEstimation;
    private TextView tvGestionImmobiliere;
    private TextView tvInformations;
    private TextView tvSignaler;
    private TextView tvSupprimer;
    private RatingBar rtNote;
    private SwitchCompat swCommentaires;
    private SwitchCompat swNotifications;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
                Glide.with(PostActivity.this)
                        .load(post.getPcover())
                        .placeholder(R.drawable.lion)
                        .error(R.drawable.ic_action_person)
                        .centerCrop()
                        .into(ivAvatar);
                tvPostTitre.setText(post.getPtitre());
                if (ds.child("Notes").hasChild(myuid))
                    manote = ds.child("Notes").child(myuid).child("nnote").getValue(String.class);
                if (post.getPnvues().equals("0")){
                    tvPostVues.setText(getString(R.string.publish_at, timestampToString(PostActivity.this, ""+post.getPdate())));
                }else {
                    tvPostVues.setText(getString(R.string.publish_at_by, timestampToString(PostActivity.this, ""+post.getPdate()), post.getPnvues()));
                }
                tvRapidGallery.setText(post.getPnimages());
                tvRapidWork.setText(post.getPntravaux());
                tvRapidJaime.setText(post.getPnlikes());
                tvRapidFavorite.setText(post.getPnfavorites());
                tvRapidSignalement.setText(post.getPnsignales());

                if (!post.getPnnotes().equals("0")){
                    float note = Float.parseFloat(post.getPnnotes())/4;
                    rtNote.setRating(note);
                    if (note<=1){
                        rtNote.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(PostActivity.this, R.color.red)));
                    } else if (note<=3){
                        rtNote.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(PostActivity.this, R.color.gold)));
                    } else if (note<=5){
                        rtNote.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(PostActivity.this, R.color.yellow)));
                    }
                }

                tvPostUsername.setText(post.getUnoms());
                tvPostDescription.setText(post.getPdescription());
                tvSignaler.setVisibility(myuid.equals(post.getUid())?View.GONE:View.VISIBLE);
                tvSupprimer.setVisibility(myuid.equals(post.getUid())?View.VISIBLE:View.GONE);
                tvInformations.setVisibility(myuid.equals(post.getUid())?View.VISIBLE:View.GONE);
                tvGestionImmobiliere.setVisibility(myuid.equals(post.getUid())?View.VISIBLE:View.GONE);
                tvComparaison.setVisibility(myuid.equals(post.getUid())? View.GONE:View.VISIBLE);
                tvEstimation.setVisibility(myuid.equals(post.getUid())?View.VISIBLE:View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(PostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            pid = dataForActivity(this, "pid");
            uid = dataForActivity(this, "uid");
            nom = dataForActivity(this, "nom");
            avatar = dataForActivity(this, "avatar");
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
            Query query = reference.child(POSTS).orderByKey().equalTo(pid);
            query.addValueEventListener(valpost);
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        ivAvatar = findViewById(R.id.ivAvatar);
        tvPostTitre = findViewById(R.id.tvPostTitre);
        tvPostVues = findViewById(R.id.tvPostVues);
        tvRapidGallery = findViewById(R.id.tvRapidGallery);
        tvRapidWork = findViewById(R.id.tvRapidWork);
        tvRapidJaime = findViewById(R.id.tvRapidJaime);
        tvRapidFavorite = findViewById(R.id.tvRapidFavorite);
        tvRapidSignalement = findViewById(R.id.tvRapidSignalement);
        tvPostUsername = findViewById(R.id.tvPostUsername);
        tvPostDescription = findViewById(R.id.tvPostDescription);
        swCommentaires = findViewById(R.id.swCommentaires);
        swNotifications = findViewById(R.id.swNotifications);
        tvVisibility = findViewById(R.id.tvVisibility);
        tvNotations = findViewById(R.id.tvNotations);
        tvComparaison = findViewById(R.id.tvComparaison);
        tvEstimation = findViewById(R.id.tvEstimation);
        tvGestionImmobiliere = findViewById(R.id.tvGestionImmobiliere);
        tvInformations = findViewById(R.id.tvInformations);
        tvSupprimer = findViewById(R.id.tvSupprimer);
        tvSignaler = findViewById(R.id.tvSignaler);
        rtNote = findViewById(R.id.rtNote);

        tvRapidGallery.setOnClickListener(this);
        tvSupprimer.setOnClickListener(this);
        tvSignaler.setOnClickListener(this);
        tvComparaison.setOnClickListener(this);
        tvEstimation.setOnClickListener(this);
        tvGestionImmobiliere.setOnClickListener(this);
        tvInformations.setOnClickListener(this);
        tvVisibility.setOnClickListener(this);
        tvNotations.setOnClickListener(this);

        swCommentaires.setOnCheckedChangeListener(this);
        swNotifications.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvSupprimer && myuid.equals(uid)){
            showDeleteDialog();
        } else if (id == R.id.tvSignaler && !myuid.equals(uid)){
            showSignalerDialog();
        } else if (id == R.id.tvRapidGallery){
            accessToGallery();
        } else if (id == R.id.tvComparaison){
            accessToComparaison();
        } else if (id == R.id.tvEstimation){
            accessToEstimation();
        } else if (id == R.id.tvGestionImmobiliere){
            accessToGestionImmobiliere();
        } else if (id == R.id.tvInformations){
            accessToInformations();
        } else if (id == R.id.tvVisibility){
            accessToPicturesVisibility();
        } else if (id == R.id.tvNotations){
            noterUnPost();
        }
    }

    private void noterUnPost() {
        if (myuid.equals(uid)){
            if (!post.getPnnotes().equals("0")){
                Intent intent = new Intent(this, NotationsActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }else {
                Toast.makeText(this, ""+getString(R.string.pas_de_note), Toast.LENGTH_SHORT).show();
            }
        }else {
            final Dialog dialog = new Dialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            dialog.setContentView(R.layout.dialog_noter);
            ImageView ivEmoji = dialog.findViewById(R.id.ivEmoji);
            Glide.with(this)
                    .load(post.getUavatar())
                    .placeholder(R.drawable.lion)
                    .error(R.drawable.ic_action_person)
                    .centerCrop()
                    .into(ivEmoji);
            final RatingBar rtNoterPost = dialog.findViewById(R.id.rtNoterPost);
            rtNoterPost.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (rating<=1){
                        rtNoterPost.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(PostActivity.this, R.color.red)));

                    } else if (rating<=3){
                        rtNoterPost.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(PostActivity.this, R.color.gold)));

                    } else if (rating<=5){
                        rtNoterPost.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(PostActivity.this, R.color.yellow)));

                    }
                }
            });
            dialog.findViewById(R.id.btNoter).setOnClickListener(v12 -> {
                String note = String.valueOf(rtNoterPost.getRating());
                if (note.isEmpty()){
                    Toast.makeText(PostActivity.this, ""+getString(R.string.no_note), Toast.LENGTH_SHORT).show();
                    return;
                }
                prepareNoteData(dialog, note);
            });
            dialog.show();
        }
    }

    private void prepareNoteData(final Dialog dialog, String note) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nt = String.valueOf((20*Float.parseFloat(note))/5);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("nid", myuid);
        hashMap.put("nnote", nt);
        hashMap.put("uid", myuid);
        hashMap.put("unoms", nom);
        hashMap.put("uavatar", avatar);
        hashMap.put("ndate", timestamp);
        saveNoteData(dialog, hashMap);
    }

    private void saveNoteData(final Dialog dialog, HashMap<String, Object> hashMap) {
        if(manote.isEmpty()){
            reference.child(POSTS).child(pid).child("Notes").child(myuid)
                    .setValue(hashMap)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(PostActivity.this, ""+getString(R.string.add_note_success), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
        }else{
            reference.child(POSTS).child(pid).child("Notes").child(myuid)
                    .updateChildren(hashMap)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(PostActivity.this, ""+getString(R.string.update_note_success), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
        }
    }

    private void accessToPicturesVisibility() {
        /*Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);*/
    }

    private void accessToInformations() {
        /*Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);*/
    }

    private void accessToGestionImmobiliere() {
        Intent intent = new Intent(this, GestionImmobiliereActivity.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }

    private void accessToEstimation() {
        Intent intent = new Intent(this, EstimationActivity.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }

    private void accessToComparaison() {
        Intent intent = new Intent(this, ComparaisonActivity.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }

    private void accessToGallery() {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("pid", pid);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    private void showSignalerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_signaler);
        dialog.setTitle(getString(R.string.app_name));
        EditText tvMessage = dialog.findViewById(R.id.tvMessage);
        CheckBox cbSignale = dialog.findViewById(R.id.cbSignale);
        dialog.findViewById(R.id.btSignaler).setOnClickListener(v12 -> {
            String raison = tvMessage.getText().toString().trim();
            if (raison.isEmpty()){
                Toast.makeText(PostActivity.this, ""+getString(R.string.no_raison), Toast.LENGTH_SHORT).show();
                return;
            }
            signalerUnPost(raison, cbSignale.isChecked());
        });
        dialog.show();
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.del_post))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        (dialog, which) -> supprimerUnPost(PostActivity.this, reference, ""+pid))
                .create().show();
    }

    private void signalerUnPost(String raison, boolean checked) {
        signaleProcess = true;
        final DatabaseReference signalesRef = reference.child(POSTS).child(pid).child(SIGNALES);
        final DatabaseReference postsRef = reference.child(POSTS);
        signalesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (signaleProcess){
                    if (snapshot.hasChild(myuid)){
                        postsRef.child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnfavorites()) - 1));
                        signalesRef.child(uid).removeValue();
                        tvRapidSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unsignaler, 0);
                        Toast.makeText(PostActivity.this, "Publication non signalée", Toast.LENGTH_SHORT).show();
                    } else {
                        postsRef.child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnfavorites()) + 1));
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("sid", uid);
                        hashMap.put("sraison", raison);
                        hashMap.put("sdate", timestamp);
                        signalesRef.child(uid).setValue(hashMap);
                        tvRapidSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
                        Toast.makeText(PostActivity.this, "Publication signalée", Toast.LENGTH_SHORT).show();
                    }
                    signaleProcess = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.swCommentaires){

        } else if (id == R.id.swNotifications){

        }
    }

}