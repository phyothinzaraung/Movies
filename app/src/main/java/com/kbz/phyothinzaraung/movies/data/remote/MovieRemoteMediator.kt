package com.kbz.phyothinzaraung.movies.data.remote

import androidx.paging.*
import androidx.room.withTransaction
import com.kbz.phyothinzaraung.movies.data.local.MovieDatabase
import com.kbz.phyothinzaraung.movies.data.model.Movie
import com.kbz.phyothinzaraung.movies.data.model.RemoteKey
import com.kbz.phyothinzaraung.movies.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception

private const val START_PAGE_INDEX = 1

@ExperimentalPagingApi
class MovieRemoteMediator(
    private val api: MovieListApi,
    private val movieDb: MovieDatabase
): RemoteMediator<Int, Movie>() {
    private val localData = movieDb.movieDao()
    private val remoteKeyData = movieDb.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }

        }

        try {

            val response = api.getAllMovies(Constant.API_KEY, page)
            val remoteMovies = response.results

            val movies = remoteMovies.map { remoteMovies: Movie ->
                Movie(
                    id = remoteMovies.id,
                    title = remoteMovies.title,
                    overview = remoteMovies.overview,
                    release_date = remoteMovies.release_date,
                    poster_path = remoteMovies.poster_path,
                    original_language = remoteMovies.original_language
                )
            }

            movieDb.withTransaction {
                val prevKey = if (page == START_PAGE_INDEX) null else page - 1
                val nextKey = page + 1
                val key = remoteMovies.map {
                    RemoteKey(it.id, nextKey, prevKey)
                }
                remoteKeyData.insertRemoteKey(key)
                localData.insertMovies(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: retrofit2.HttpException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)

        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRefreshRemoteKey(state)
                remoteKeys?.nextPageKey?.minus(1) ?: START_PAGE_INDEX

            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.previousKey ?: MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)

                val nextKey = remoteKeys?.nextPageKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey

            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Movie>): RemoteKey? {
        return withContext(Dispatchers.IO) {
            state.pages
                .firstOrNull { it.data.isNotEmpty() }
                ?.data?.firstOrNull()
                ?.let { movie -> localData.getRemoteKey(movie.id) }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH

    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): RemoteKey? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { data ->
                // Get the remote keys of the last item retrieved
                localData.getRemoteKey(data.id)
            }
    }

    private suspend fun getRefreshRemoteKey(state: PagingState<Int, Movie>): RemoteKey? {
        return withContext(Dispatchers.IO) {
            state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { id ->
                    localData.getRemoteKey(id)
                }
            }
        }
    }
}