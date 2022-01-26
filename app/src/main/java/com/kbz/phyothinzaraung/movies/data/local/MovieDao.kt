package com.kbz.phyothinzaraung.movies.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.data.model.RemoteKey

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(articles: List<Movie>)

    @Query("SELECT * FROM movie")
    fun getMoviePageSource(): PagingSource<Int, Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(key: List<RemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(key: RemoteKey)

    @Query("SELECT * FROM remotekey WHERE id = :id")
    suspend fun getRemoteKey(id: Int): RemoteKey

    @Query("DELETE FROM remotekey")
    fun clearAllRemoteKey()

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("DELETE FROM movie")
    fun clearAllMovie()
}