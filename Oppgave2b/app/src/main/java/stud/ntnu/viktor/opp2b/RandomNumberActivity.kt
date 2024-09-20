package stud.ntnu.viktor.opp2b

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

