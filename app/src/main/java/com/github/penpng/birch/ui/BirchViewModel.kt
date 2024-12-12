package com.github.penpng.birch.ui

import androidx.lifecycle.ViewModel
import com.github.penpng.birch.data.BirchUIState
import com.github.penpng.birch.data.Connection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BirchViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(BirchUIState(chat = StringBuilder("")))
    val uiState: StateFlow<BirchUIState> = _uiState.asStateFlow()

    fun createConnection(c: Connection) {
        _uiState.update { currentState ->
            currentState.copy(
                connection = c
            )
        }
    }

    fun updateChat(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                chat = currentState.chat.append(message+"\n")
            )
        }
    }

    fun updateNick(nickname: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nickname = nickname
            )
        }
    }

    fun updateServer(server: String) {
        _uiState.update { currentState ->
            currentState.copy(
                server = server
            )
        }
    }

    fun sendMessage(message: String) {
        uiState.value.connection?.sendMessage(message)
    }

    fun getServer() : String {
        return uiState.value.server
    }

    fun getNick() : String {
        return uiState.value.nickname
    }

    fun getChat() : String {
        return uiState.value.chat.toString()
    }

    fun closeConnection() {
        uiState.value.connection?.disconnect()
    }

    fun clearChat() {
        _uiState.update { currentState ->
            currentState.copy(
                chat = StringBuilder("")
            )
        }
    }


}