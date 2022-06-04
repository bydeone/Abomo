package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.COMMENTS;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.supprimerUnPost;
import static com.deone.abomo.outils.MethodTools.timestampToString;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

public class PostActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private DatabaseReference reference;
    private Toolbar toolbar;
    private String myuid;
    private String pid;
    private String uid;
    private Post post;
    private ImageView ivAvatar;
    private TextView tvPostTitre;
    private TextView tvPostVues;
    private TextView tvRapidGallery;
    private TextView tvRapidWork;
    private TextView tvRapidJaime;
    private TextView tvRapidFavorite;
    private TextView tvRapidSignalement;
    private TextView tvRapidNote;
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
                tvRapidNote.setText(post.getPnnotes());
                tvPostUsername.setText(post.getUnoms());
                tvPostDescription.setText(post.getPdescription());
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
        //
        ivAvatar = findViewById(R.id.ivAvatar);
        //
        tvPostTitre = findViewById(R.id.tvPostTitre);
        //
        tvPostVues = findViewById(R.id.tvPostVues);
        //
        tvRapidGallery = findViewById(R.id.tvRapidGallery);
        //
        tvRapidWork = findViewById(R.id.tvRapidWork);
        //
        tvRapidJaime = findViewById(R.id.tvRapidJaime);
        //
        tvRapidFavorite = findViewById(R.id.tvRapidFavorite);
        //
        tvRapidSignalement = findViewById(R.id.tvRapidSignalement);
        //
        tvRapidNote = findViewById(R.id.tvRapidNote);
        //
        tvPostUsername = findViewById(R.id.tvPostUsername);
        //
        tvPostDescription = findViewById(R.id.tvPostDescription);
        //
        swCommentaires = findViewById(R.id.swCommentaires);
        //
        swNotifications = findViewById(R.id.swNotifications);
        //
        tvVisibility = findViewById(R.id.tvVisibility);
        //
        tvNotations = findViewById(R.id.tvNotations);
        // Accès à l'interface de comparaison de la publication, pour le visiteur, avec des publications similaires
        tvComparaison = findViewById(R.id.tvComparaison);
        //tvComparaison.setVisibility(myuid.equals(uid)? View.GONE:View.VISIBLE);
        // Accès à l'interface d'estimation de la publication par les experts (cette notion est à bien définir)
        tvEstimation = findViewById(R.id.tvEstimation);
        //tvEstimation.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        // Accès à l'interface de gestion immobilière reservé au proprietaire de la publication
        tvGestionImmobiliere = findViewById(R.id.tvGestionImmobiliere);
        //tvGestionImmobiliere.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        // Demander des informations liées à la publication
        tvInformations = findViewById(R.id.tvInformations);
        //tvInformations.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);

        // Suppression de la publication
        tvSupprimer = findViewById(R.id.tvSupprimer);
        tvSupprimer.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        // Signalement de la publication
        tvSignaler = findViewById(R.id.tvSignaler);
        tvSignaler.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);

        tvRapidGallery.setOnClickListener(this);
        tvRapidNote.setOnClickListener(this);
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

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            pid = dataForActivity(this, "pid");
            uid = dataForActivity(this, "uid");
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
                            (dialog, which) -> supprimerUnPost(PostActivity.this, reference, ""+pid))
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
        } else if (id == R.id.tvRapidGallery){
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra("pid", pid);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } else if (id == R.id.tvComparaison){
            Intent intent = new Intent(this, ComparaisonActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        } else if (id == R.id.tvEstimation){
            Intent intent = new Intent(this, EstimationActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        } else if (id == R.id.tvGestionImmobiliere){
            Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        } else if (id == R.id.tvInformations){
            /*Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);*/
        } else if (id == R.id.tvVisibility){
            /*Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);*/
        } else if (id == R.id.tvNotations){
            /*Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);*/
        } else if (id == R.id.tvRapidNote){
            /*Intent intent = new Intent(this, GestionImmobiliereActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);*/
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.swCommentaires){

        } else if (id == R.id.swNotifications){

        }
    }
}