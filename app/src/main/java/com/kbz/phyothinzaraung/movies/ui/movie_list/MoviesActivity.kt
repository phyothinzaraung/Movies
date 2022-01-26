package com.kbz.phyothinzaraung.movies.ui.movie_list

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.kbz.phyothinzaraung.movies.R
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.databinding.ActivityMoviesBinding
import com.kbz.phyothinzaraung.movies.ui.RecyclerViewItemClickListener
import com.kbz.phyothinzaraung.movies.ui.movie_detail.MovieDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.Serializable

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity(), RecyclerViewItemClickListener {

    private val viewModel: MoviesViewModel by viewModels()
    private val movieAdapter = MovieAdapter(this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRecyclerViewItemClick(view: View, movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("MOVIE_EXTRA", movie)
        startActivity(intent)
    }
}