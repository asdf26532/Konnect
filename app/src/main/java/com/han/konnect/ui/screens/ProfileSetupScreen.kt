package com.han.konnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.han.konnect.ui.theme.PurpleMain
import com.han.konnect.ui.viewmodel.AuthUiState
import com.han.konnect.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onSetupComplete: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var nativeLanguage by remember { mutableStateOf("English") }
    var targetLanguage by remember { mutableStateOf("Korean") }
    var bio by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onSetupComplete()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필 설정", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌐 언어 교환을 위한 프로필을 만들어주세요",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름 / 닉네임") },
                placeholder = { Text("예: Sarah") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = nativeLanguage,
                onValueChange = { nativeLanguage = it },
                label = { Text("모국어 (Native Language)") },
                placeholder = { Text("예: English, Korean") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = targetLanguage,
                onValueChange = { targetLanguage = it },
                label = { Text("배우고 싶은 언어 (Target Language)") },
                placeholder = { Text("예: Korean, English") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("한 줄 자기소개") },
                placeholder = { Text("한국어 친구를 구하고 있어요!") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )

            if (uiState is AuthUiState.Error) {
                Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.registerProfile(
                        name = name,
                        nativeLanguage = nativeLanguage,
                        targetLanguage = targetLanguage,
                        bio = bio
                    )
                },
                enabled = name.isNotBlank() && uiState !is AuthUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleMain)
            ) {
                if (uiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("시작하기 🚀", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}