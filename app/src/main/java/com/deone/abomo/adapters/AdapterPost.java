package com.deone.abomo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deone.abomo.R;
import com.deone.abomo.models.Post;
import com.deone.abomo.outils.Alistener;

import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder> {

    private Alistener listener;
    private final List<Post> postList;


    public AdapterPost(List<Post> postList) {
            this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_immeuble, parent, false);
            return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            String cover = postList.get(position).getPcover();
            String titre = postList.get(position).getPtitre();
            String description = postList.get(position).getPdescription();

            holder.tvTitre.setText(HtmlCompat.fromHtml(titre, 0));
            holder.tvDescription.setText(HtmlCompat.fromHtml(description, 0));

    }

    @Override
    public int getItemCount() {
            return postList.size();
            }


    public void setListener(Alistener listener) {
            this.listener = listener;
            }


    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {

        ImageView ivCover;
        TextView tvTitre;
        TextView tvDescription;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(v, position);
            }
        }


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onLongItemClick(v, position);
            }
            return true;
        }
    }
}
