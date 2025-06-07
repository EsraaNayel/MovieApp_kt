package com.esraa.nayel.movieapp.feature.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.esraa.nayel.movieapp.feature.data.models.MovieData
import com.esraa.nayel.movieapp.feature.data.remote.MovieRemoteDataSource

class SearchMoviesPagingSource(private val api: MovieRemoteDataSource,
                               private val query: String
) : PagingSource<Int, MovieData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
        return try {
            val page = params.key ?: 1
            val response = api.searchMovies(query = query, page = page)

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieData>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}