package com.han.konnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.han.konnect.data.model.UserProfile
import com.han.konnect.ui.components.PartnerCard
import com.han.konnect.ui.theme.PurpleMain
import com.han.konnect.ui.viewmodel.MatchingUiState
import com.han.konnect.ui.viewmodel.MatchingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchingScreen(
    currentUid: String = "my_uid_123",
    onStartChat: (UserProfile) -> Unit = {},
    viewModel: MatchingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedFilter by viewModel.selectedLanguageFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val languageFilters = listOf("전체", "English", "Japanese", "Chinese", "Spanish")

    LaunchedEffect(currentUid) {
        viewModel.fetchRecommendedPartners(currentUid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("파트너 찾기 🌐", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("이름, 관심사, 소개글 검색...", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "검색") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                items(languageFilters) { language ->
                    FilterChip(
                        selected = selectedFilter == language,
                        onClick = { viewModel.onLanguageFilterSelected(language) },
                        label = { Text(language) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurpleMain,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is MatchingUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is MatchingUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is MatchingUiState.Success -> {
                        if (state.users.isEmpty()) {
                            Text(
                                text = "조건에 맞는 언어 교환 파트너가 없습니다 🥲",
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(state.users, key = { it.uid }) { user ->
                                    PartnerCard(
                                        user = user,
                                        onStartChatClick = { onStartChat(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}