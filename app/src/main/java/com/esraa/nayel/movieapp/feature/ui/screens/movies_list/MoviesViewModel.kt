package com.esraa.nayel.movieapp.feature.ui.screens.movies_list

import DefaultMovieRepository
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import androidx.paging.map
import com.esraa.nayel.movieapp.feature.data.remote.DefaultNetworkErrorHandler
import com.esraa.nayel.movieapp.feature.domain.GetNowPlayingMoviesUseCase
import com.esraa.nayel.movieapp.feature.domain.GetSearchSuggestionsUseCase
import com.esraa.nayel.movieapp.feature.domain.SearchMoviesUseCase
import com.esraa.nayel.movieapp.feature.framework.database.MoviesDatabase
import com.esraa.nayel.movieapp.feature.framework.network.MoviesNetwork
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase

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
                    SearchMoviesUseCase(repo),
                    GetSearchSuggestionsUseCase(repo)
                )
            }
        }
    }

    private val _searchSuggestions = MutableStateFlow<List<String>>(emptyList())
    val searchSuggestions = _searchSuggestions.asStateFlow()

    private val _isLoadingSuggestions = MutableStateFlow(false)
    val isLoadingSuggestions = _isLoadingSuggestions.asStateFlow()

    private var suggestionJob: Job? = null


    private val _uiState = MutableStateFlow(MoviesUIState())
    val uiState = _uiState.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getNowPlayingMovies()
        setupAutocomplete()
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
            _uiState.value = _uiState.value.copy(searchMovies = emptyFlow())
        }
    }


    @OptIn(FlowPreview::class)
    private fun setupAutocomplete() {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank() && query.length >= 2) {
                        loadSuggestions(query)
                    } else {
                        _searchSuggestions.value = emptyList()
                    }
                }
        }
    }

    private fun loadSuggestions(query: String) {
        suggestionJob?.cancel()
        suggestionJob = viewModelScope.launch {
            _isLoadingSuggestions.value = true
            try {
                getSearchSuggestionsUseCase(query)
                    .onSuccess { suggestions ->
                        _searchSuggestions.value = suggestions
                    }
                    .onFailure {
                        _searchSuggestions.value = emptyList()
                    }
            } finally {
                _isLoadingSuggestions.value = false
            }
        }
    }

    fun onSuggestionSelected(suggestion: String) {
        _searchQuery.value = suggestion
        searchMovie(suggestion)
        _searchSuggestions.value = emptyList()
    }


}