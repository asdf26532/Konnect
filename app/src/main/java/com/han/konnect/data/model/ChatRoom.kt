package com.han.konnect.data.model

data class ChatRoom(
    val id: String,
    val userName: String,
    val userProfileUrl: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int
)