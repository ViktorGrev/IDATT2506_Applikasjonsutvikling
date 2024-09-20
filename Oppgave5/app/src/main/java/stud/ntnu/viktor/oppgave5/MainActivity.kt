package stud.ntnu.viktor.oppgave5

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import stud.ntnu.viktor.oppgave5.ui.theme.Oppgave5Theme

class MainActivity : ComponentActivity() {
    private val apiService by lazy { GameApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Oppgave5Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        apiService = apiService
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, apiService: GameApiService) {
    var name by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var guess by remember { mutableStateOf("") }
    var responseMessage by remember { mutableStateOf("") }
    var isGameStarted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (!isGameStarted) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Navn") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Kortnummer") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = apiService.registerUser(name, cardNumber)
                            responseMessage = response
                            isGameStarted = response.contains("Oppgi et tall")
                        } catch (e: Exception) {
                            responseMessage = "Registrering feilet: ${e.message}"
                            Log.e("GameResponse", responseMessage)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start nytt spill")
            }
        } else {
            TextField(
                value = guess,
                onValueChange = { guess = it },
                label = { Text("Tipp et tall") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (guess.isNotEmpty()) {
                            try {
                                val response = apiService.submitGuess(guess.toInt())
                                responseMessage = response
                                if (response.contains("vunnet") || response.contains("ingen flere sjanser")) {
                                    isGameStarted = false // Reset game state after win or loss
                                }
                            } catch (e: Exception) {
                                responseMessage = "Tipping feilet: ${e.message}"
                            }
                        } else {
                            responseMessage = "Vennligst oppgi et tall."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send inn tipp")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = responseMessage)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Oppgave5Theme {
        MainScreen(apiService = GameApiService.create())
    }
}
