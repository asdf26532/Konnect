package com.han.konnect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.han.konnect.data.entity.CorrectionEntity
import com.han.konnect.ui.theme.PurpleMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorrectionDetailModal(
    correction: CorrectionEntity,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 헤더: 이름 & 태그
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${correction.userName} 님의 피드백",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CorrectionTagChip(correctionType = correction.correctionType)
                }
            }

            Divider(color = Color(0xFFEEEEEE))

            // 원문 섹션
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("❌ 수정 전 (Original)", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = correction.originalText,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        color = Color(0xFFC62828)
                    )
                }
            }

            // 교정문 섹션
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("✅ 수정 후 (Corrected)", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = correction.correctedText,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            // 교정 이유/설명
            if (correction.reason.isNotBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("💡 AI & 튜터의 피드백", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = correction.reason,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 삭제 & 닫기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onDelete()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("삭제하기")
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(2f),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
                ) {
                    Text("확인")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}