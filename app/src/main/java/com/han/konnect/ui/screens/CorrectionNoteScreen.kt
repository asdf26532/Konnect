package com.han.konnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.han.konnect.data.entity.CorrectionEntity
import com.han.konnect.ui.theme.PurpleMain
import com.han.konnect.ui.viewmodel.CorrectionViewModel

@Composable
fun CorrectionNoteScreen(
    viewModel: CorrectionViewModel = viewModel()
) {
    val correctionList by viewModel.corrections.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // 1. 헤더 타이틀 Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "konnect",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = PurpleMain
            )
            Text(
                text = " 오답노트",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        // 2. 오답 카드 리스트 (DB 데이터 렌더링)
        if (correctionList.isEmpty()) {
            // DB가 비어있을 때 표시할 빈 화면 state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "저장된 교정 노트가 없습니다. \n친구들과 대화하며 문장을 교정해보세요!",
                    color = Color(0xFF94A3B8),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = correctionList,
                    key = { item -> item.id } // LazyColumn 성능 최적화를 위한 키 설정
                ) { item ->
                    CorrectionItemCard(
                        item = item,
                        onDeleteClick = { viewModel.deleteCorrection(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun CorrectionItemCard(
    item: CorrectionEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFEEF2FF),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.reason,
                            color = PurpleMain,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "by ${item.userName}",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "❌  ${item.originalText}",
                fontSize = 15.sp,
                color = Color(0xFFEF4444),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "✅  ${item.correctedText}",
                fontSize = 15.sp,
                color = Color(0xFF10B981),
                fontWeight = FontWeight.Bold
            )
        }
    }
}