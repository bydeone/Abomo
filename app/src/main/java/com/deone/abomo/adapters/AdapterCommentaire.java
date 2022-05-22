package com.deone.abomo.adapters;

import static com.deone.abomo.outils.MethodTools.formatHeureJourAn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deone.abomo.R;
import com.deone.abomo.models.Commentaire;
import com.deone.abomo.outils.Alistener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCommentaire extends RecyclerView.Adapter<AdapterCommentaire.MyHolder> implements View.OnClickListener {

    private final Context context;
    private Alistener listener;
    private final List<Commentaire> commentaireList;
    private String myuid;

    public AdapterCommentaire(Context context, List<Commentaire> commentaireList) {
            this.context = context;
            this.commentaireList = commentaireList;
            this.myuid = FirebaseAuth.getInstance().getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String avatar = commentaireList.get(position).getUavatar();
        String message = commentaireList.get(position).getCmessage();
        String vues = commentaireList.get(position).getCnvues();
        String jaimes = commentaireList.get(position).getCnlikes();
        String commentaires = commentaireList.get(position).getCncommentaires();
        String partages = commentaireList.get(position).getCnpartage();
        String signales = commentaireList.get(position).getCnsignalement();
        String nom = commentaireList.get(position).getUnoms();
        String date = commentaireList.get(position).getCdate();
        String uid = commentaireList.get(position).getUid();

        holder.ivAvatar.setVisibility(this.myuid.equals(uid) ? View.GONE:View.VISIBLE);
        Picasso.get().load(avatar).placeholder(R.drawable.lion).into(holder.ivAvatar);

        holder.tvMessage.setText(HtmlCompat.fromHtml(message, 0));
        holder.tvLikes.setText(jaimes);
        holder.tvComments.setText(commentaires);
        holder.tvNoms.setText(this.myuid.equals(uid) ? context.getString(R.string.vous):nom);
        holder.tvSignales.setText(signales);
        holder.tvDate.setText(formatHeureJourAn(""+date));

        holder.tvComments.setOnClickListener(this);
        holder.tvLikes.setOnClickListener(this);
        holder.tvSignales.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
            return commentaireList.size();
            }


    public void setListener(Alistener listener) {
            this.listener = listener;
            }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvComments) {

        } else if (id == R.id.tvLikes) {

        }  else if (id == R.id.tvSignales) {

        }
    }


    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {

        ImageView ivAvatar;
        TextView tvNoms;
        TextView tvMessage;
        TextView tvDate;
        TextView tvComments;
        TextView tvLikes;
        TextView tvSignales;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvNoms = itemView.findViewById(R.id.tvNoms);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvComments = itemView.findViewById(R.id.tvComments);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvSignales = itemView.findViewById(R.id.tvSignales);
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
