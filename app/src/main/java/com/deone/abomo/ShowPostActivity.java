package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.FORMAT_DATE_FULL_FR;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.MethodTools.timestampToString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deone.abomo.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowPostActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private String myuid;
    private String pid;
    private String uid;
    private Post post;
    private Toolbar toolbar;
    private ImageView ivCover;
    private TextView tvGallery;
    private TextView tvComments;
    private TextView tvJaime;
    private TextView tvFavorite;
    private TextView tvPartage;
    private TextView tvSignalement;
    private TextView tvDetails;
    private TextView tvGestion;
    private RecyclerView rvGallery;
    private TextView tvNotifications;
    private TextView tvShowGallery;
    private RecyclerView rvComments;
    private TextView tvStatistiques;
    private TextView tvSignaler;
    private TextView tvSupprimer;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
                tvDetails.setText(HtmlCompat.fromHtml(customTitre(), 0));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ShowPostActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private String customTitre() {
        return getString(R.string.custom_post_titre, post.getPtitre(),
                post.getPdescription(),
                timestampToString(ShowPostActivity.this,
                        ""+FORMAT_DATE_FULL_FR, ""+post.getPdate()),
                post.getPnvues());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSystemPreference();
        setContentView(R.layout.activity_show_post);
        checkuser();
    }

    private void loadSystemPreference() {

    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            pid = checkpostid();
            uid = checkpostuid();
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private String checkpostid() {
        return getIntent().getStringExtra("pid");
    }

    private String checkpostuid() {
        return getIntent().getStringExtra("uid");
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        ivCover = findViewById(R.id.ivCover);
        tvGallery = findViewById(R.id.tvGallery);
        tvGallery.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        tvComments = findViewById(R.id.tvComments);
        tvComments.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        tvJaime = findViewById(R.id.tvJaime);
        tvJaime.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        tvFavorite = findViewById(R.id.tvFavorite);
        tvFavorite.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        tvPartage = findViewById(R.id.tvPartage);
        tvPartage.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        tvSignalement = findViewById(R.id.tvSignalement);
        tvSignalement.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        tvDetails = findViewById(R.id.tvDetails);
        tvGestion = findViewById(R.id.tvGestion);
        tvGestion.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        LinearLayoutManager layoutManagerGallery = new LinearLayoutManager(this);
        layoutManagerGallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGallery.setLayoutManager(layoutManagerGallery);
        tvNotifications = findViewById(R.id.tvNotifications);
        tvNotifications.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        tvShowGallery = findViewById(R.id.tvShowGallery);
        tvShowGallery.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        rvComments = findViewById(R.id.rvComments);
        rvComments.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        LinearLayoutManager layoutManagerComments = new LinearLayoutManager(this);
        layoutManagerComments.setOrientation(LinearLayoutManager.VERTICAL);
        rvComments.setLayoutManager(layoutManagerComments);
        tvStatistiques = findViewById(R.id.tvStatistiques);
        tvStatistiques.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        tvSignaler = findViewById(R.id.tvSignaler);
        tvSignaler.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        tvSupprimer = findViewById(R.id.tvSupprimer);
        tvSupprimer.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        Query query = reference.child(POSTS).orderByKey().equalTo(pid);
        query.addListenerForSingleValueEvent(valpost);
        findViewById(R.id.tvGallery).setOnClickListener(this);
        findViewById(R.id.tvComments).setOnClickListener(this);
        findViewById(R.id.tvJaime).setOnClickListener(this);
        findViewById(R.id.tvFavorite).setOnClickListener(this);
        findViewById(R.id.tvPartage).setOnClickListener(this);
        findViewById(R.id.tvSignalement).setOnClickListener(this);
        findViewById(R.id.tvGestion).setOnClickListener(this);
        findViewById(R.id.tvNotifications).setOnClickListener(this);
        findViewById(R.id.tvShowGallery).setOnClickListener(this);
        findViewById(R.id.tvStatistiques).setOnClickListener(this);
        findViewById(R.id.tvSignaler).setOnClickListener(this);
        findViewById(R.id.tvSupprimer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGallery){

        }else if (v.getId() == R.id.tvComments){

        }else if (v.getId() == R.id.tvJaime){

        }else if (v.getId() == R.id.tvFavorite){

        }else if (v.getId() == R.id.tvPartage){

        }else if (v.getId() == R.id.tvSignalement){

        }else if (v.getId() == R.id.tvGestion){

        }else if (v.getId() == R.id.tvNotifications){

        }else if (v.getId() == R.id.tvShowGallery){

        }else if (v.getId() == R.id.tvSignaler){

        }else if (v.getId() == R.id.tvStatistiques){

        }else if (v.getId() == R.id.tvSupprimer){

        }
    }
}