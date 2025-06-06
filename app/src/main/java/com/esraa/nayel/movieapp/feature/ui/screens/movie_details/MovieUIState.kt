package com.esraa.nayel.movieapp.feature.ui.screens.movie_details

import com.esraa.nayel.movieapp.feature.ui.components.AppError

sealed interface  MovieUIState {

    data object Loading : MovieUIState
    data class Success(val movie: MovieUIModel) : MovieUIState
    data class Error(val error: AppError) : MovieUIState
}