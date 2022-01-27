package com.kbz.phyothinzaraung.movies.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class MovieDatabaseTest : TestCase(){

    private lateinit var db: MovieDatabase
    private lateinit var dao: MovieDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
        dao = db.movieDao()
    }

    @After
    fun closeDB(){
        db.close()
    }

    @Test
    fun writeAndReadMovie() = runBlocking {
        val movies = dao.getMoviePageSource()
        Assert.assertNotNull(movies)
    }
}