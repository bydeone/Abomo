package com.deone.abomo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.deone.abomo.R;
import com.google.firebase.database.DatabaseReference;

public class CommentFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatabaseReference reference;
    private Toolbar toolbar;
    private String mParam1;
    private String mParam2;

    public CommentFrag() {
        // Required empty public constructor
    }

    /*public static CommentFrag newInstance(String param1, String param2) {
        CommentFrag fragment = new CommentFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(getString(R.string.post_comments));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.frag_commentaire, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.menu_add_contact) {
            T.showToastBro(getActivity(), item.getTitle().toString());
        }
        if (id == R.id.menu_nearby_businesses) {
            T.showToastBro(getActivity(), item.getTitle().toString());
        }*/
        return super.onOptionsItemSelected(item);
    }
}