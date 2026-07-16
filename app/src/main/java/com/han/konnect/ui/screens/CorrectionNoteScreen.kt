package com.han.konnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.han.konnect.data.model.CorrectionItem
import com.han.konnect.ui.components.CorrectionCard
import com.han.konnect.ui.theme.PurpleMain

@Composable
fun CorrectionNoteScreen() {
    val dummyCorrections = listOf(
        CorrectionItem(
            id = "1",
            originalText = "반가워요 사라이라 해요! 어떤 드라마 좋아해요?",
            correctedText = "반가워요! 저는 사라라고 해요. 어떤 드라마를 좋아해요?",
            reason = "조사 및 띄어쓰기",
            userName = "Sarah",
            timestamp = "오늘 오후 5:15"
        ),
        CorrectionItem(
            id = "2",
            originalText = "내일 홍대 맛집 추천해 줄 수 있어?",
            correctedText = "내일 홍대에 있는 맛집을 추천해 줄 수 있어?",
            reason = "자연스러운 표현",
            userName = "Yuki",
            timestamp = "어제 오후 2:35"
        ),
        CorrectionItem(
            id = "3",
            originalText = "오늘 손흥민 경기 대박이었어",
            correctedText = "오늘 손흥민 선수 경기는 정말 대박이었어!",
            reason = "명사 수식 오류",
            userName = "James",
            timestamp = "2026-07-14"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "tripmate",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = PurpleMain
            )
            Text(
                text = " 교정 노트",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(dummyCorrections) { item ->
                CorrectionCard(item = item)
            }
        }
    }
}