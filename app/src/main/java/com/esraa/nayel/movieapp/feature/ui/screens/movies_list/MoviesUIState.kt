package com.esraa.nayel.movieapp.feature.ui.screens.movies_list

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Stable
data class MoviesUIState(val nowPlayingMovies: Flow<PagingData<MoviesUIModel>> = emptyFlow(),
    val searchMovies: Flow<PagingData<MoviesUIModel>> = emptyFlow())
{

}