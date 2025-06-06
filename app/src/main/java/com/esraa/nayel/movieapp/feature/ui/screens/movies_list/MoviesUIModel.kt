package com.esraa.nayel.movieapp.feature.ui.screens.movies_list

import androidx.compose.runtime.Immutable
import com.esraa.nayel.movieapp.Constants
import com.esraa.nayel.movieapp.feature.domain.models.Movie


@Immutable
data class MoviesUIModel(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
)

fun Movie.toMoviesUIModel() = MoviesUIModel(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = Constants.IMAGE_BASE_URL + posterPath,
)