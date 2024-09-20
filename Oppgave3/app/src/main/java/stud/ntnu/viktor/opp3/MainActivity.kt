package stud.ntnu.viktor.opp3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import stud.ntnu.viktor.opp3.model.Person
import stud.ntnu.viktor.opp3.ui.theme.Opp3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Opp3Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Content()
                    }
                }
            }
        }
    }
}

@Composable
fun Content() {
    val people = remember {
        mutableStateOf(
            SnapshotStateList<Person>().apply {
                add(Person("Viktor", "20.12.2023"))
                add(Person("John", "15.05.1990"))
                add(Person("Alice", "12.11.1985"))
            }
        )
    }
    val showDialog = remember { mutableStateOf(false) }
    val newName = remember { mutableStateOf("") }
    val newDateOfBirth = remember { mutableStateOf("") }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Add New Person") },
            text = {
                Column {
                    TextField(
                        value = newName.value,
                        onValueChange = { newName.value = it },
                        label = { Text("Name") },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    TextField(
                        value = newDateOfBirth.value,
                        onValueChange = { newDateOfBirth.value = it },
                        label = { Text("Date of Birth") },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        people.value.add(Person(newName.value, newDateOfBirth.value))
                        showDialog.value = false
                        newName.value = ""
                        newDateOfBirth.value = ""
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn {
        items(people.value) { person ->
            val isExpanded = remember { mutableStateOf(false) }
            val nameState = remember { mutableStateOf(person.name) }
            val dateOfBirthState = remember { mutableStateOf(person.dateOfBirth) }

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(person.name, modifier = Modifier.weight(1f))
                        Text(person.dateOfBirth, modifier = Modifier.weight(1f))
                        Button(onClick = { isExpanded.value = !isExpanded.value }) {
                            Text(if (isExpanded.value) "Collapse" else "Expand")
                        }
                    }
                    if (isExpanded.value) {
                        TextField(
                            value = nameState.value,
                            onValueChange = { newName ->
                                nameState.value = newName
                                person.name = newName
                            },
                            label = { Text("Name") },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        TextField(
                            value = dateOfBirthState.value,
                            onValueChange = { newDateOfBirth ->
                                dateOfBirthState.value = newDateOfBirth
                                person.dateOfBirth = newDateOfBirth
                            },
                            label = { Text("Date of Birth") },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
        item {
            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text("Add New Person")
            }
        }
    }
}
