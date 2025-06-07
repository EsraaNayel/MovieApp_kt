package com.esraa.nayel.movieapp.feature.ui.screens.movie_details

import DefaultMovieRepository
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.esraa.nayel.movieapp.feature.data.remote.DefaultNetworkErrorHandler
import com.esraa.nayel.movieapp.feature.domain.GetMovieUseCase
import com.esraa.nayel.movieapp.feature.domain.repository.MovieError
import com.esraa.nayel.movieapp.feature.framework.database.MoviesDatabase
import com.esraa.nayel.movieapp.feature.framework.network.MoviesNetwork
import com.esraa.nayel.movieapp.feature.ui.components.toAppError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val getMovieUseCase: GetMovieUseCase): ViewModel() {
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

                MovieViewModel(
                    GetMovieUseCase(repo),
                )
            }
        }
    }

    private val _uiState = MutableStateFlow<MovieUIState>(MovieUIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            val result = getMovieUseCase(movieId)
            result.onSuccess { movie ->
                _uiState.value = MovieUIState.Success(movie.toMovieUIModel())
            }.onFailure {
                it as MovieError
                _uiState.value = MovieUIState.Error(it.toAppError())
            }
        }
    }
}