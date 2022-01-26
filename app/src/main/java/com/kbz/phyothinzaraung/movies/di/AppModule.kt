package com.kbz.phyothinzaraung.movies.di

import android.app.Application
import androidx.room.Room
import com.kbz.phyothinzaraung.movies.data.local.MovieDatabase
import com.kbz.phyothinzaraung.movies.data.remote.MovieListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://api.themoviedb.org/3/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideMovieListApi(retrofit: Retrofit): MovieListApi =
        retrofit.create(MovieListApi::class.java)

    @Singleton
    @Provides
    fun provideMovieListDatabase(app: Application): MovieDatabase =
        Room.databaseBuilder(app, MovieDatabase::class.java, "movie_database").build()
}