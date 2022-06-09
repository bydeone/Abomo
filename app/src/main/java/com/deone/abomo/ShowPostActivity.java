package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.COMMENTS;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.FAVORITES;
import static com.deone.abomo.outils.ConstantsTools.FORMAT_DATE_SIMPLE;
import static com.deone.abomo.outils.ConstantsTools.LIKES;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.SIGNALES;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.ConstantsTools.WORK;
import static com.deone.abomo.outils.MethodTools.appPreferences;
import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.formatDetailsPost;
import static com.deone.abomo.outils.MethodTools.formatHeureJourAn;
import static com.deone.abomo.outils.MethodTools.supprimerUnPost;
import static com.deone.abomo.outils.MethodTools.timestampToString;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deone.abomo.adapters.AdapterCommentaire;
import com.deone.abomo.models.Commentaire;
import com.deone.abomo.models.Favorite;
import com.deone.abomo.models.Note;
import com.deone.abomo.models.Post;
import com.deone.abomo.models.Signale;
import com.deone.abomo.models.User;
import com.deone.abomo.outils.Alistener;
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
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowPostActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private Toolbar toolbar;
    private String myuid;
    private String pid;
    private String uid;
    private boolean likeProcess = false;
    private boolean favoriteProcess = false;
    private boolean signaleProcess = false;
    private boolean commentProcess = false;
    private ImageView ivCover;
    private ImageView ivAvatar;
    private TextView tvPostTitre;
    private TextView tvPostInfos;
    private TextView tvPost;
    private RatingBar rtNoterPost;
    private TextView tvRapidGallery;
    private TextView tvRapidWork;
    private TextView tvRapidJaime;
    private TextView tvRapidFavorite;
    private TextView tvRapidSignalement;
    private RecyclerView rvComments;
    private EditText edtvComment;
    private List<Commentaire> commentaireList;
    private Post post;
    private User user;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
                assert post != null;
                tvPostTitre.setText(post.getPtitre());
                tvPostInfos.setText(formatHeureJourAn(""+post.getPdate()));
                tvPost.setText(post.getPdescription());
                Glide.with(getApplicationContext())
                        .load(post.getUavatar())
                        .placeholder(R.drawable.lion)
                        .error(R.drawable.ic_action_person)
                        .centerCrop()
                        .into(ivAvatar);
                Glide.with(getApplicationContext())
                        .load(post.getPcover())
                        .placeholder(R.drawable.lion)
                        .error(R.drawable.lion)
                        .centerCrop()
                        .into(ivCover);
                tvRapidGallery.setText(post.getPnimages());
                tvRapidJaime.setText(post.getPnlikes());
                tvRapidJaime.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        ds.child(LIKES).hasChild(myuid) ? R.drawable.ic_action_jaime:R.drawable.ic_action_unjaime,
                        0);
                tvRapidFavorite.setText(post.getPnfavorites());
                tvRapidFavorite.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        ds.child(FAVORITES).hasChild(myuid) ? R.drawable.ic_action_favorite:R.drawable.ic_action_unfavorite,
                        0);
                tvRapidSignalement.setText(post.getPnsignales());
                tvRapidSignalement.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        ds.child(SIGNALES).hasChild(myuid) ? R.drawable.ic_action_signaler:R.drawable.ic_action_unsignaler,
                        0);
                tvRapidWork.setText(post.getPntravaux());
                tvRapidWork.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        ds.child(WORK).hasChild(myuid) ? R.drawable.ic_action_custom_work:R.drawable.ic_action_travaux,
                        0);

                if (!post.getPnnotes().equals("0")){
                    float note = Float.parseFloat(post.getPnnotes())/4;
                    rtNoterPost.setRating(note);
                    if (note<=2){
                        rtNoterPost.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(ShowPostActivity.this, R.color.red)));
                    } else if (note<=4){
                        rtNoterPost.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(ShowPostActivity.this, R.color.gold)));
                    } else if (note<=5){
                        rtNoterPost.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(ShowPostActivity.this, R.color.yellow)));
                    }
                }

                /*initializeLikes(ds);
                initializeFavorites(ds);
                initializeSignales(ds);
                initializeShares(ds);*/
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ShowPostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private final ValueEventListener valuser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot ds : snapshot.getChildren()){
                user = ds.getValue(User.class);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ShowPostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private final ValueEventListener valcomments = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            commentaireList.clear();
            for (DataSnapshot ds : snapshot.getChildren()){
                Commentaire commentaire = ds.getValue(Commentaire.class);
                commentaireList.add(commentaire);
                AdapterCommentaire adapterCommentaire = new AdapterCommentaire(ShowPostActivity.this, commentaireList);
                rvComments.setAdapter(adapterCommentaire);
                adapterCommentaire.setListener(new Alistener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                });
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
        appPreferences(this);
        setContentView(R.layout.activity_show_post);
        checkuser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_info_post){
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra("pid", pid);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
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
            //
            Query query = reference.child(POSTS).orderByKey().equalTo(pid);
            query.addValueEventListener(valpost);
            //
            Query uquery = reference.child(USERS).orderByKey().equalTo(myuid);
            uquery.addValueEventListener(valuser);
            //
            reference.child(POSTS).child(pid).child(COMMENTS).addValueEventListener(valcomments);
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        // Image de couverture de la publication
        ivCover = findViewById(R.id.ivCover);
        // Image du proprietaire de la publication
        ivAvatar = findViewById(R.id.ivAvatar);
        //
        tvPostTitre = findViewById(R.id.tvPostTitre);
        //
        tvPostInfos = findViewById(R.id.tvPostInfos);
        //
        rtNoterPost = findViewById(R.id.rtNoterPost);
        // Liste des boutons d'action
        tvRapidJaime = findViewById(R.id.tvRapidJaime);
        //
        tvRapidGallery = findViewById(R.id.tvRapidGallery);
        //
        tvRapidFavorite = findViewById(R.id.tvRapidFavorite);
        //
        ImageButton ibRapidPartage = findViewById(R.id.ibRapidPartage);
        //
        tvRapidSignalement = findViewById(R.id.tvRapidSignalement);
        //
        tvPost = findViewById(R.id.tvPost);
        //
        tvRapidWork = findViewById(R.id.tvRapidWork);
        // Affichage des commentaires
        rvComments = findViewById(R.id.rvComments);
        rvComments.setHasFixedSize(false);
        rvComments.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvComments.setLayoutManager(layoutManager);
        commentaireList = new ArrayList<>();
        edtvComment = findViewById(R.id.edtvComment);
        findViewById(R.id.ibComment).setOnClickListener(this);
        tvRapidJaime.setOnClickListener(this);
        tvRapidGallery.setOnClickListener(this);
        tvRapidFavorite.setOnClickListener(this);
        ibRapidPartage.setOnClickListener(this);
        tvRapidSignalement.setOnClickListener(this);
        tvRapidWork.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvPostTitre.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvRapidJaime && !myuid.equals(uid)){
            aimerUnPost();
        } else if (id == R.id.tvRapidFavorite && !myuid.equals(uid)){
            favoriteUnPost();
        } else if (id == R.id.ibRapidPartage && !myuid.equals(uid)){
            partagerUnPost();
        } else if (id == R.id.tvRapidSignalement){
            proccessSignaler();
        } else if (id == R.id.tvRapidWork){
            workUnPost();
        } else if (id == R.id.tvRapidGallery){
            galleryUnPost();
        }else if (id == R.id.ibComment) {
            verificationCommentaire();
        } else if (id == R.id.ivAvatar) {
            // Affichez les informations de l'utilisateur
        } else if (id == R.id.tvPostTitre) {
            openPostDetails();
        }
    }

    private void openPostDetails() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("pid", pid);
        intent.putExtra("uid", uid);
        intent.putExtra("nom", user.getUnoms());
        intent.putExtra("avatar", user.getUavatar());
        startActivity(intent);
    }



    private void proccessSignaler() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_signaler);
        dialog.setTitle(getString(R.string.app_name));
        EditText tvMessage = dialog.findViewById(R.id.tvMessage);
        CheckBox cbSignale = dialog.findViewById(R.id.cbSignale);
        dialog.findViewById(R.id.btSignaler).setOnClickListener(v12 -> {
            String raison = tvMessage.getText().toString().trim();
            if (raison.isEmpty()){
                Toast.makeText(ShowPostActivity.this, ""+getString(R.string.no_raison), Toast.LENGTH_SHORT).show();
                return;
            }
            signalerUnPost(raison, cbSignale.isChecked());
            dialog.dismiss();
        });
        dialog.show();
    }

    private void galleryUnPost() {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("pid", pid);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    private void workUnPost() {

    }

    private void initializeFavorites(DataSnapshot ds) {
        tvRapidFavorite.setText(post.getPnfavorites());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Favorite favorite = dsnapshot.getValue(Favorite.class);
            assert favorite != null;
            if (favorite.getFid().equals(myuid)){
                favoriteProcess = true;
                tvRapidFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
            }
        }
    }

    private void initializeSignales(DataSnapshot ds) {
        tvRapidSignalement.setText(post.getPnsignales());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Signale signale = dsnapshot.getValue(Signale.class);
            assert signale != null;
            if (signale.getSid().equals(myuid)){
                signaleProcess = true;
                tvRapidSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
            }
        }
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
                        tvRapidJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unjaime, 0);
                        Toast.makeText(ShowPostActivity.this, "Je n'aime pas cette publication", Toast.LENGTH_SHORT).show();
                    } else {
                        postsRef.child(pid).child("pnlikes").setValue(""+ (Integer.parseInt(post.getPnlikes()) + 1));
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("lid", myuid);
                        hashMap.put("ldate", timestamp);
                        likesRef.child(myuid).setValue(hashMap);
                        tvRapidJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_jaime, 0);
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
                        postsRef.child(pid).child("pnfavorites").setValue(""+ (snapshot.getChildrenCount() - 1));
                        favoritesRef.child(uid).removeValue();
                        tvRapidFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unfavorite, 0);
                        Toast.makeText(ShowPostActivity.this, "Publication non favorite", Toast.LENGTH_SHORT).show();
                    } else {
                        postsRef.child(pid).child("pnfavorites").setValue(""+ snapshot.getChildrenCount());
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("fid", uid);
                        hashMap.put("fdate", timestamp);
                        favoritesRef.child(uid).setValue(hashMap);
                        tvRapidFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
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

    public void signalerUnPost(String raison, boolean blockUser) {
        signaleProcess = true;
        final DatabaseReference signalesRef = reference.child(POSTS).child(pid).child(SIGNALES);
        final DatabaseReference postsRef = reference.child(POSTS);
        signalesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (signaleProcess){
                    if (snapshot.hasChild(myuid)){
                        signalesRef.child(uid).removeValue();
                        tvRapidSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unsignaler, 0);
                        postsRef.child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnsignales()) -1 ));
                        Toast.makeText(ShowPostActivity.this, "Publication non signalée", Toast.LENGTH_SHORT).show();
                    } else {
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("sid", uid);
                        hashMap.put("sraison", raison);
                        hashMap.put("sdate", timestamp);
                        signalesRef.child(uid).setValue(hashMap);
                        tvRapidSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
                        postsRef.child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnsignales()) + 1));
                        Toast.makeText(ShowPostActivity.this, "Publication signalée" + snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                    }
                    signaleProcess = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verificationCommentaire() {
        String message = edtvComment.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, ""+getString(R.string.error_comment), Toast.LENGTH_SHORT).show();
            return;
        }
        formatData(message);
    }

    private void formatData(@NonNull String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cid", timestamp);
        hashMap.put("cmessage", message);
        hashMap.put("cnvues", "0");
        hashMap.put("cnlikes", "0");
        hashMap.put("cncommentaires", "0");
        hashMap.put("cnpartage", "0");
        hashMap.put("cnsignalement", "0");
        hashMap.put("cdate", timestamp);
        hashMap.put("uid", user.getUid());
        hashMap.put("unoms", user.getUnoms());
        hashMap.put("uavatar", user.getUavatar());
        saveData(timestamp, hashMap);
    }

    private void saveData(String timestamp, HashMap<String, String> hashMap) {
        final DatabaseReference commentsRef = reference.child(POSTS).child(pid).child(COMMENTS);
        final DatabaseReference postsRef = reference.child(POSTS);
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentsRef.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(unused -> {
                            postsRef.child(pid).child("pncommentaires").setValue(""+ (snapshot.getChildrenCount()))
                                    .addOnSuccessListener(unused1 -> {
                                        Toast.makeText(ShowPostActivity.this, ""+getString(R.string.success_comment), Toast.LENGTH_SHORT).show();
                                        edtvComment.setText(null);
                                        edtvComment.setHint(getString(R.string.votre_commentaire));
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ShowPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e -> Toast.makeText(ShowPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowPostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}