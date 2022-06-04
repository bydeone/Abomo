package com.deone.abomo.adapters;

import static com.deone.abomo.outils.MethodTools.formatHeureJourAn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.deone.abomo.R;
import com.deone.abomo.models.Note;
import com.deone.abomo.models.Post;
import com.deone.abomo.outils.Alistener;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.MyHolder> {

    private final Context context;
    private Alistener listener;
    private final List<Note> noteList;


    public AdapterNote(Context context, List<Note> noteList) {
            this.context = context;
            this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String cover = noteList.get(position).getUavatar();
        String nom = noteList.get(position).getUnoms();
        String note = noteList.get(position).getNnote();

        Glide.with(context)
                .load(cover)
                .placeholder(R.drawable.lion)
                .error(R.drawable.ic_action_person)
                .centerCrop()
                .into(holder.ivCover);

        holder.tvUserNoms.setText(String.format("%s", nom));
        holder.tvNotes.setText(String.format("%s", note));
    }

    @Override
    public int getItemCount() {
            return noteList.size();
            }


    public void setListener(Alistener listener) {
            this.listener = listener;
            }


    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {

        ImageView ivCover;
        TextView tvUserNoms;
        TextView tvNotes;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvUserNoms = itemView.findViewById(R.id.tvUserNoms);
            tvNotes = itemView.findViewById(R.id.tvNotes);
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
