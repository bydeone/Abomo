package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.COMMENTS;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.IMAGES;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.dataForActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.deone.abomo.adapters.AdapterCommentaire;
import com.deone.abomo.adapters.AdapterGallerie;
import com.deone.abomo.models.Commentaire;
import com.deone.abomo.models.Image;
import com.deone.abomo.models.Post;
import com.deone.abomo.models.User;
import com.deone.abomo.outils.Alistener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DatabaseReference reference;
    private User user;
    private Post post;
    private String myuid;
    private String pid;
    private RecyclerView rvGallery;
    private List<Image> imageList;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private final ValueEventListener valuser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                user = ds.getValue(User.class);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private final ValueEventListener valimages = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            imageList.clear();
            for (DataSnapshot ds : snapshot.getChildren()) {
                Image image = ds.getValue(Image.class);
                imageList.add(image);
                AdapterGallerie adapterGallerie = new AdapterGallerie(GalleryActivity.this, imageList);
                rvGallery.setAdapter(adapterGallerie);
                adapterGallerie.setListener(new Alistener() {
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

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fuser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            myuid = fuser.getUid();
            pid = dataForActivity(this, "pid");
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
            reference.child(POSTS).child(pid).child(IMAGES).addValueEventListener(valimages);
            Query query = reference.child(USERS).orderByKey().equalTo(myuid);
            query.addValueEventListener(valuser);
            Query pQuery = reference.child(POSTS).orderByKey().equalTo(pid);
            pQuery.addValueEventListener(valpost);
        }
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvGallery = findViewById(R.id.rvGallery);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGallery.setLayoutManager(layoutManager);
        imageList = new ArrayList<>();
        findViewById(R.id.fabAddImage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fabAddImage) {
            Intent intent = new Intent(this, AddImageActivity.class);
            intent.putExtra("pid", pid);
            startActivity(intent);
        }
    }
}