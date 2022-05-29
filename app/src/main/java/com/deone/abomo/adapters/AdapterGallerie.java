package com.deone.abomo.adapters;

import static com.deone.abomo.outils.MethodTools.formatHeureJourAn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deone.abomo.R;
import com.deone.abomo.models.Image;
import com.deone.abomo.models.Post;
import com.deone.abomo.outils.Alistener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterGallerie extends RecyclerView.Adapter<AdapterGallerie.MyHolder> {

    private final Context context;
    private Alistener listener;
    private final List<Image> imageList;


    public AdapterGallerie(Context context, List<Image> imageList) {
            this.context = context;
            this.imageList = imageList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
            return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String cover = imageList.get(position).getIcover();
        String titre = imageList.get(position).getItitre();
        String description = imageList.get(position).getIdescription();
        String date = imageList.get(position).getIdate();

        Picasso.get().load(cover).placeholder(R.drawable.lion).into(holder.ivGallery);
        holder.tvTitre.setText(titre);
        holder.tvDescription.setText(description);
        holder.tvDate.setText(formatHeureJourAn(""+date));

    }

    @Override
    public int getItemCount() {
            return imageList.size();
            }


    public void setListener(Alistener listener) {
            this.listener = listener;
            }


    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {

        ImageView ivGallery;
        TextView tvTitre;
        TextView tvDescription;
        TextView tvDate;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivGallery = itemView.findViewById(R.id.ivGallery);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDescription = itemView.findViewById(R.id.tvDescription);
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
