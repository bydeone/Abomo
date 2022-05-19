package com.deone.abomo.fragments;

import static com.deone.abomo.outils.ConstantsTools.DATABASE;
import static com.deone.abomo.outils.ConstantsTools.FAVORITES;
import static com.deone.abomo.outils.ConstantsTools.LIKES;
import static com.deone.abomo.outils.ConstantsTools.POSTS;
import static com.deone.abomo.outils.ConstantsTools.SIGNALES;
import static com.deone.abomo.outils.ConstantsTools.USERS;
import static com.deone.abomo.outils.MethodTools.formatDetailsPost;
import static com.deone.abomo.outils.MethodTools.supprimerUnPost;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.deone.abomo.R;
import com.deone.abomo.models.Favorite;
import com.deone.abomo.models.Like;
import com.deone.abomo.models.Post;
import com.deone.abomo.models.Signale;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PostFrag extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String PID = "pid";
    private static final String UID = "uid";
    private static final String MYUID = "myuid";
    private Post post;
    private DatabaseReference reference;
    private Toolbar toolbar;
    private String myuid;
    private String pid;
    private String uid;
    private boolean likeProcess = false;
    private boolean favoriteProcess = false;
    private boolean signaleProcess = false;
    private ImageView ivCover;
    private TextView tvPost;
    private TextView tvSupprimer;
    private TextView tvSignaler;
    private TextView tvJaime;
    private TextView tvFavorite;
    private TextView tvPartage;
    private TextView tvSignalement;
    private final ValueEventListener valpost = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                post = ds.getValue(Post.class);
                assert post != null;
                tvPost.setText(HtmlCompat.fromHtml(formatDetailsPost(getActivity(),
                        ""+post.getPtitre(), ""+post.getPdescription(),
                        ""+post.getPdate()), 0));
                Picasso.get().load(post.getPcover()).placeholder(R.drawable.ic_action_add_image).into(ivCover);
                initializeLikes(ds);
                /*initializeFavorites(ds);
                initializeSignales(ds);
                initializeShares(ds);*/
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void initializeLikes(DataSnapshot ds) {
        tvJaime.setText(post.getPnlikes());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Like like = dsnapshot.getValue(Like.class);
            assert like != null;
            if (like.getLid().equals(myuid)){
                likeProcess = true;
                tvJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_jaime, 0);
            }else {
                likeProcess = false;
                tvJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unjaime, 0);
            }
        }
    }

    private void initializeFavorites(DataSnapshot ds) {
        tvFavorite.setText(post.getPnfavorites());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Favorite favorite = dsnapshot.getValue(Favorite.class);
            assert favorite != null;
            if (favorite.getFid().equals(myuid)){
                favoriteProcess = true;
                tvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
            }
        }
    }

    private void initializeSignales(DataSnapshot ds) {
        tvSignalement.setText(post.getPnsignales());
        for (DataSnapshot dsnapshot : ds.child(LIKES).getChildren()){
            Signale signale = dsnapshot.getValue(Signale.class);
            assert signale != null;
            if (signale.getSid().equals(myuid)){
                signaleProcess = true;
                tvSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
            }
        }
    }

    private void initializeShares(DataSnapshot ds) {
        tvPartage.setText(post.getPnshares());
    }

    public PostFrag() {
        // Required empty public constructor
    }

    /*public static PostFrag newInstance(String param1, String param2) {
        PostFrag fragment = new PostFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            pid = getArguments().getString(PID);
            uid = getArguments().getString(UID);
            myuid = getArguments().getString(MYUID);
            reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(getString(R.string.post_infos));
        ivCover = view.findViewById(R.id.ivCover);
        tvJaime = view.findViewById(R.id.tvJaime);
        tvFavorite = view.findViewById(R.id.tvFavorite);
        tvPartage = view.findViewById(R.id.tvPartage);
        tvSignalement = view.findViewById(R.id.tvSignalement);
        tvPost = view.findViewById(R.id.tvPost);
        tvSupprimer = view.findViewById(R.id.tvSupprimer);
        tvSupprimer.setVisibility(myuid.equals(uid)?View.VISIBLE:View.GONE);
        tvSignaler = view.findViewById(R.id.tvSignaler);
        tvSignaler.setVisibility(myuid.equals(uid)?View.GONE:View.VISIBLE);
        Query query = reference.child(POSTS).orderByKey().equalTo(pid);
        query.addValueEventListener(valpost);
        tvSupprimer.setOnClickListener(this);
        tvSignaler.setOnClickListener(this);
        tvJaime.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
        tvPartage.setOnClickListener(this);
        tvSignalement.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.frag_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.menu_add_contact) {
            T.showToastBro(requireActivity(), item.getTitle().toString());
        }
        if (id == R.id.menu_nearby_businesses) {
            T.showToastBro(requireActivity(), item.getTitle().toString());
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvSupprimer && myuid.equals(uid)){
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.del_post))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, which) -> supprimerUnPost(requireActivity(), reference, ""+pid))
                    .create().show();
        } else if (id == R.id.tvSignaler && !myuid.equals(uid)){
            final Dialog dialog = new Dialog(getActivity());
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
        } else if (id == R.id.tvJaime){
             aimerUnPost();
        } else if (id == R.id.tvFavorite){
            favoriteUnPost();
        } else if (id == R.id.tvPartage){
            partagerUnPost();
        } else if (id == R.id.tvSignalement){
            signalerUnPost();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.swShowGallery){

        }else if (id == R.id.swNotifComment){

        }
    }

    private void aimerUnPost() {
        final DatabaseReference likesRef = reference.child(POSTS).child(pid).child(LIKES);
        final DatabaseReference postsRef = reference.child(POSTS);
        if(likeProcess){
            postsRef.child(pid).child("pnlikes").setValue(""+ (Integer.parseInt(post.getPnlikes()) - 1));
            likesRef.child(myuid).removeValue();
            tvJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unjaime, 0);
            Toast.makeText(getActivity(), "Je n'aime pas cette publication", Toast.LENGTH_SHORT).show();
            likeProcess = false;
        }else {
            postsRef.child(pid).child("pnlikes").setValue(""+ (Integer.parseInt(post.getPnlikes()) + 1));
            String timestamp = String.valueOf(System.currentTimeMillis());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("lid", myuid);
            hashMap.put("ldate", timestamp);
            likesRef.child(myuid).setValue(hashMap);
            tvJaime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_jaime, 0);
            Toast.makeText(getActivity(), "J'aime cette publication", Toast.LENGTH_SHORT).show();
            likeProcess = true;
        }
    }

    public static void partagerUnPost() {
        /*final DatabaseReference likesRef = reference.child(POSTS).child(pid).child(SHARES);
        final DatabaseReference postsRef = reference.child(POSTS);
        post.getPnshares()*/
    }

    public  void favoriteUnPost() {
        if (favoriteProcess){
            reference.child(POSTS).child(pid).child("pnfavorites").setValue(""+ (Integer.parseInt(post.getPnfavorites()) - 1));
            reference.child(POSTS).child(pid).child(FAVORITES).child(uid).removeValue();
            reference.child(USERS).child(uid).child(FAVORITES).child(pid).removeValue();
            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unfavorite, 0);
            Toast.makeText(getActivity(), "Publication non favorite", Toast.LENGTH_SHORT).show();
            favoriteProcess = false;
        }else {
            reference.child(POSTS).child(pid).child("pnfavorites").setValue(""+ (Integer.parseInt(post.getPnfavorites()) + 1));
            String timestamp = String.valueOf(System.currentTimeMillis());
            HashMap<String, String> hashMapPost = new HashMap<>();
            hashMapPost.put("fid", uid);
            hashMapPost.put("fdate", timestamp);
            reference.child(POSTS).child(pid).child(FAVORITES).child(uid).setValue(hashMapPost);
            HashMap<String, String> hashMapUser = new HashMap<>();
            hashMapUser.put("fid", pid);
            hashMapUser.put("fdate", timestamp);
            reference.child(USERS).child(uid).child(FAVORITES).child(pid).setValue(hashMapUser);
            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_favorite, 0);
            Toast.makeText(getActivity(), "Publication favorite", Toast.LENGTH_SHORT).show();
            favoriteProcess = true;
        }
    }

    public void signalerUnPost() {
        if (signaleProcess){
            reference.child(POSTS).child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnsignales()) - 1));
            reference.child(POSTS).child(pid).child(SIGNALES).child(uid).removeValue();
            tvSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_unsignaler, 0);
            Toast.makeText(getActivity(), "Publication non signalée", Toast.LENGTH_SHORT).show();
            signaleProcess = false;
        }else {
            reference.child(POSTS).child(pid).child("pnsignales").setValue(""+ (Integer.parseInt(post.getPnsignales()) + 1));
            String timestamp = String.valueOf(System.currentTimeMillis());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("lid", uid);
            hashMap.put("ldate", timestamp);
            reference.child(POSTS).child(pid).child(SIGNALES).child(uid).setValue(hashMap);
            tvSignalement.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_signaler, 0);
            Toast.makeText(getActivity(), "Publication signalée", Toast.LENGTH_SHORT).show();
            signaleProcess = true;
        }
    }

}