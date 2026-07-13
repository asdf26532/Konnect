package com.han.konnect.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.han.konnect.data.model.ChatMessage
import com.han.konnect.ui.theme.PurpleMain

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: ChatMessage,
    onLongClick: () -> Unit
) {
    val isMe = message.senderId == "me"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        if (!isMe) {
            MessageCard(message = message, isMe = isMe, onLongClick = onLongClick)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = message.timestamp, fontSize = 10.sp, color = Color(0xFF94A3B8))
        } else {
            // [나일 때] 시간 표시가 왼쪽에 먼저 붙음
            Text(text = message.timestamp, fontSize = 10.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.width(6.dp))
            MessageCard(message = message, isMe = isMe, onLongClick = onLongClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(message: ChatMessage, isMe: Boolean, onLongClick: () -> Unit) {
    Surface(
        color = if (isMe) PurpleMain else Color(0xFFF1F5F9),
        contentColor = if (isMe) Color.White else Color(0xFF0F172A),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = if (isMe) 16.dp else 0.dp,
            bottomEnd = if (isMe) 0.dp else 16.dp
        ),
        modifier = Modifier
            .widthIn(max = 260.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { onLongClick() }
            )
    ) {
        Text(
            text = message.text,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            lineHeight = 20.sp
        )
    }
}