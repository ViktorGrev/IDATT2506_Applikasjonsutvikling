package stud.ntnu.viktor.oppgave2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import stud.ntnu.viktor.oppgave2.RandomNumberActivity

class MainActivity : ComponentActivity() {

    private val getRandomNumber =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val randomNumber = result.data?.getIntExtra("RANDOM_NUMBER_RESULT", 0)
                randomNumber?.let {
                    updateRandomNumber(it)
                }
            }
        }

    private val randomNumber = mutableStateOf("Result will appear here")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestApp(randomNumber.value) { startRandomNumberActivity() }
        }
    }

    private fun startRandomNumberActivity() {
        val intent = Intent(this, RandomNumberActivity::class.java)
        intent.putExtra("UPPER_LIMIT", 100)
        getRandomNumber.launch(intent)
    }

    private fun updateRandomNumber(number: Int) {
        randomNumber.value = number.toString()
    }
}

@Composable
fun TestApp(randomNumber: String, onButtonClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onButtonClick) {
            Text("Start Random Number Activity")
        }
        Text(
            text = randomNumber,
            modifier = Modifier.fillMaxSize(),
            fontSize = 24.sp
        )
    }
}
