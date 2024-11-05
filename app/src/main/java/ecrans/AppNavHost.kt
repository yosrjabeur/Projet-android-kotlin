
package ecrans

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.monapp.MainViewModel
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController) }
        composable("films") {
            FilmsScreen(viewModel = viewModel, onMovieClick = { movieId ->
                navController.navigate("movieDetails/$movieId")
            })
        }
        composable("series") {
            SeriesScreen(viewModel = viewModel, onSeriesClick = { seriesId ->
                navController.navigate("seriesDetails/$seriesId")
            })
        }
        composable("actors") { ActorsScreen(viewModel = viewModel) }
        composable("movieDetails/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: -1
            MovieDetailsScreen(viewModel, movieId)
        }
        composable("seriesDetails/{seriesId}") { backStackEntry ->
            val seriesId = backStackEntry.arguments?.getString("seriesId")?.toInt() ?: -1
            SeriesDetailsScreen(viewModel, seriesId)
        }
    }
}
