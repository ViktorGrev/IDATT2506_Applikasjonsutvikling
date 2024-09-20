package stud.ntnu.viktor.opp2b

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import stud.ntnu.viktor.opp2b.ui.theme.Opp2bTheme

class MainActivity : ComponentActivity() {

    private val number1 = mutableStateOf(5)
    private val number2 = mutableStateOf(3)
    private var currentType: String = ""
    private var maxNumber = mutableStateOf(10)

    private val getRandomNumber =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val randomNumber = result.data?.getIntExtra("RANDOM_NUMBER_RESULT", 0) ?: 0
                updateRandomNumber(randomNumber)
                if (currentType == "number1") {
                    currentType = "number2"
                    startRandomNumberActivity(maxNumber.value)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Opp2bTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Content(
                            number1 = number1.value,
                            number2 = number2.value,
                            maxNumber = maxNumber.value,
                            onMaxNumberChange = { newMaxNumber ->
                                maxNumber.value = newMaxNumber
                            },
                            onCalculate = { type ->
                                currentType = type
                                startRandomNumberActivity(maxNumber.value)
                            }
                        )
                    }
                }
            }
        }
        // Siden vi har satt nummer 1 og nummer 2 manuelt, trenger vi ikke å hente dem tilfeldig ved oppstart
    }

    private fun startRandomNumberActivity(upperLimit: Int) {
        val intent = Intent(this, RandomNumberActivity::class.java).apply {
            putExtra("UPPER_LIMIT", upperLimit)
        }
        getRandomNumber.launch(intent)
    }

    private fun updateRandomNumber(number: Int) {
        when (currentType) {
            "number1" -> number1.value = number
            "number2" -> number2.value = number
        }
    }
}

@Composable
fun Content(
    number1: Int,
    number2: Int,
    maxNumber: Int,
    onMaxNumberChange: (Int) -> Unit,
    onCalculate: (String) -> Unit
) {
    val userInput = remember { mutableStateOf(8) }
    val context = LocalContext.current

    Column {
        Text(text = "Oppgave 2")
        Row {
            Text("Tall 1: $number1")
            Spacer(modifier = Modifier.padding(16.dp))
            Text("Tall 2: $number2")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Column {
            TextField(
                label = { Text("Resultatet") },
                value = userInput.value.toString(),
                onValueChange = {
                    userInput.value = it.toIntOrNull() ?: 0
                }
            )
            TextField(
                label = { Text("Øvre grense for tall") },
                value = maxNumber.toString(),
                onValueChange = {
                    val newMax = it.toIntOrNull() ?: 0
                    onMaxNumberChange(newMax)
                }
            )
        }
        Row {
            Button(onClick = {
                val result = number1 * number2
                if (userInput.value == result) {
                    Toast.makeText(context, "Riktig", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Feil. Riktig svar er $result", Toast.LENGTH_SHORT).show()
                }
                onCalculate("number1")
            }) {
                Text("Multipliser tallene")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = {
                val result = number1 + number2
                if (userInput.value == result) {
                    Toast.makeText(context, "Riktig", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Feil. Riktig svar er $result", Toast.LENGTH_SHORT).show()
                }
                onCalculate("number1")
            }) {
                Text("Adder tallene")
            }
        }
    }
}
