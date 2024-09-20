package stud.ntnu.viktor.oppgave6tjener

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

class ChatServer(private val port: Int) {
    private val clients = ConcurrentHashMap<Socket, PrintWriter>()

    suspend fun startServer() {
        withContext(Dispatchers.IO) {
            val localIp = InetAddress.getLocalHost().hostAddress
            val serverSocket = ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"))
            Log.e("ChatServer", "Server started on IP $localIp and port $port")

            while (true) {
                val clientSocket = serverSocket.accept()
                Log.e("ChatServer", "Client connected: ${clientSocket.inetAddress.hostAddress}")

                val writer = PrintWriter(clientSocket.getOutputStream(), true)
                clients[clientSocket] = writer

                launch {
                    handleClient(clientSocket)
                }
            }
        }
    }

    private suspend fun handleClient(socket: Socket) {
        withContext(Dispatchers.IO) {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            var message: String?

            try {
                while (reader.readLine().also { message = it } != null) {
                    Log.e("ChatServer", "Received: $message from ${socket.inetAddress.hostAddress}")
                    broadcastMessage(message!!)
                }
            } catch (e: Exception) {
                Log.e("ChatServer", "Error: ${e.message}")
            } finally {
                clients.remove(socket)
                socket.close()
            }
        }
    }


    private fun broadcastMessage(message: String) {
        for (client in clients.values) {
            client.println(message)
        }
    }
}