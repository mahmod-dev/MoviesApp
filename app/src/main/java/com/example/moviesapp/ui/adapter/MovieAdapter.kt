package com.example.moviesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.data.entity.Movie
import com.example.moviesapp.databinding.MovieItemBinding

class MovieAdapter(private val data: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.Holder>() {
    var onItemClick: ((Movie) -> Unit)? = null
    var onDeleteClick: ((Movie) -> Unit)? = null
    var onEditClick: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class Holder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                tvMovieName.text = movie.name
                tvYear.text = movie.year
                rateBar.rating = movie.rate
                Glide.with(binding.root.context)
                    .load(movie.imageUrl)
                    .into(imagePoster)

                imgDelete.setOnClickListener {
                    onDeleteClick?.invoke(movie)
                    data.removeAt(adapterPosition);
                    notifyItemRemoved(adapterPosition)
                }

                imgEdit.setOnClickListener {
                    onEditClick?.invoke(movie)
                }
                imagePoster.setOnClickListener {
                    onItemClick?.invoke(movie)
                }
            }
        }

    }

}