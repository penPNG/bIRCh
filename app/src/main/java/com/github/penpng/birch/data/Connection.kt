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
        "MODE",
        "ERROR",
        "PART",
        "QUIT"
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

    fun parseChat(fullMessage: String?) {
        if (fullMessage == null) return
        println(fullMessage)
        var messageSplit: List<String> = fullMessage.split(" :")
        val command: String
        val message: String
        if (messageSplit.size > 1) {
            if (messageSplit[0].split(" ").size > 1) { command = messageSplit[0].split(" ")[1] }
            else { command = messageSplit[0] }
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
                        0 -> { //PRIVMSG
                            viewModel.updateChat(messageSplit[0].split("!")[0].substring(1) + ": " + message)
                            println(messageSplit[0].split("!")[0].substring(1) + ": " + message)
                            return@post
                        }

                        1 -> { //PING
                            Thread(Runnable {sender.println(
                                "PONG " + message)}).start()
                            println("PONG " + message)

                            return@post
                        }

                        3 -> { //JOIN
                            if (messageSplit[0].split("!")[0].substring(1)==viewModel.getNick()) {
                                viewModel.updateChat("Now talking on "+message)
                                println("Now talking on "+message)
                            } else {
                                viewModel.updateChat(messageSplit[0].split("!")[0].substring(1) + " has joined")
                                println(messageSplit[0].split("!")[0].substring(1) + " has joined")
                            }
                            return@post
                        }

                        5 -> { //ERROR
                            disconnect()
                            return@post
                        }

                        6 -> { //PART
                            viewModel.updateChat(messageSplit[0].split("!")[0].substring(1) + " has left (" + message+")")
                            println(messageSplit[0].split("!")[0].substring(1) + " has left (" + message+")")
                            return@post
                        }

                        7 -> { //QUIT
                            viewModel.updateChat(messageSplit[0].split("!")[0].substring(1) + " has quit (" + message+")")
                            println(messageSplit[0].split("!")[0].substring(1) + " has quit (" + message+")")
                            return@post
                        }

                        2, 4 -> return@post
                    }
                }
            }
            when (command.toInt()) {
                353 -> {
                    viewModel.updateUsers(message.split(" ".toRegex()))
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
            if (message.startsWith("/")) {
                sender.println(message.substring(1))
            }else {
                sender.println("PRIVMSG #test :" + message)
            }
        }).start()
    }

    fun disconnect() {
        connected = false
        server.close()
        sender.close()
        receiver.close()
    }

}