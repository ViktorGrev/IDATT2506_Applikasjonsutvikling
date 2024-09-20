package stud.ntnu.viktor.greeting

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import stud.ntnu.viktor.greeting.ui.theme.GreetingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreetingTheme  {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OptionsMenu") },
                actions = {
                    DropdownMenu()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Red)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
        }
    }
}

@Composable
fun DropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select an option") }

    val fornavn: String = "Viktor"
    val etternavn: String = "Grevskott"

    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Blue),
    ) {
        TextButton(onClick = { expanded = true }) {
            Text(text = selectedOption, color = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Button(onClick = {
                selectedOption = "$fornavn"
                expanded = false
                Log.w("LogForDropDown", "$fornavn")
            }) {
                Text("$fornavn")
            }
            Button(onClick = {
                selectedOption = "$etternavn"
                expanded = false
                Log.e("LogForDropDown", "$etternavn")
            }) {
                Text("$etternavn")
            }
        }
    }
}


















