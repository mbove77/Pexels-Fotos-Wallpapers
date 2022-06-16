package com.bove.martin.pexel.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bove.martin.pexel.R
import com.bove.martin.pexel.domain.model.Search
import com.bumptech.glide.Glide

/**
 * Created by Martín Bove on 31/05/2018.
 * E-mail: mbove77@gmail.com
 */
class SearchAdapter(private val searches: List<Search>, private val layout: Int, private val listener: OnSearchItemClickListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflamos la vista
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searches[position], listener)
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    // ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewFoto: ImageView
        private val searchTerm: TextView

        // Aca esta la logica de remplazo de datos y asiganacion de eventos
        fun bind(search: Search, listener: OnSearchItemClickListener) {

            // if photo is null is the curated especial item
            Glide.with(itemView)
                    .load(search.photo)
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewFoto)
            searchTerm.text = search.searchInSpanish
            itemView.setOnClickListener { listener.onSearchSuggestItemClick(search, bindingAdapterPosition) }
        }

        init {
            imageViewFoto = itemView.findViewById(R.id.imageSearch)
            searchTerm = itemView.findViewById(R.id.searchTerm)
        }
    }

    // Listener para comunicar el item click
    interface OnSearchItemClickListener {
        fun onSearchSuggestItemClick(search: Search, posicion: Int)
    }
}