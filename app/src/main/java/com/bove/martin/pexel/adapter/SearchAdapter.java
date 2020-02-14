package com.bove.martin.pexel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bove.martin.pexel.R;
import com.bove.martin.pexel.model.Foto;
import com.bove.martin.pexel.model.Search;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Martín Bove on 31/05/2018.
 * E-mail: mbove77@gmail.com
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Search> searches;
    private int layout;
    private OnItemClickListener listener;

    public SearchAdapter(List<Search> searches, int layout, OnItemClickListener listener) {
        this.searches = searches;
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
        holder.bind(searches.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewFoto;
        private TextView searchTerm;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewFoto = itemView.findViewById(R.id.imageSearch);
            searchTerm = itemView.findViewById(R.id.searchTerm);
        }

        // Aca esta la logica de remplazo de datos y asiganacion de eventos
        public void bind(final Search search, final OnItemClickListener listener) {

            Glide.with(itemView)
                    .load(search.getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewFoto);

            searchTerm.setText(search.getSearchInSpanish());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(search, getAdapterPosition());
                }
            });
        }
    }

    // Listener para comunicar el item click
    public interface OnItemClickListener {
        void onItemClick(Search search, int posicion);
    }
}
