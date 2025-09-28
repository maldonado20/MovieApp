package com.tuapp.tmdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.data.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.VH>() {
    private val items = mutableListOf<Movie>()
    fun submit(list: List<Movie>) { items.clear(); items.addAll(list); notifyDataSetChanged() }
    class VH(val b: ItemMovieBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemMovieBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, i: Int) {
        val m = items[i]
        h.b.txtTitle.text = m.title
        h.b.txtOverview.text = m.overview
        h.b.txtRating.text = "⭐ ${"%.1f".format(m.rating)} • ${m.releaseDate ?: "-"}"
        Glide.with(h.itemView).load(m.posterUrl)
            .placeholder(android.R.color.darker_gray).error(android.R.color.darker_gray)
            .into(h.b.imgPoster)
    }
    override fun getItemCount() = items.size
}
