package com.kbz.phyothinzaraung.movies.ui.movie_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kbz.phyothinzaraung.movies.R
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.databinding.ItemMovieBinding
import com.kbz.phyothinzaraung.movies.ui.RecyclerViewItemClickListener
import com.kbz.phyothinzaraung.movies.util.Constant
import java.lang.System.load

class MovieAdapter(private val listener:RecyclerViewItemClickListener): PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieComparator()) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position)!!, listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie, listener: RecyclerViewItemClickListener){
            binding.apply {
                textTitle.text = movie.title
                Glide.with(binding.root)
                    .load("${Constant.IMAGE_URL}${movie.poster_path}")
                    .centerCrop()
                    .into(imageMoviePoster)
                binding.root.setOnClickListener{listener.onRecyclerViewItemClick(binding.root, movie)}
            }
        }
    }

    class MovieComparator: DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }
}