package com.han.konnect.data.model

data class FirestoreChatMessage(
    val messageId: String = "",
    val senderUid: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isCorrected: Boolean = false,
    val correctedText: String? = null
)