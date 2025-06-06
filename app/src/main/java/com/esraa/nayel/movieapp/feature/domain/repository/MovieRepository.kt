package com.esraa.nayel.movieapp.feature.domain.repository

import androidx.paging.PagingData
import com.esraa.nayel.movieapp.feature.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<PagingData<Movie>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>

    suspend fun getMovie(id: Int): Result<Movie>

}