package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.FAVORITES;
import static com.deone.abomo.outils.ConstantsTools.LIKES;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.SIGNALES;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.deconnecter;
import static com.deone.abomo.outils.MethodTools.formatDetailsPost;
import static com.deone.abomo.outils.MethodTools.loadSystemPreference;
import static com.deone.abomo.outils.MethodTools.supprimerUnPost;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.deone.abomo.models.Favorite;
import com.deone.abomo.models.Like;
import com.deone.abomo.models.Post;
import com.deone.abomo.models.Signale;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ShowPostActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Post post;
    private DatabaseReference reference;
    private Toolbar toolbar;
    private String myuid;
    private String pid;
    private String uid;
    private boolean likeProcess = false;
    private boolean favoriteProcess = false;
    private boolean signaleProcess = false;
    private ImageView ivCover;
    private TextView tvPost;
    private TextView tvSupprimer;
    private TextView tvSignaler;
    private TextView tvTravaux;
    private TextView tvWork;
    private TextView tvPhotos;
    private TextView tvCommentaires;
    private TextView tvJaime;
    private TextView tvFavorite;
    private TextView tvPartage;
    private TextView tvSignalement;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
                assert post != null;
                if (pid.equals(post.getPid())){
                    tvPost.setText(HtmlCompat.fromHtml(formatDetailsPost(ShowPostActivity.this,
                            ""+post.getPtitre(), ""+post.getPdescription(),
                            ""+post.getPdate()), 0));
                    Picasso.get().load(post.getPcover()).placeholder(R.drawable.ic_action_add_image).into(ivCover);
                    tvJaime.setText(post.getPnlikes());
                    tvJaime.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            ds.child(LIKES).hasChild(myuid) ? R.drawable.ic_action_jaime:R.drawable.ic_action_unjaime,
                            0);
                    tvFavorite.setText(post.getPnfavorites());
                    tvFavorite.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            ds.child(FAVORITES).hasChild(myuid) ? R.drawable.ic_action_favorite:R.drawable.ic_action_unfavorite,
                            0);
                    tvPartage.setText(post.getPnshares());
                    tvSignalement.setText(post.getPnsignales());
                    tvWork.setText(post.getPntravaux());

                    /*initializeLikes(ds);
                    initializeFavorites(ds);
                    initializeSignales(ds);
                    initializeShares(ds);*/
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ShowPostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSystemPreference(this);
        setContentView(R.layout.activity_show_post);
        checkuser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            pid = dataForActivity(this, "pid");
            uid = dataForActivity(this, "uid");
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
            reference.child(POSTS).addValueEventListener(valpost);
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(getString(R.string.post_infos));
        ivCover = findViewById(R.id.ivCover);
        tvJaime = findViewById(R.id.tvJaime);
        tvFavorite = findViewById(R.id.tvFavorite);
        tvPartage = findViewById(R.id.tvPartage);
        tvSignalement = findViewById(R.id.tvSignalement);
        tvPost = findViewById(R.id.tvPost);
        tvTravaux = findViewById(R.id.tvTravaux);
        tvWork = findViewById(R.id.tvWork);
        tvPhotos = findViewById(R.id.tvPhotos);
        tvCommentaires = findViewById(R.id.tvCommentaires);
        tvSupprimer = findViewById(R.id.tvSupprimer);
        tvSupprimer.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        tvSignaler = findViewById(R.id.tvSignaler);
        tvSignaler.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        tvSupprimer.setOnClickListener(this);
        tvSignaler.setOnClickListener(this);
        tvJaime.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
        tvPartage.setOnClickListener(this);
        tvSignalement.setOnClickListener(this);
        tvTravaux.setOnClickListener(this);
        tvWork.setOnClickListener(this);
        tvPhotos.setOnClickListener(this);
        tvCommentaires.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvSupprimer && myuid.equals(uid)){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.del_post))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, which) -> supprimerUnPost(ShowPostActivity.this, reference, ""+pid))
                    .create().show();
        } else if (id == R.id.tvSignaler && !myuid.equals(uid)){
            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_signaler);
            TextView tvTitre = dialog.findViewById(R.id.tvTitre);
            TextView tvMessage = dialog.findViewById(R.id.tvMessage);
            CheckBox cbSignale = dialog.findViewById(R.id.cbSignale);
            Button btSignaler = dialog.findViewById(R.id.btSignaler);
            Button btAnnuler = dialog.findViewById(R.id.btAnnuler);
            btSignaler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btAnnuler.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        } else if (id == R.id.tvJaime && !myuid.equals(uid)){
            aimerUnPost();
        } else if (id == R.id.tvFavorite && !myuid.equals(uid)){
            favoriteUnPost();
        } else if (id == R.id.tvPartage && !myuid.equals(uid)){
            partagerUnPost();
        } else if (id == R.id.tvSignalement){
            signalerUnPost();
        } else if (id == R.id.tvWork){

        } else if (id == R.id.tvPhotos){
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        } else if (id == R.id.tvCommentaires){
            Intent intent = new Intent(this, CommentsActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.swShowGallery){

        }else if (id == R.id.swNotifComment){

        }
    }


    private void initializeFavorites(DataSnapshot ds) {
        tvFavorite.setText(post.getPnfavorites());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Favorite favorite = dsnapshot.getValue(Favorite.class);
            assert favorite != null;
            if (favorite.getFid().equals(myuid)){
                favoriteProcess = true;
                tvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
            }
        }
    }

    private void initializeSignales(DataSnapshot ds) {
        tvSignalement.setText(post.getPnsignales());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Signale signale = dsnapshot.getValue(Signale.class);
            assert signale != null;
            if (signale.getSid().equals(myuid)){
                signaleProcess = true;
                tvSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
            }
        }
    }

    private void initializeShares(DataSnapshot ds) {
        tvPartage.setText(post.getPnshares());
    }

    private void aimerUnPost() {
        likeProcess = true;
        final DatabaseReference likesRef = reference.child(POSTS).child(pid).child(LIKES);
        final DatabaseReference postsRef = reference.child(POSTS);
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (likeProcess){
                    if (snapshot.hasChild(myuid)){
                        postsRef.child(pid).child("pnlikes").setValue(""+ (Integer.parseInt(post.getPnlikes()) - 1));
                        likesRef.child(myuid).removeValue();
                        tvJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unjaime, 0);
                        Toast.makeText(ShowPostActivity.this, "Je n'aime pas cette publication", Toast.LENGTH_SHORT).show();
                    } else {
                        postsRef.child(pid).child("pnlikes").setValue(""+ (Integer.parseInt(post.getPnlikes()) + 1));
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("lid", myuid);
                        hashMap.put("ldate", timestamp);
                        likesRef.child(myuid).setValue(hashMap);
                        tvJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_jaime, 0);
                        Toast.makeText(ShowPostActivity.this, "J'aime cette publication", Toast.LENGTH_SHORT).show();
                    }
                    likeProcess = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void partagerUnPost() {
        /*final DatabaseReference likesRef = reference.child(POSTS).child(pid).child(SHARES);
        final DatabaseReference postsRef = reference.child(POSTS);
        post.getPnshares()*/
    }

    public  void favoriteUnPost() {
        favoriteProcess = true;
        final DatabaseReference favoritesRef = reference.child(POSTS).child(pid).child(FAVORITES);
        final DatabaseReference postsRef = reference.child(POSTS);
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (favoriteProcess){
                    if (snapshot.hasChild(myuid)){
                        postsRef.child(pid).child("pnfavorites").setValue(""+ (Integer.parseInt(post.getPnfavorites()) - 1));
                        favoritesRef.child(uid).removeValue();
                        tvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unfavorite, 0);
                        Toast.makeText(ShowPostActivity.this, "Publication non favorite", Toast.LENGTH_SHORT).show();
                    } else {
                        postsRef.child(pid).child("pnfavorites").setValue(""+ (Integer.parseInt(post.getPnfavorites()) + 1));
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("fid", uid);
                        hashMap.put("fdate", timestamp);
                        favoritesRef.child(uid).setValue(hashMap);
                        tvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
                        Toast.makeText(ShowPostActivity.this, "Publication favorite", Toast.LENGTH_SHORT).show();
                    }
                    favoriteProcess = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void signalerUnPost() {
        if (signaleProcess){
            reference.child(POSTS).child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnsignales()) - 1));
            reference.child(POSTS).child(pid).child(SIGNALES).child(uid).removeValue();
            tvSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unsignaler, 0);
            Toast.makeText(ShowPostActivity.this, "Publication non signalée", Toast.LENGTH_SHORT).show();
            signaleProcess = false;
        }else {
            reference.child(POSTS).child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnsignales()) + 1));
            String timestamp = String.valueOf(System.currentTimeMillis());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("lid", uid);
            hashMap.put("ldate", timestamp);
            reference.child(POSTS).child(pid).child(SIGNALES).child(uid).setValue(hashMap);
            tvSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
            Toast.makeText(ShowPostActivity.this, "Publication signalée", Toast.LENGTH_SHORT).show();
            signaleProcess = true;
        }
    }

}