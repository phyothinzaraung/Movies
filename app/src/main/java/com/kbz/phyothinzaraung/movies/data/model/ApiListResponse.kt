package com.kbz.phyothinzaraung.movies.data.model

import kotlinx.coroutines.flow.Flow

data class ApiListResponse(val page: Int, val results: List<Movie>)
