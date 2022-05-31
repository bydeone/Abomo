package com.deone.abomo.adapters;

import static com.deone.abomo.outils.MethodTools.formatHeureJourAn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deone.abomo.R;
import com.deone.abomo.models.Post;
import com.deone.abomo.outils.Alistener;

import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder> {

    private final Context context;
    private Alistener listener;
    private final List<Post> postList;


    public AdapterPost(Context context, List<Post> postList) {
            this.context = context;
            this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String avatar = postList.get(position).getUavatar();
        String cover = postList.get(position).getPcover();
        String titre = postList.get(position).getPtitre();
        String description = postList.get(position).getPdescription();
        String vues = postList.get(position).getPnvues();
        String jaimes = postList.get(position).getPnlikes();
        String commentaires = postList.get(position).getPncommentaires();
        String nom = postList.get(position).getUnoms();
        String date = postList.get(position).getPdate();

        Glide.with(context)
                .load(avatar)
                .placeholder(R.drawable.lion)
                .error(R.drawable.ic_action_person)
                .centerCrop()
                .into(holder.ivAvatar);

        Glide.with(context)
                .load(cover)
                .placeholder(R.drawable.lion)
                .error(R.drawable.ic_action_person)
                .centerCrop()
                .into(holder.ivCover);

        holder.tvTitre.setText(HtmlCompat.fromHtml(titre, 0));
        holder.tvDescription.setText(HtmlCompat.fromHtml(description, 0));
        holder.tvVues.setText(context.getString(R.string.nbre_vues, vues));
        holder.tvJaime.setText(context.getString(R.string.nbre_jaimes, jaimes));
        holder.tvComments.setText(context.getString(R.string.nbre_commentaires, commentaires));
        holder.tvNoms.setText(nom);
        holder.tvDate.setText(formatHeureJourAn(""+date));

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
        ImageView ivAvatar;
        TextView tvTitre;
        TextView tvDescription;
        TextView tvVues;
        TextView tvJaime;
        TextView tvComments;
        TextView tvNoms;
        TextView tvDate;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvVues = itemView.findViewById(R.id.tvVues);
            tvJaime = itemView.findViewById(R.id.tvJaime);
            tvComments = itemView.findViewById(R.id.tvComments);
            tvNoms = itemView.findViewById(R.id.tvNoms);
            tvDate = itemView.findViewById(R.id.tvDate);
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
