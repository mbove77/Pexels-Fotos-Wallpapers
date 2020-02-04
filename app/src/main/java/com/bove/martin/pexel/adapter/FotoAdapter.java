package com.bove.martin.pexel.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bove.martin.pexel.R;
import com.bove.martin.pexel.model.Foto;
import com.bumptech.glide.Glide;

import java.util.List;
/**
 * Created by Martín Bove on 31/05/2018.
 * E-mail: mbove77@gmail.com
 */
public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ViewHolder> {
    private List<Foto> fotos;
    private int layout;
    private OnItemClickListener listener;


    public FotoAdapter(List<Foto> fotos, int layout, OnItemClickListener listener) {
        this.fotos = fotos;
        this.layout = layout;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflamos la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(fotos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewFoto;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);
        }

        // Aca esta la logica de remplazo de datos y asiganacion de eventos
        public void bind(final Foto foto, final OnItemClickListener listener) {

            Glide.with(itemView)
                    .load(foto.getMedium())
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewFoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(foto, getAdapterPosition());
                }
            });
        }
    }

    // Listener para comunicar el item click
    public interface OnItemClickListener {
        void onItemClick(Foto foto, int posicion);
    }
}
