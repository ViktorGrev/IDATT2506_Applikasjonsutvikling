package stud.ntnu.viktor.oppgave7v2.ui.movie

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import stud.ntnu.viktor.oppgave7v2.model.Actor
import stud.ntnu.viktor.oppgave7v2.model.Movie
import stud.ntnu.viktor.oppgave7v2.database.MovieDatabase

class MovieViewModel(private val database: MovieDatabase) : ViewModel() {

    suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        database.movieDao().getAllMovies()
    }

    suspend fun getMoviesByDirector(director: String): List<Movie> = withContext(Dispatchers.IO) {
        database.movieDao().getAllMovies().filter { it.director == director }
    }

    suspend fun getActorsByMovie(movieTitle: String): List<Actor> = withContext(Dispatchers.IO) {
        database.movieDao().getAllActors().filter { it.movieTitle == movieTitle }
    }
}
