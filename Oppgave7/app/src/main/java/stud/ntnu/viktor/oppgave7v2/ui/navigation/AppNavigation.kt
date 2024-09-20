package stud.ntnu.viktor.oppgave7v2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import stud.ntnu.viktor.oppgave7v2.database.MovieDatabase
import stud.ntnu.viktor.oppgave7v2.ui.movie.MovieScreen
import stud.ntnu.viktor.oppgave7v2.ui.settings.SettingsScreen

@Composable
fun AppNavigation(database: MovieDatabase) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "movie") {
        composable("movie") { MovieScreen(navController = navController, database = database) }
        composable("settings") { SettingsScreen(navController = navController) }
    }
}
