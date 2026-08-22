package com.han.konnect.data.model

data class RoleplayMessage(
    val id: String,
    val sender: String,
    val text: String,
    val translation: String? = null
)