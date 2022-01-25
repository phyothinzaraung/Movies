package com.kbz.phyothinzaraung.movies.data.remote

import com.kbz.phyothinzaraung.movies.data.model.ApiListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieListApi {

    @GET("movie/popular")
    suspend fun getAllMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int) : ApiListResponse

}