package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.appPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deone.abomo.adapters.AdapterPost;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String myuid;
    private String mysearch;
    private DatabaseReference reference;
    private RecyclerView rvPosts;
    private List<Post> postList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences(this);
        setContentView(R.layout.activity_main);
        checkuser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem  mSearch = menu.findItem(R.id.rechercher);
        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.rechercher));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lancerLaRecherche(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lancerLaRecherche(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.parametres){
            ouvrirLesParametres();
        }else if (item.getItemId() == R.id.apropos){
            ouvrirApropos();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddPost){
            ajouterUnPost();
        } else if (v.getId() == R.id.fabSearchPost){
            rechercherUnPost();
        }
    }

    private void lancerLaRecherche(String query) {
        if (!TextUtils.isEmpty(query)){
            reference.child(POSTS).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Post post = ds.getValue(Post.class);
                        assert post != null;
                        if (post.getPtitre().toLowerCase().contains(query.toLowerCase()) ||
                                post.getPdescription().toLowerCase().contains(query.toLowerCase())){
                            postList.add(post);
                            afficherLesPublications();
                        }
                    }
                    if (postList.size() == 0){
                        showNoPostMessage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            reference.child(POSTS).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Post post = ds.getValue(Post.class);
                        postList.add(post);
                        afficherLesPublications();
                    }
                    if (postList.size() == 0){
                        showNoPostMessage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showNoPostMessage() {

    }

    private void afficherLesPublications() {
        AdapterPost adapterPost = new AdapterPost(getApplicationContext(), postList);
        rvPosts.setAdapter(adapterPost);
        adapterPost.setListener(new Alistener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, ShowPostActivity.class);
                intent.putExtra("pid", postList.get(position).getPid());
                intent.putExtra("uid", postList.get(position).getUid());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Show details dialog view
            }
        });
    }

    private void ouvrirApropos() {

    }

    private void ouvrirLesParametres() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("uid", myuid);
        startActivity(intent);
    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
            reference.child(POSTS).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Post post = ds.getValue(Post.class);
                        postList.add(post);
                        AdapterPost adapterPost = new AdapterPost(getApplicationContext(), postList);
                        rvPosts.setAdapter(adapterPost);
                        adapterPost.setListener(new Alistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(MainActivity.this, ShowPostActivity.class);
                                intent.putExtra("pid", postList.get(position).getPid());
                                intent.putExtra("uid", postList.get(position).getUid());
                                intent.putExtra("noms", postList.get(position).getUnoms());
                                intent.putExtra("avatar", postList.get(position).getUavatar());
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Query query = reference.child(USERS).orderByKey().equalTo(myuid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        user = ds.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    private void initviews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvPosts = findViewById(R.id.rvPosts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPosts.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        findViewById(R.id.fabAddPost).setOnClickListener(this);
        findViewById(R.id.fabSearchPost).setOnClickListener(this);
    }

    private void rechercherUnPost() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    private void ajouterUnPost() {
        Intent intent =new Intent(this, AddPostActivity.class);
        intent.putExtra("userName", user.getUnoms());
        intent.putExtra("userAvatar", user.getUavatar());
        startActivity(intent);
    }

    private void showCreateSearchDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.search_ask, mysearch))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        (dialog, which) -> {
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.putExtra("mysearch", mysearch);
                    startActivity(intent);
                })
                .create().show();
    }

}