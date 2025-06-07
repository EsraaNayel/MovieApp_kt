package com.esraa.nayel.movieapp.feature.domain

import DefaultMovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSearchSuggestionsUseCase(
    private val repository: DefaultMovieRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(query: String): Result<List<String>> = withContext(dispatcher) {
        repository.getSearchSuggestions(query)
    }
}