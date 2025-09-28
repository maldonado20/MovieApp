package com.tuapp.tmdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemMovieBinding
import com.tuapp.tmdb.data.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.VH>() {

    private val items = mutableListOf<Movie>()

    fun submit(list: List<Movie>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class VH(val b: ItemMovieBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val m = items[position]
        holder.b.txtTitle.text = m.title
        holder.b.txtOverview.text = m.overview
        holder.b.txtRating.text = "⭐ ${"%.1f".format(m.rating)}  •  ${m.releaseDate ?: "-"}"

        Glide.with(holder.itemView)
            .load(m.posterUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .into(holder.b.imgPoster)
    }

    override fun getItemCount(): Int = items.size
}
