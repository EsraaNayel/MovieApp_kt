package com.esraa.nayel.movieapp.feature.data.remote.models

import com.esraa.nayel.movieapp.feature.data.models.MovieData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MovieData>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)