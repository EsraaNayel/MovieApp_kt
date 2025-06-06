package com.esraa.nayel.movieapp.feature.domain.models

data class Movie(
    val id: Int,
    val title: String,
    val language: String,
    val overview: String,
    val releaseDate: String,

    val backdropPath: String,
    val posterPath: String,

    val runtime: Int = 0,
    val genres: List<String>
)
