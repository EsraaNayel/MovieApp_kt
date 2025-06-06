package com.esraa.nayel.movieapp.feature.domain

import com.esraa.nayel.movieapp.feature.domain.repository.MovieRepository

class GetMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = repository.getMovie(movieId)

}