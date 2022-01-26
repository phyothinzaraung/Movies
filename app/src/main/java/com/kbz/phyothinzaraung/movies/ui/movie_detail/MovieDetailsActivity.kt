package com.kbz.phyothinzaraung.movies.ui.movie_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.databinding.ActivityMovieDetailsBinding
import com.kbz.phyothinzaraung.movies.util.Constant
import android.view.MenuItem
import com.kbz.phyothinzaraung.movies.R

class MovieDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movie = intent.getSerializableExtra("MOVIE_EXTRA") as Movie

        setupToolbar()

        binding.apply {
            Glide.with(applicationContext)
                .load("${Constant.IMAGE_URL}${movie.poster_path}")
                .centerInside()
                .into(imageMovieBackdrop)

            Glide.with(applicationContext)
                .load("${Constant.IMAGE_URL}${movie.poster_path}")
                .centerCrop()
                .into(movieDetailsInfo.imagePoster)

            movieDetailsInfo.textTitle.text = movie.title
            movieDetailsInfo.textLanguage.text = movie.original_language
            movieDetailsInfo.textReleaseDate.text = movie.release_date
            movieDetailsInfo.textOverview.text = movie.overview
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            handleCollapsedToolbarTitle()
        }
    }

    private fun handleCollapsedToolbarTitle() {
        binding.appbar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                // verify if the toolbar is completely collapsed and set the movie name as the title
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.title = movie.title
                    isShow = true
                } else if (isShow) {
                    // display an empty string when toolbar is expanded
                    binding.collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }
}