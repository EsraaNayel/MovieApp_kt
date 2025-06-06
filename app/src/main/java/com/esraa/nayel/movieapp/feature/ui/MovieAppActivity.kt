package com.esraa.nayel.movieapp.feature.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.esraa.nayel.movieapp.feature.ui.screens.movie_details.MovieScreen
import com.esraa.nayel.movieapp.feature.ui.screens.movies_list.MoviesScreen
import com.esraa.nayel.movieapp.feature.ui.theme.MoviesTheme

class MovieAppActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()

                val onMovieClick: (Int) -> Unit = { movieId ->
                    navController.navigate("${AppRoute.Movie.route}/$movieId")
                }
                val onBackClick: () -> Unit = {
                    navController.popBackStack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "Movies App",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AppRoute.Movies.route,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable(route = AppRoute.Movies.route) {
                            MoviesScreen(onMovieClick = onMovieClick)
                        }
                        composable(
                            route = "${AppRoute.Movie.route}/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) {
                            val movieId: Int = backStackEntry?.arguments?.getInt("movieId") ?: -1

                            MovieScreen(
                                movieId = movieId,
                                onBackClick = onBackClick
                            )
                        }
                    }
                }
            }
        }
    }
}
