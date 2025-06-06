package com.esraa.nayel.movieapp.feature.domain.repository

sealed class MovieError: RuntimeException() {
    data object NotFoundError : MovieError()
    data object ApiError : MovieError()
    data object NetworkError : MovieError()
    data class UnknownError(override val message: String) : MovieError()
}