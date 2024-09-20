package stud.ntnu.viktor.oppgave7v2.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import stud.ntnu.viktor.oppgave7v2.GlobalSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val colors = listOf(Color.White, Color.Yellow, Color.Green, Color.Blue, Color.Red)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.background(GlobalSettings.backgroundColor.value)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Background Color",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(color)
                            .clickable {
                                GlobalSettings.backgroundColor.value = color
                            }
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}
