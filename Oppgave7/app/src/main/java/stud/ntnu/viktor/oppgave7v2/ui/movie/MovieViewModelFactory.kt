package stud.ntnu.viktor.oppgave7v2.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import stud.ntnu.viktor.oppgave7v2.database.MovieDatabase

class MovieViewModelFactory(private val database: MovieDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
