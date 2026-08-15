package com.han.konnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.han.konnect.ui.theme.PurpleMain
import com.han.konnect.ui.viewmodel.ChallengeViewModel

@Composable
fun DailyChallengeScreen(
    viewModel: ChallengeViewModel = viewModel()
) {
    val streakCount by viewModel.streakCount.collectAsState()
    val userPoints by viewModel.userPoints.collectAsState()
    val challenge = viewModel.todayChallenge
    val selectedOptionId by viewModel.selectedOptionId.collectAsState()
    val isSubmitted by viewModel.isSubmitted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFFE4E6))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFE11D48))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${streakCount}일 연속 달성!", color = Color(0xFFE11D48), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFD97706))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${userPoints} P", color = Color(0xFFD97706), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PurpleMain),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "🔥 오늘의 표현",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = challenge.expression,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = challenge.meaning,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "💬 \"${challenge.exampleSentence}\"",
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("💡 AI Daily Quiz", color = PurpleMain, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(challenge.question, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(16.dp))

                challenge.options.forEach { option ->
                    val isSelected = selectedOptionId == option.id

                    val borderColor = when {
                        isSubmitted && option.isCorrect -> Color(0xFF22C55E)
                        isSubmitted && isSelected && !option.isCorrect -> Color(0xFFEF4444)
                        isSelected -> PurpleMain
                        else -> Color(0xFFE2E8F0)
                    }

                    val bgColor = when {
                        isSubmitted && option.isCorrect -> Color(0xFFDCFCE7)
                        isSubmitted && isSelected && !option.isCorrect -> Color(0xFFFEE2E2)
                        isSelected -> PurpleMain.copy(alpha = 0.1f)
                        else -> Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                            .clickable { viewModel.selectOption(option.id) }
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${option.id}. ${option.text}",
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSubmitted && option.isCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF22C55E))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.submitAnswer() },
                    enabled = selectedOptionId != null && !isSubmitted,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleMain),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = if (isSubmitted) "제출 완료" else "정답 확인하기",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}