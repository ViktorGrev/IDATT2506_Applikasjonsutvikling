package stud.ntnu.viktor.oppgave7v2.ui.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import stud.ntnu.viktor.oppgave7v2.model.Actor
import stud.ntnu.viktor.oppgave7v2.GlobalSettings
import stud.ntnu.viktor.oppgave7v2.model.Movie
import stud.ntnu.viktor.oppgave7v2.database.MovieDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    database: MovieDatabase
) {
    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(database))
    val scope = rememberCoroutineScope()

    var selectedOption by remember { mutableStateOf("Alle Filmer") }
    var directorList by remember { mutableStateOf(emptyList<String>()) }
    var movieList by remember { mutableStateOf(emptyList<Movie>()) }
    var actorList by remember { mutableStateOf(emptyList<Actor>()) }
    var selectedDirector by remember { mutableStateOf<String?>(null) }
    var selectedMovie by remember { mutableStateOf<String?>(null) }

    var expandedDirectorDropdown by remember { mutableStateOf(false) }
    var expandedMovieDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            directorList = viewModel.getAllMovies().map { it.director }.distinct()
            movieList = viewModel.getAllMovies()
        }
    }

    LaunchedEffect(selectedOption, selectedDirector, selectedMovie) {
        scope.launch {
            directorList = viewModel.getAllMovies().map { it.director }.distinct()
            movieList = viewModel.getAllMovies()
            when (selectedOption) {
                "Alle Filmer" -> movieList = viewModel.getAllMovies()
                "Filmer av Regissør" -> selectedDirector?.let { movieList = viewModel.getMoviesByDirector(it) }
                "Skuespillere i en Film" -> selectedMovie?.let { actorList = viewModel.getActorsByMovie(it) }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Movie Info") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                modifier = Modifier.background(GlobalSettings.backgroundColor.value)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GlobalSettings.backgroundColor.value)
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        selectedOption = "Alle Filmer"
                    }) {
                        Text(text = "Alle Filmer")
                    }
                    Button(onClick = {
                        selectedOption = "Filmer av Regissør"
                        expandedDirectorDropdown = true
                    }) {
                        Text(text = "Filmer av Regissør")
                    }
                    Button(onClick = {
                        selectedOption = "Skuespillere i en Film"
                        expandedMovieDropdown = true
                    }) {
                        Text(text = "Skuespillere i en Film")
                    }
                }

                if (selectedOption == "Filmer av Regissør") {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Button(onClick = { expandedDirectorDropdown = true }) {
                            Text(text = selectedDirector ?: "Velg Regissør")
                        }

                        DropdownMenu(
                            expanded = expandedDirectorDropdown,
                            onDismissRequest = { expandedDirectorDropdown = false }
                        ) {
                            directorList.forEach { director ->
                                DropdownMenuItem(
                                    text = { Text(director) },
                                    onClick = {
                                        selectedDirector = director
                                        expandedDirectorDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (selectedOption == "Skuespillere i en Film") {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Button(onClick = { expandedMovieDropdown = true }) {
                            Text(text = selectedMovie ?: "Velg Film")
                        }

                        DropdownMenu(
                            expanded = expandedMovieDropdown,
                            onDismissRequest = { expandedMovieDropdown = false }
                        ) {
                            movieList.forEach { movie ->
                                DropdownMenuItem(
                                    text = { Text(movie.title) },
                                    onClick = {
                                        selectedMovie = movie.title
                                        expandedMovieDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    when (selectedOption) {
                        "Alle Filmer" -> {
                            items(movieList) { movie ->
                                Text(
                                    text = "${movie.title} (Director: ${movie.director})",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        "Filmer av Regissør" -> {
                            items(movieList) { movie ->
                                Text(
                                    text = movie.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        "Skuespillere i en Film" -> {
                            items(actorList) { actor ->
                                Text(
                                    text = actor.actorName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

