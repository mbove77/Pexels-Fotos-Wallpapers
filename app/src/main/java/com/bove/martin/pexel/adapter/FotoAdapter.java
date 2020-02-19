package com.bove.martin.pexel.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
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
    private int oldposition = 0;


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
        if (position>oldposition) {
            animate(holder,true);
        }
        else animate(holder,false);
        oldposition = position;
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    void animate(FotoAdapter.ViewHolder hold,boolean down) {
        ObjectAnimator animator = new ObjectAnimator().ofFloat(hold.itemView,"TranslationX",-100f,0f);
        animator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animator1 = new ObjectAnimator().ofFloat(hold.itemView,"TranslationX",100f,0f);
        animator1.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animator2 = new ObjectAnimator().ofFloat(hold.itemView,"TranslationY",down?100f:-100f,0f);
        animator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animator3 = new ObjectAnimator().ofFloat(hold.itemView,"ScaleX",1.2f,1f);
        animator3.setInterpolator(new AnticipateOvershootInterpolator());
        ObjectAnimator animator4 = new ObjectAnimator().ofFloat(hold.itemView,"ScaleY",1.2f,1f);
        animator4.setInterpolator(new AnticipateOvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator,animator1,animator2,animator3,animator4);
        set.setDuration(1000);
        set.start();
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
