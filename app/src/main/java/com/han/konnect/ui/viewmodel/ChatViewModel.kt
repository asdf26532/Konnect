package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.konnect.data.model.FirestoreChatMessage
import com.han.konnect.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ChatUiState {
    object Loading : ChatUiState
    data class Success(val messages: List<FirestoreChatMessage>) : ChatUiState
    data class Error(val message: String) : ChatUiState
}

class ChatViewModel : ViewModel() {

    private val chatRepository = ChatRepository()

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun observeMessages(roomId: String) {
        viewModelScope.launch {
            _uiState.value = ChatUiState.Loading
            chatRepository.getRealtimeMessages(roomId).collect { messages ->
                _uiState.value = ChatUiState.Success(messages)
            }
        }
    }

    fun sendMessage(roomId: String, senderUid: String, text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            val message = FirestoreChatMessage(
                senderUid = senderUid,
                text = text,
                timestamp = System.currentTimeMillis()
            )
            chatRepository.sendMessage(roomId, message)
        }
    }
}