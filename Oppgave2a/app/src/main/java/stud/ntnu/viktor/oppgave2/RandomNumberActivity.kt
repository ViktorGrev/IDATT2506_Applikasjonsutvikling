package stud.ntnu.viktor.oppgave2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

class RandomNumberActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val upperLimit = intent.getIntExtra("UPPER_LIMIT", 100)

        // Generer et tilfeldig tall med en gang aktiviteten opprettes
        val randomNumber = Random.nextInt(0, upperLimit + 1)

        // Sett resultatet og lukk aktiviteten umiddelbart
        val resultIntent = Intent().apply {
            putExtra("RANDOM_NUMBER_RESULT", randomNumber)
        }
        Toast.makeText(this, "Random number: $randomNumber", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

