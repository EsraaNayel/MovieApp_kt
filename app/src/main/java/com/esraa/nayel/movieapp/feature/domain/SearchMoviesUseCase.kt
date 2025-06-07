package com.esraa.nayel.movieapp.feature.domain

import DefaultMovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class SearchMoviesUseCase(
    private val repository: DefaultMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(query: String) = repository.searchMovies(query).flowOn(dispatcher)
}