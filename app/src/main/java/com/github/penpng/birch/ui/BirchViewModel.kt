package com.github.penpng.birch.ui

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.github.penpng.birch.data.BirchUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BirchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BirchUIState(chat = StringBuilder("")))
    val uiState: StateFlow<BirchUIState> = _uiState.asStateFlow()

    fun updateChat(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                chat = currentState.chat.append(message+"\n")
            )
        }
    }


}