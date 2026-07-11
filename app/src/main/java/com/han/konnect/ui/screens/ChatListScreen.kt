package com.han.konnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.han.konnect.data.model.ChatRoom
import com.han.konnect.ui.components.ChatRoomItem
import com.han.konnect.ui.theme.PurpleMain

@Composable
fun ChatListScreen() {
    val dummyChatRooms = listOf(
        ChatRoom(
            id = "1",
            userName = "Sarah",
            userProfileUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=500&q=80",
            lastMessage = "방금 보낸 한글 문장 맞게 고친 거야? 🧐",
            lastMessageTime = "오후 5:12",
            unreadCount = 2
        ),
        ChatRoom(
            id = "2",
            userName = "Yuki",
            userProfileUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=500&q=80",
            lastMessage = "고마워! 내일 홍대 맛집 추천해 줄 수 있어?",
            lastMessageTime = "오후 2:30",
            unreadCount = 0
        ),
        ChatRoom(
            id = "3",
            userName = "James",
            userProfileUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=500&q=80",
            lastMessage = "오늘 손흥민 경기 대박이었어 ⚽🔥",
            lastMessageTime = "어제",
            unreadCount = 0
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "konnect",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = PurpleMain
            )
            Text(
                text = " 채팅",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(dummyChatRooms) { room ->
                ChatRoomItem(
                    chatRoom = room,
                    onClick = {
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    thickness = 0.5.dp,
                    color = Color(0xFFF1F5F9)
                )
            }
        }
    }
}