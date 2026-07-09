package com.han.konnect.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    object Discovery : NavigationItem("discovery", "발견", Icons.Default.Search)
    object Chat : NavigationItem("chat", "채팅", Icons.Default.ChatBubble)
    object Correction : NavigationItem("correction", "교정 노트", Icons.Default.CheckCircle)
    object MyPage : NavigationItem("mypage", "마이페이지", Icons.Default.Person)
}