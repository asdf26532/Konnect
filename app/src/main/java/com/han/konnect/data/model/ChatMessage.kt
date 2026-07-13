package com.han.konnect.data.model

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isCorrected: Boolean = false
)