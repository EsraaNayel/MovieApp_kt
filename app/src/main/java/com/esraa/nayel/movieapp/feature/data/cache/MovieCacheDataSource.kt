package com.esraa.nayel.movieapp.feature.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.esraa.nayel.movieapp.feature.data.models.MovieData

@Dao
interface MovieCacheDataSource {

    @Query("SELECT * FROM MovieData")
    fun getNowPlayingMovies(): PagingSource<Int, MovieData>

    @Query("SELECT title FROM MovieData WHERE title LIKE '%' || :query || '%' LIMIT 10")
    suspend fun getMovieTitles(query: String): List<String>

    @Upsert
    suspend fun insertMovies(movies: List<MovieData>)

    @Query("SELECT * FROM MovieData WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieData?

    @Query("DELETE FROM MovieData")
    suspend fun clearAll()

}