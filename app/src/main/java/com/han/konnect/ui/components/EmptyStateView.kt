package com.han.konnect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.han.konnect.ui.theme.PurpleMain

@Composable
fun EmptyStateView(
    icon: ImageVector = Icons.Default.Info,
    title: String = "데이터가 없습니다",
    description: String = "새로운 콘텐츠가 추가되면 이곳에 표시됩니다.",
    retryText: String? = null,
    onRetryClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Gray.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (retryText != null && onRetryClick != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onRetryClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
                ) {
                    Text(retryText, color = Color.White)
                }
            }
        }
    }
}