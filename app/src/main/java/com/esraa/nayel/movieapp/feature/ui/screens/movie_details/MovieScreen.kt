package com.esraa.nayel.movieapp.feature.ui.screens.movie_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.esraa.nayel.movieapp.feature.ui.components.AppErrorView
import com.esraa.nayel.movieapp.feature.ui.components.AppLoadingView

@Composable
fun MovieScreen(
    movieId: Int,
    viewModel: MovieViewModel = viewModel<MovieViewModel>(factory = MovieViewModel.Factory),
    onBackClick: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    viewModel.getMovie(movieId)

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
        when (uiState) {
            MovieUIState.Loading -> {
                AppLoadingView()
            }

            is MovieUIState.Error -> {
                AppErrorView(error = uiState.error)
            }

            is MovieUIState.Success -> {
                MovieContent(movie = uiState.movie)
            }
        }
    }
}

@Composable
private fun MovieContent(movie: MovieUIModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Card(
            modifier = Modifier
                .height(500.dp)
                .padding(8.dp)
        ) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterPath).crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .height(500.dp),
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.3f, Color.Transparent), Pair(
                                        1.5f, MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = movie.title,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Released: ${movie.releaseDate}",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            )

                            Text(
                                text = "Runtime: ${movie.runtime} minutes",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            )
                        }
                    }
                }
            }
        }

        Text(
            modifier = Modifier.padding(8.dp),
            text = movie.overview,
            color = MaterialTheme.colorScheme.onBackground,
        )

        GenreChips(movie.genres)
    }
}

@Composable
private fun GenreChips(genres: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        genres.forEach { genre ->
            SuggestionChip(
                onClick = { },
                label = { Text(genre) }
            )
        }
    }
}