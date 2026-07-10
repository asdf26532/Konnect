package com.han.konnect.data.model

data class User(
    val id: String,
    val name: String,
    val age: Int,
    val country: String,
    val profileImageUrl: String,
    val bio: String,
    val koreanLevel: String,
    val interests: List<String>
)
