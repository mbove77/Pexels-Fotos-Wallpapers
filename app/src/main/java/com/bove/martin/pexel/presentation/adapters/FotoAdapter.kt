package com.bove.martin.pexel.presentation.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.Foto
import com.bumptech.glide.Glide

/**
 * Created by Martín Bove on 31/05/2018.
 * E-mail: mbove77@gmail.com
 */
class FotoAdapter(private val fotos: List<Foto>, private val layout: Int, private val listener: OnFotoClickListener) : RecyclerView.Adapter<FotoAdapter.ViewHolder>() {
    private var oldposition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflamos la vista
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fotos[holder.bindingAdapterPosition], listener)
        if (holder.bindingAdapterPosition > oldposition) {
            animate(holder, true)
        } else animate(holder, false)
        oldposition = holder.bindingAdapterPosition
    }

    override fun getItemCount(): Int {
        return fotos.size
    }

    private fun animate(hold: ViewHolder, down: Boolean) {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(hold.itemView, "TranslationX", -100f, 0f)
        animator.interpolator = OvershootInterpolator()
        val animator1: ObjectAnimator = ObjectAnimator.ofFloat(hold.itemView, "TranslationX", 100f, 0f)
        animator1.interpolator = OvershootInterpolator()
        val animator2: ObjectAnimator = ObjectAnimator.ofFloat(hold.itemView, "TranslationY", if (down) 120f else -120f, 0f)
        animator.interpolator = OvershootInterpolator()
        val animator3: ObjectAnimator = ObjectAnimator.ofFloat(hold.itemView, "ScaleX", 0.9f, 1f)
        animator3.interpolator = AnticipateOvershootInterpolator()
        val animator4: ObjectAnimator = ObjectAnimator.ofFloat(hold.itemView, "ScaleY", 0.9f, 1f)
        animator4.interpolator = AnticipateOvershootInterpolator()
        val set = AnimatorSet()
        set.playTogether(animator2, animator3, animator4)
        set.duration = 1000
        set.start()
    }

    // ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewFoto: ImageView

        // Aca esta la logica de remplazo de datos y asiganacion de eventos
        fun bind(foto: Foto, listener: OnFotoClickListener) {
            Glide.with(itemView)
                    .load(foto.medium)
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewFoto)
            itemView.setOnClickListener { listener.onFotoClick(foto, bindingAdapterPosition) }
        }

        init {
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto)
        }
    }

    // Listener para comunicar el item click
    interface OnFotoClickListener {
        fun onFotoClick(foto: Foto, position: Int)
    }
}