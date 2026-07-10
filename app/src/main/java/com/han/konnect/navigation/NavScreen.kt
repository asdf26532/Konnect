package com.han.konnect.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreen(val route: String, val title: String, val icon: ImageVector) {
    object Discovery : NavScreen("discovery", "발견", Icons.Default.Search)
    object Chat : NavScreen("chat", "채팅", Icons.Default.ChatBubble)
    object Correction : NavScreen("correction", "교정 노트", Icons.Default.CheckCircle)
    object MyPage : NavScreen("mypage", "마이페이지", Icons.Default.Person)
}