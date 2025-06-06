package com.esraa.nayel.movieapp.feature.ui.screens.movies_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import androidx.paging.map
import com.esraa.nayel.movieapp.feature.data.DefaultMovieRepository
import com.esraa.nayel.movieapp.feature.data.remote.DefaultNetworkErrorHandler
import com.esraa.nayel.movieapp.feature.domain.GetNowPlayingMoviesUseCase
import com.esraa.nayel.movieapp.feature.domain.SearchMoviesUseCase
import com.esraa.nayel.movieapp.feature.framework.database.MoviesDatabase
import com.esraa.nayel.movieapp.feature.framework.network.MoviesNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class MoviesViewModel(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                val db = MoviesDatabase.getInstance(context)
                val network = MoviesNetwork.getInstance()

                val cache = db.dataSource()
                val remote = network.dataSource()
                val networkErrorHandler = DefaultNetworkErrorHandler()

                val repo = DefaultMovieRepository(db, cache, remote, networkErrorHandler)

                MoviesViewModel(
                    GetNowPlayingMoviesUseCase(repo),
                    SearchMoviesUseCase(repo)
                )
            }
        }
    }

    private val _uiState = MutableStateFlow(MoviesUIState())
    val uiState = _uiState.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getNowPlayingMovies()
    }

    private fun getNowPlayingMovies() {
        _uiState.value = _uiState.value.copy(
            nowPlayingMovies = getNowPlayingMoviesUseCase().map { pagingData ->
                pagingData.map { it.toMoviesUIModel() }
            }.cachedIn(viewModelScope)
        )
    }

    fun searchMovie(searchText: String) {
        _uiState.value = _uiState.value.copy(
            searchMovies = searchMoviesUseCase(searchText).map { pagingData ->
                pagingData.map { it.toMoviesUIModel() }
            }.cachedIn(viewModelScope)
        )
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query

        if (query.isNotBlank()) {
            searchMovie(query)
        } else {
            // Optional: Clear search results or just show now playing
            _uiState.value = _uiState.value.copy(searchMovies = emptyFlow())
        }
    }
}