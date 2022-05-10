package com.deone.abomo;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.POSTS;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deone.abomo.adapters.AdapterPost;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String myuid;
    private String mysearch;
    private DatabaseReference reference;
    private RecyclerView rvPosts;
    private List<Post> postList;
    private final ValueEventListener valImmeubles = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            postList.clear();
            for (DataSnapshot ds : snapshot.getChildren()){
                Post post = ds.getValue(Post.class);
                postList.add(post);
                AdapterPost adapterPost = new AdapterPost(postList);
                rvPosts.setAdapter(adapterPost);
                adapterPost.setListener(new Alistener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /*Intent intent = new Intent(HomeActivity.this, ShowPostActivity.class);
                        intent.putExtra("pid", postList.get(position).getPid());
                        intent.putExtra("uid", postList.get(position).getUid());
                        intent.putExtra("noms", postList.get(position).getUnoms());
                        intent.putExtra("avatar", postList.get(position).getUavatar());
                        startActivity(intent);*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        /*new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.app_name))
                                .setMessage(getString(R.string.sign_out))
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes,
                                        (dialog, which) -> deconnecter(this))
                                .create().show();*/
                    }
                });
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private final ValueEventListener valSearchs = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            postList.clear();
            for (DataSnapshot ds : snapshot.getChildren()){
                Post post = ds.getValue(Post.class);
                assert post != null;
                if (post.getPtitre().toLowerCase().contains(mysearch.toLowerCase()) ||
                        post.getPdescription().toLowerCase().contains(mysearch.toLowerCase())){
                    postList.add(post);
                    AdapterPost adapterPost = new AdapterPost(postList);
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
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSystemPreference();
        setContentView(R.layout.activity_main);
        checkuser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem  mSearch = menu.findItem(R.id.rechercher);
        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.rechercher));
        manageSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void manageSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)){
                    mysearch = query;
                    reference.child(POSTS).addValueEventListener(valSearchs);
                }else {
                    reference.child(POSTS).addValueEventListener(valImmeubles);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mysearch = newText;
                    reference.child(POSTS).addValueEventListener(valSearchs);
                }else {
                    reference.child(POSTS).addValueEventListener(valImmeubles);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.parametres){
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("uid", myuid);
            startActivity(intent);
        }else if (item.getItemId() == R.id.apropos){

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSystemPreference() {

    }

    private void checkuser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            myuid = fuser.getUid();
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
            initviews();
        }else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    private void initviews() {
        rvPosts = findViewById(R.id.rvPosts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPosts.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        reference.child(POSTS).addValueEventListener(valImmeubles);
        findViewById(R.id.fabAddImmeuble).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddImmeuble){
            Intent intent =new Intent(this, AddPostActivity.class);
            //intent.
            startActivity(intent);
        }
    }
}