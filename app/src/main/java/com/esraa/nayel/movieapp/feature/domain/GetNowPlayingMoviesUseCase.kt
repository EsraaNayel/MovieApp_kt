package com.esraa.nayel.movieapp.feature.domain

import com.esraa.nayel.movieapp.feature.data.DefaultMovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetNowPlayingMoviesUseCase(
    private val repository: DefaultMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke() = repository.getMovies().flowOn(dispatcher)
}