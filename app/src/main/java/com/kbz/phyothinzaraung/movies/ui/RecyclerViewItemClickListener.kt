package com.kbz.phyothinzaraung.movies.ui

import android.view.View
import com.kbz.phyothinzaraung.movies.data.model.Movie

interface RecyclerViewItemClickListener {
    fun onRecyclerViewItemClick(view: View, movie: Movie)
}