package com.kbz.phyothinzaraung.movies.ui.movie_list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.data.remote.MovieListApi
import com.kbz.phyothinzaraung.movies.util.Constant
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(val api: MovieListApi): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page: Int = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = api.getAllMovies(Constant.API_KEY, page)

            LoadResult.Page(data = response.results, prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1)
        } catch (exception: IOException){
            LoadResult.Error(exception)
        }catch (exception: HttpException){
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val DEFAULT_PAGE_INDEX = 1
    }
}
