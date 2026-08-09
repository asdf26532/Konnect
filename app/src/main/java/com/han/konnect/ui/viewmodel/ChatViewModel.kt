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

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

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

    fun sendVoiceMessage(
        chatRoomId: String,
        senderUid: String,
        audioFile: File
    ) {
        val fileUri = Uri.fromFile(audioFile)
        val storageRef = storage.reference.child("chat_voices/${UUID.randomUUID()}.mp3")

        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val message = hashMapOf(
                        "senderUid" to senderUid,
                        "content" to "🎤 음성 메시지",
                        "audioUrl" to downloadUrl.toString(),
                        "type" to "VOICE", // 메시지 타입 (TEXT / VOICE)
                        "timestamp" to Timestamp.now()
                    )

                    db.collection("chatRooms")
                        .document(chatRoomId)
                        .collection("messages")
                        .add(message)
                }
            }
            .addOnFailureListener {
            }
    }
}