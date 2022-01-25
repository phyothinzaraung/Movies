package com.kbz.phyothinzaraung.movies.ui.movie_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kbz.phyothinzaraung.movies.databinding.ActivityMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private val viewModel: MoviesViewModel by viewModels()
    private val movieAdapter = MovieAdapter()
    private lateinit var binding: ActivityMoviesBinding

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recyclerviewMovies.apply {
                adapter = movieAdapter
                layoutManager = GridLayoutManager(this@MoviesActivity, 2)
            }

            fetchMovies()
            }
        }

    @ExperimentalPagingApi
    private fun fetchMovies() {
        lifecycleScope.launchWhenCreated {
            viewModel.movie.collectLatest {
                movieAdapter.submitData(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()

    }
}