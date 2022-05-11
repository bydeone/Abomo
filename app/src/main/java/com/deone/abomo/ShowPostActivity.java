package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.FORMAT_DATE_FULL_FR;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.MethodTools.aimerUnPost;
import static com.deone.abomo.outils.MethodTools.customTitre;
import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.deconnecter;
import static com.deone.abomo.outils.MethodTools.favoriteUnPost;
import static com.deone.abomo.outils.MethodTools.loadSystemPreference;
import static com.deone.abomo.outils.MethodTools.partagerUnPost;
import static com.deone.abomo.outils.MethodTools.signalerUnPost;
import static com.deone.abomo.outils.MethodTools.supprimerUnPost;
import static com.deone.abomo.outils.MethodTools.timestampToString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
    private ImageView ivAvatar;
    private ImageView ivCover;
    private TextView tvNoms;
    private TextView tvVues;
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
    private TextView tvNotes;
    private TextView tvSignaler;
    private TextView tvSupprimer;
    private TextView tvComparaison;
    private TextView tvEstimation;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
                assert post != null;
                tvDetails.setText(HtmlCompat.fromHtml(customTitre(ShowPostActivity.this,
                        ""+post.getPtitre(),
                        ""+post.getPdescription(),
                        ""+post.getPdate(),
                        ""+post.getPnvues()), 0));
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

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            pid = dataForActivity(this, "pid");
            uid = dataForActivity(this, "uid");
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivCover = findViewById(R.id.ivCover);
        tvNoms = findViewById(R.id.tvNoms);
        tvVues = findViewById(R.id.tvVues);
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
        tvComparaison = findViewById(R.id.tvComparaison);
        tvEstimation = findViewById(R.id.tvEstimation);
        tvNotes = findViewById(R.id.tvNotes);
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
            aimerUnPost(this, myuid, tvJaime, reference, ""+pid, ""+post.getPnlikes());
        }else if (v.getId() == R.id.tvFavorite){
            favoriteUnPost(this, reference, ""+pid, ""+post.getPnfavorites());
        }else if (v.getId() == R.id.tvPartage){
            partagerUnPost(this, reference, ""+pid, ""+post.getPnshares());
        }else if (v.getId() == R.id.tvSignalement){
            signalerUnPost(this, reference, ""+pid, ""+post.getPnsignales());
        }else if (v.getId() == R.id.tvGestion){

        }else if (v.getId() == R.id.tvNotifications){

        }else if (v.getId() == R.id.tvShowGallery){

        }else if (v.getId() == R.id.tvSignaler){

        }else if (v.getId() == R.id.tvStatistiques){

        }else if (v.getId() == R.id.tvSupprimer){
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.delete_post))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, which) -> supprimerUnPost(this, reference, pid))
                    .create().show();
        }
    }
}