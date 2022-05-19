package com.deone.abomo;

import static com.deone.abomo.outils.MethodTools.dataForActivity;
import static com.deone.abomo.outils.MethodTools.loadSystemPreference;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.deone.abomo.fragments.CommentFrag;
import com.deone.abomo.fragments.GalleryFrag;
import com.deone.abomo.fragments.PostFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShowPostActivity extends AppCompatActivity {

    private String myuid;
    private String pid;
    private String uid;
    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.menu_view_post){
                Bundle bundle1 = new Bundle();
                bundle1.putString("pid", pid );
                bundle1.putString("uid", uid );
                bundle1.putString("myuid", myuid );
                PostFrag postFragment = new PostFrag();
                postFragment.setArguments(bundle1);
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.flContent, postFragment, "");
                fragmentTransaction1.commit();
                return true;
            }else if (item.getItemId() == R.id.menu_view_gallery){
                Bundle bundle2 = new Bundle();
                bundle2.putString("pid", pid );
                bundle2.putString("uid", uid );
                bundle2.putString("myuid", myuid );
                GalleryFrag galleryFragment = new GalleryFrag();
                galleryFragment.setArguments(bundle2);
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.flContent, galleryFragment, "");
                fragmentTransaction2.commit();
                return true;
            }else if (item.getItemId() == R.id.menu_view_comments){
                Bundle bundle3 = new Bundle();
                bundle3.putString("pid", pid );
                bundle3.putString("uid", uid );
                bundle3.putString("myuid", myuid );
                CommentFrag commentFragment = new CommentFrag();
                commentFragment.setArguments(bundle3);
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.flContent, commentFragment, "");
                fragmentTransaction3.commit();
            }
            return false;
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
            initviews();
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initviews() {
        BottomNavigationView bnv = findViewById(R.id.bnPost);
        bnv.setOnNavigationItemSelectedListener(selectedListener);
        Bundle bundle1 = new Bundle();
        bundle1.putString("pid", pid );
        bundle1.putString("uid", uid );
        bundle1.putString("myuid", myuid );
        PostFrag postFragment = new PostFrag();
        postFragment.setArguments(bundle1);
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.flContent, postFragment, "");
        fragmentTransaction1.commit();
    }
}