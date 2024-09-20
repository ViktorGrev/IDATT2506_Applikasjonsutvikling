package stud.ntnu.viktor.oppgave6

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private val client = ChatClient("10.0.2.2", 12345, "Viktor")

    init {
        viewModelScope.launch {
            try {
                client.connect()
                client.receiveMessages { message ->
                    _messages.value = _messages.value + message
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + "Connection failed: ${e.message}"
                Log.e("ChatViewModel", "Error connecting to server: ${e.message}")
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                if (client.isConnected()) {
                    client.sendMessage(message)
                } else {
                    _messages.value = _messages.value + "Failed to send message: Socket is not connected."
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + "Failed to send message: ${e.message}"
            }
        }
    }
}



