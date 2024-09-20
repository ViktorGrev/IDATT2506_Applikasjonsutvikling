package stud.ntnu.viktor.oppgave6

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import stud.ntnu.viktor.oppgave6.ui.theme.Oppgave6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Oppgave6Theme {
                    ChatScreen(ChatViewModel())
            }
        }
    }
}

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages by viewModel.messages.collectAsState()
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.padding(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Text("Chat messages", fontSize = 24.sp)
            }
            items(messages) { msg ->
                Log.e("ChatScreen", "Displaying message: $msg")
                Text(text = msg, modifier = Modifier.padding(4.dp), fontSize = 20.sp)
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                viewModel.sendMessage(message)
                message = ""
            }) {
                Text("Send")
            }
        }
    }
}
