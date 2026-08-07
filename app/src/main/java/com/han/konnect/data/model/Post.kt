package com.han.konnect.data.model

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val authorUid: String = "",
    val authorName: String = "",
    val authorProfileUrl: String = "",
    val content: String = "",
    val category: String = "일반",
    val imageUrl: String = "",
    val likeCount: Int = 0,
    val likedBy: List<String> = emptyList(),
    val commentCount: Int = 0,
    val timestamp: Timestamp = Timestamp.now()
)

data class Comment(
    val id: String = "",
    val authorUid: String = "",
    val authorName: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now()
)