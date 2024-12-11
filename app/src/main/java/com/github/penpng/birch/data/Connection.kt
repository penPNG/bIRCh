package com.github.penpng.birch.data

import android.os.Handler
import android.os.Looper
import androidx.datastore.core.IOException
import com.github.penpng.birch.ui.BirchViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executor


class Connection(val nickname: String, val serverName: String, val viewModel: BirchViewModel) {
    lateinit var server: Socket
    lateinit var sender: PrintWriter
    lateinit var receiver: BufferedReader
    var connected: Boolean = false
    var mainHandler: Handler = Handler(Looper.getMainLooper())
    lateinit var mainExecutor: Executor
    val COMMANDS: List<String> = mutableListOf(
        "PRIVMSG",
        "PING",
        "NOTICE",
        "JOIN",
        "MODE"
    )

    init {
        try {
            Thread(Runnable {
                server = Socket(serverName, 6667)
                println("connection")
                sender = PrintWriter(server.getOutputStream(), true)
                println("sender")
                receiver =
                    BufferedReader(InputStreamReader(server.getInputStream()))
                println("receiver")
                sender.println("NICK "+viewModel.getNick());
                sender.println("USER "+viewModel.getNick()+" * * :realname")
                sender.println("JOIN #test")
                connected = true
                println("connected")
                getChat()
            }).start()
        } catch (_: IOException) {
            println("Connection Lost")
        }
    }


    fun getChat() {
        var text: String
        try {
            while (connected) {
                //text = receiver.readLine()
                    parseChat(receiver.readLine())
                    //println(text)
            }
        } catch (_: IOException) {
            println("Connection Closed")
        }

    }

    fun parseChat(fullMessage: String) {
        println(fullMessage)
        var messageSplit: List<String> = fullMessage.split(" :")
        val command: String
        val message: String
        if (messageSplit.size > 1) {
            command = messageSplit[0].split(" ")[1];
            message = messageSplit[1]
        } else {
            messageSplit = fullMessage.split(" ")
            command = messageSplit[1]
            message = messageSplit[2]
        }
        viewModel.uiState.value.mainHandler.post {
        //mainExecutor = Executor() {

            for (cmd in COMMANDS) {
                if (command.contains(cmd)) {
                    when (COMMANDS.indexOf(cmd)) {
                        0 -> {
                            viewModel.updateChat(messageSplit[0].split("!")[0].substring(1) + ": " + message)
                            println(messageSplit[0].split("!")[0].substring(1) + ": " + message)
                            return@post
                        }

                        1 -> {
                            sender.println(
                                "PONG " + command.split(" ")[0].substring(1))
                            return@post
                        }

                        2, 3, 4 -> return@post
                    }
                }
            }
            when (command.toInt()) {
                353 -> {
                    //users = message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    //chatPanel.setUserList(users)
                    return@post
                }

                372, 375, 376 -> {
                    viewModel.updateChat(message)
                    println(message)
                    return@post
                }

                366 -> return@post
                else -> {}
            }
        }
    }

    fun sendMessage(message: String) {
        Thread(Runnable {
            sender.println("PRIVMSG #test :"+message)
        }).start()
    }

    fun disconnect() {
        connected = false
        server.close()
        sender.close()
        receiver.close()
    }

}