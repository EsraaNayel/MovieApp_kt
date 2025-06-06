package com.esraa.nayel.movieapp.feature.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.esraa.nayel.movieapp.feature.domain.repository.MovieError

sealed interface AppError {
    data object NotFoundError : AppError
    data object ApiError : AppError
    data object NetworkError : AppError
    data class UnknownError(val message: String) : AppError
}

fun MovieError.toAppError(): AppError {
    return when (this) {
        MovieError.NotFoundError -> AppError.NotFoundError
        MovieError.ApiError -> AppError.ApiError
        MovieError.NetworkError -> AppError.NetworkError
        is MovieError.UnknownError -> AppError.UnknownError(message)
    }
}

@Composable
fun AppErrorView(error: AppError) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (error) {
                AppError.NotFoundError -> "Not found"
                AppError.ApiError -> "Api error"
                AppError.NetworkError -> "Network error, please check your connection!"
                is AppError.UnknownError -> error.message
            },
            color = MaterialTheme.colorScheme.error,
        )
    }
}
