package com.deone.abomo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deone.abomo.R;
import com.deone.abomo.models.Loyer;
import com.deone.abomo.outils.Alistener;

import java.util.List;

    public class AdapterLoyer extends RecyclerView.Adapter<AdapterLoyer.MyHolder> {

    private Context context;
    private Alistener listener;
    private final List<Loyer> loyerList;


    public AdapterLoyer(Context context, List<Loyer> loyerList) {
            this.context = context;
            this.loyerList = loyerList;
            }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_loyer, parent, false);
            return new MyHolder(view);
            }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            /*String titre = loyerList.get(position).getCtitre();
            String nombre = loyerList.get(position).getCnombre();

            holder.tvTitre.setText(HtmlCompat.fromHtml(titre, 0));
            holder.tvNombre.setText(HtmlCompat.fromHtml(nombre, 0));*/

            }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
            return loyerList.size();
            }

    /**
     *
     * @param listener
     */
    public void setListener(Alistener listener) {
            this.listener = listener;
            }

    /**
     *
     */
    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnLongClickListener {

        TextView tvTitre;
        TextView tvNombre;

        /**
         *
         * @param itemView
         */
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //tvTitre = itemView.findViewById(R.id.tvTitre);
            //tvNombre = itemView.findViewById(R.id.tvNombre);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /**
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(v, position);
            }
        }

        /**
         *
         * @param v
         * @return
         */
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
