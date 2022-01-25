package com.kbz.phyothinzaraung.movies.ui.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    repository: MoviesRepository
): ViewModel() {

    private val loadMovieChannel = Channel<String>() {}
    private val loadMovieTrigger = loadMovieChannel.receiveAsFlow()
    var onprogress = false
    @ExperimentalPagingApi
    val movie = loadMovieTrigger.flatMapLatest {
        repository.getMovies().cachedIn(viewModelScope)
    }.cachedIn(viewModelScope)

    fun onStart() {
        viewModelScope.launch {
            loadMovieChannel.send("_")
            onprogress=true
        }

    }
}
