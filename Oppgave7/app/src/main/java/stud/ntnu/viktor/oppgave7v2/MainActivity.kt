package stud.ntnu.viktor.oppgave7v2

import android.content.ContentValues
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import stud.ntnu.viktor.oppgave7v2.database.MovieDatabase
import stud.ntnu.viktor.oppgave7v2.model.Actor
import stud.ntnu.viktor.oppgave7v2.model.Movie
import stud.ntnu.viktor.oppgave7v2.ui.navigation.AppNavigation
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private lateinit var database: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(applicationContext, MovieDatabase::class.java, "movie_database").build()

        setContent {
            AppNavigation(database)
        }

        CoroutineScope(Dispatchers.IO).launch {
            loadMoviesFromRaw()
            writeMoviesToDownloadsFolder()
        }

    }

    private suspend fun loadMoviesFromRaw() {
        try {
            val inputStream = resources.openRawResource(R.raw.movies)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val movieJsonString = bufferedReader.use { it.readText() }

            val movies = parseJsonToMovies(movieJsonString)
            saveMoviesToDatabase(movies)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseJsonToMovies(jsonString: String): List<Pair<Movie, List<Actor>>> {
        val movies = mutableListOf<Pair<Movie, List<Actor>>>()
        val jsonArray = org.json.JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            val director = jsonObject.getString("director")
            val actorsJsonArray = jsonObject.getJSONArray("actors")
            val actors = mutableListOf<Actor>()
            for (j in 0 until actorsJsonArray.length()) {
                actors.add(Actor(title, actorsJsonArray.getString(j)))
            }
            movies.add(Pair(Movie(title, director), actors))
        }
        return movies
    }

    private suspend fun saveMoviesToDatabase(movies: List<Pair<Movie, List<Actor>>>) {
        database.movieDao().insertMovies(movies.map { it.first })
        movies.forEach { database.movieDao().insertActors(it.second) }
    }

    private suspend fun writeMoviesToDownloadsFolder() {
        val movies = database.movieDao().getAllMovies()
        val actors = database.movieDao().getAllActors()

        val movieInfo = movies.joinToString(separator = "\n") { movie ->
            val movieActors = actors.filter { it.movieTitle == movie.title }.joinToString { it.actorName }
            "Title: ${movie.title}, Director: ${movie.director}, Actors: $movieActors"
        }

        withContext(Dispatchers.IO) {
            val resolver = applicationContext.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "movies_output.txt")
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(movieInfo)
                        writer.flush()
                    }
                }
            }
        }
    }
}
