package com.esraa.nayel.movieapp.feature.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.esraa.nayel.movieapp.feature.data.cache.MovieCacheDataSource
import com.esraa.nayel.movieapp.feature.data.models.MovieData
import com.esraa.nayel.movieapp.feature.data.remote.MovieRemoteDataSource
import com.esraa.nayel.movieapp.feature.data.remote.NetworkErrorHandler

@OptIn(ExperimentalPagingApi::class)
class DefaultMovieMediator(
    private val db: RoomDatabase,
    private val cacheDataSource: MovieCacheDataSource,
    private val remoteDataSource: MovieRemoteDataSource,
    private val networkErrorHandler: NetworkErrorHandler
) : RemoteMediator<Int, MovieData>() {

    private var nextPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieData>
    ): MediatorResult {
        return try {
            nextPage = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = false
                        )

                    ++nextPage
                }
            }

            val remoteData = networkErrorHandler.handle {
                remoteDataSource.getNowPlayerMovies(
                    page = nextPage,
                ).results
            }.getOrThrow()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    cacheDataSource.clearAll()
                }
                cacheDataSource.insertMovies(remoteData)
            }

            MediatorResult.Success(
                endOfPaginationReached = remoteData.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}