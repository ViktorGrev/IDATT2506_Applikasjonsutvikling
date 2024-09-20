package stud.ntnu.viktor.oppgave6

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatClient(private val host: String, private val port: Int, private val username: String) {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null

    suspend fun connect() {
        withContext(Dispatchers.IO) {
            try {
                socket = Socket(host, port)
                writer = PrintWriter(socket!!.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                Log.e("ChatClient", "Connected to server at $host:$port")
            } catch (e: Exception) {
                Log.e("ChatClient", "Failed to connect to server: ${e.message}")
            }
        }
    }

    fun isConnected(): Boolean {
        return socket?.isConnected == true && socket?.isClosed == false
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            Log.d("ChatClient", "Sending message: $message")
            writer?.let {
                val formattedMessage = "$username: $message"
                it.println(formattedMessage)
                it.flush()
                Log.d("ChatClient", "Message sent successfully")
            } ?: throw IllegalStateException("Socket is not connected.")
        }
    }

    suspend fun receiveMessages(callback: (String) -> Unit) {
        withContext(Dispatchers.IO) {
            reader?.let {
                var message: String?
                while (it.readLine().also { message = it } != null) {
                    Log.d("ChatClient", "Message received: $message")
                    callback(message!!)
                }
            } ?: throw IllegalStateException("Socket is not connected.")
        }
    }
}


