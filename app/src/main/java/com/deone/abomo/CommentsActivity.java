package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.COMMENTS;
import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.appPreferences;
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
import android.widget.Toast;

import com.deone.abomo.adapters.AdapterCommentaire;
import com.deone.abomo.adapters.AdapterPost;
import com.deone.abomo.models.Commentaire;
import com.deone.abomo.models.Post;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private User user;
    private Post post;
    private String myuid;
    private String pid;
    private Toolbar toolbar;
    private RecyclerView rvComments;
    private EditText edtvComment;
    private List<Commentaire> commentaireList;
    private final ValueEventListener valcomments = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            commentaireList.clear();
            for (DataSnapshot ds : snapshot.getChildren()){
                Commentaire commentaire = ds.getValue(Commentaire.class);
                commentaireList.add(commentaire);
                AdapterCommentaire adapterCommentaire = new AdapterCommentaire(CommentsActivity.this, commentaireList);
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
            Toast.makeText(CommentsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(CommentsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences(this);
        setContentView(R.layout.activity_comments);
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
            reference.child(POSTS).child(pid).child(COMMENTS).addValueEventListener(valcomments);
            Query query = reference.child(USERS).orderByKey().equalTo(myuid);
            query.addValueEventListener(valuser);
            Query pQuery = reference.child(POSTS).orderByKey().equalTo(pid);
            pQuery.addValueEventListener(valpost);
        }
    }

    private void initviews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvComments = findViewById(R.id.rvComments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvComments.setLayoutManager(layoutManager);
        commentaireList = new ArrayList<>();
        edtvComment = findViewById(R.id.edtvComment);
        findViewById(R.id.ibComment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ibComment) {
            verificationCommentaire();
        }
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
        reference.child(POSTS).child(pid)
                .child(COMMENTS).child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    reference.child(POSTS).child(pid).child("pncommentaires").setValue(""+ (Integer.parseInt(post.getPnlikes()) + 1))
                            .addOnSuccessListener(unused1 -> {
                                Toast.makeText(CommentsActivity.this, ""+getString(R.string.success_comment), Toast.LENGTH_SHORT).show();
                                edtvComment.setText(null);
                                edtvComment.setHint(getString(R.string.votre_commentaire));
                            })
                            .addOnFailureListener(e -> Toast.makeText(CommentsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(CommentsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}