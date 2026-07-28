package com.han.konnect.data.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val nativeLanguage: String = "Korean",
    val targetLanguage: String = "English",
    val bio: String = "",
    val interests: List<String> = emptyList(),
    val rating: Double = 5.0,
    val createdAt: Long = System.currentTimeMillis()
)