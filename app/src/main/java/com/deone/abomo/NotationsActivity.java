package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.MethodTools.dataForActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.deone.abomo.adapters.AdapterNote;
import com.deone.abomo.adapters.AdapterPost;
import com.deone.abomo.models.Note;
import com.deone.abomo.models.Post;
import com.deone.abomo.outils.Alistener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotationsActivity extends AppCompatActivity {

    private String myuid;
    private String pid;
    private DatabaseReference reference;
    private RecyclerView rvNotes;
    private List<Note> noteList;
    private final ValueEventListener valNotes = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            noteList.clear();
            for (DataSnapshot ds : snapshot.getChildren()){
                Note note = ds.getValue(Note.class);
                noteList.add(note);
                AdapterNote adapterNote = new AdapterNote(NotationsActivity.this, noteList);
                rvNotes.setAdapter(adapterNote);
                adapterNote.setListener(new Alistener() {
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
            Toast.makeText(NotationsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notations);
        checkuser();
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            pid = dataForActivity(this, "pid");
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
            reference.child(POSTS).child(pid).child("Notes").addValueEventListener(valNotes);
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvNotes = findViewById(R.id.rvNotes);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvNotes.setLayoutManager(gridLayoutManager);
        noteList = new ArrayList<>();
    }
}