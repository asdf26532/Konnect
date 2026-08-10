package com.han.konnect.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.han.konnect.data.entity.CorrectionEntity
import com.han.konnect.data.model.ChatMessage
import com.han.konnect.ui.components.MessageBubble
import com.han.konnect.ui.theme.PurpleMain
import com.han.konnect.ui.components.CorrectionInputDialog
import com.han.konnect.ui.viewmodel.CorrectionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    userName: String = "Sarah",
    onBackClick: () -> Unit = {},
    viewModel: CorrectionViewModel = viewModel()
){
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val audioRecorder = remember { AudioRecorder(context) }
    var isRecording by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage("1", "other", "안녕하세요! 한국어 공부하고 싶어요.", "오후 5:10"),
            ChatMessage("2", "me", "반가워요 사라이라 해요! 어떤 드라마 좋아해요?", "오후 5:11"),
            ChatMessage("3", "other", "나는 이태원 클라쓰 좋아해요! 방금 보낸 한글 문장 맞게 고친 거야? 🧐", "오후 5:12")
        )
    }

    var inputText by remember { mutableStateOf("") }
    var selectedMessageForCorrection by remember { mutableStateOf<ChatMessage?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
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
                    if (isRecording) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔴 음성 녹음 중...",
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        val recordedFile = audioRecorder.stopRecording()
                                        isRecording = false
                                        recordedFile?.let {
                                            messages.add(
                                                ChatMessage(
                                                    id = System.currentTimeMillis().toString(),
                                                    senderId = "me",
                                                    text = "🎤 음성 메시지",
                                                    timestamp = "방금"
                                                )
                                            )
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Stop, contentDescription = "녹음 전송", tint = Color.Red)
                                }
                            }
                        }
                    } else {
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
                        Spacer(modifier = Modifier.width(8.dp))

                        if (inputText.isBlank()) {
                            IconButton(
                                onClick = {
                                    isRecording = true
                                    audioRecorder.startRecording()
                                },
                                colors = IconButtonDefaults.iconButtonColors(contentColor = PurpleMain)
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = "음성 녹음")
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    messages.add(
                                        ChatMessage(
                                            id = System.currentTimeMillis().toString(),
                                            senderId = "me",
                                            text = inputText.trim(),
                                            timestamp = "방금"
                                        )
                                    )
                                    inputText = ""
                                },
                                colors = IconButtonDefaults.iconButtonColors(contentColor = PurpleMain)
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "전송")
                            }
                        }
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
                        if (message.senderId != "me") {
                            selectedMessageForCorrection = message
                        } else {
                            Toast.makeText(context, "내가 보낸 문장입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }

    selectedMessageForCorrection?.let { message ->
        CorrectionInputDialog(
            originalText = message.text,
            userName = userName,
            onDismiss = { selectedMessageForCorrection = null },
            onSubmit = { correctedText, reason ->
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                viewModel.insertCorrection(
                    CorrectionEntity(
                        originalText = message.text,
                        correctedText = correctedText,
                        reason = reason,
                        userName = userName,
                        timestamp = System.currentTimeMillis()
                    )
                )
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "✏️ 오답노트에 성공적으로 추가되었습니다!",
                        duration = SnackbarDuration.Short
                    )
                }

                selectedMessageForCorrection = null
            }
        )
    }
}