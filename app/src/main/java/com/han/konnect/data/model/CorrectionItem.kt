package com.han.konnect.data.model

data class CorrectionItem(
    val id: String,
    val originalText: String,
    val correctedText: String,
    val reason: String,
    val userName: String,
    val timestamp: String
)