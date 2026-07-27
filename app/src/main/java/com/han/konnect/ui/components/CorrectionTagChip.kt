package com.han.konnect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CorrectionTagChip(
    correctionType: String,
    modifier: Modifier = Modifier
) {
    val (label, backgroundColor, textColor) = when (correctionType.uppercase()) {
        "GRAMMAR" -> Triple("문법 오류", Color(0xFFFFEBEE), Color(0xFFD32F2F)) // 연분홍 / 빨강
        "SPELLING" -> Triple("맞춤법", Color(0xFFFFF8E1), Color(0xFFF57F17)) // 연노랑 / 주황
        "NATURAL_EXPRESSION" -> Triple("어색한 표현", Color(0xE8E3F2FD), Color(0xFF1976D2)) // 연파랑 / 파랑
        else -> Triple("문장 교정", Color(0xFFF3E5F5), Color(0xFF7B1FA2)) // 연보라 / 보라
    }

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}