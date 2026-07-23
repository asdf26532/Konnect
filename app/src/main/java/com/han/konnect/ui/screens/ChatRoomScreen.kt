package com.han.konnect.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.han.konnect.data.model.ChatMessage
import com.han.konnect.ui.components.MessageBubble
import com.han.konnect.ui.theme.PurpleMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    userName: String = "Sarah",
    onBackClick: () -> Unit = {})
{
    val context = LocalContext.current

    val messages = remember {
        mutableStateListOf(
            ChatMessage("1", "other", "안녕하세요! 한국어 공부하고 싶어요.", "오후 5:10"),
            ChatMessage("2", "me", "반가워요 사라이라 해요! 어떤 드라마 좋아해요?", "오후 5:11"),
            ChatMessage("3", "other", "나는 이태원 클라쓰 좋아해요! 방금 보낸 한글 문장 맞게 고친 거야? 🧐", "오후 5:12")
        )
    }

    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 4.dp,
                modifier = Modifier.imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("메시지를 입력하세요...", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF1F5F9),
                            unfocusedContainerColor = Color(0xFFF1F5F9),
                            disabledContainerColor = Color(0xFFF1F5F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = {
                            if (inputText.trim().isNotEmpty()) {
                                messages.add(ChatMessage(
                                    id = System.currentTimeMillis().toString(),
                                    senderId = "me",
                                    text = inputText,
                                    timestamp = "방금"
                                ))
                                inputText = ""
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = PurpleMain)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "전송")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    onLongClick = {
                        Toast.makeText(context, "💡 문장 복사 및 AI 한국어 교정 분석을 준비 중입니다!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}