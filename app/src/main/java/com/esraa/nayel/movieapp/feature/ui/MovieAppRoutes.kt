package com.esraa.nayel.movieapp.feature.ui


sealed class AppRoute(
    open val route: String, open val label: String
) {
    data object Movies : AppRoute("Movies", "Movies List")
    data object Movie : AppRoute("Movie", "Movie Details")
}
