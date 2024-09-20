package stud.ntnu.viktor.oppgave6tjener

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val server = ChatServer(12345)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //trenger ingen UI elementer :D
        }

        lifecycleScope.launch {
            server.startServer()
        }
    }
}