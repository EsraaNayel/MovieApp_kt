package com.esraa.nayel.movieapp.feature.ui.screens.movie_details

import androidx.compose.runtime.Immutable
import com.esraa.nayel.movieapp.Constants
import com.esraa.nayel.movieapp.feature.domain.models.Movie

@Immutable
data class MovieUIModel(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,

    val posterPath: String,

    val runtime: Int = 0,
    val genres: List<String>
)

fun Movie.toMovieUIModel() = MovieUIModel(
    id = id,
    title = title,
    overview = overview,
    releaseDate = releaseDate,

    posterPath = Constants.IMAGE_BASE_URL + posterPath,

    runtime = runtime,
    genres = genres
)