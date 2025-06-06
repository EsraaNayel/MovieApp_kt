package com.esraa.nayel.movieapp.feature.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.esraa.nayel.movieapp.feature.domain.models.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity
@Serializable
data class MovieData(
    @PrimaryKey(autoGenerate = true)
    @Transient
    val index: Int = 0,

    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("original_language")
    val originalLanguage: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("poster_path")
    val posterPath: String?,

    val sources: Set<String> = emptySet(),
) {
    @Ignore
    var runtime: Int? = 0

    @Ignore
    var genres: List<MovieGenre>? = emptyList()
}

@Serializable
data class MovieGenre(
    val id: Int,
    val name: String
)

fun MovieData.toMovie(): Movie {
    return Movie(
        id = id,
        title = title ?: "",
        language = originalLanguage ?: "",
        overview = overview ?: "",
        releaseDate = releaseDate ?: "",

        backdropPath = backdropPath ?: "",
        posterPath = posterPath ?: "",

        runtime = runtime ?: 0,
        genres = genres?.map { it.name } ?: emptyList(),
    )
}