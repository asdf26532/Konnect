package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.konnect.data.model.UserProfile
import com.han.konnect.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface MatchingUiState {
    object Loading : MatchingUiState
    data class Success(val users: List<UserProfile>) : MatchingUiState
    data class Error(val message: String) : MatchingUiState
}

class MatchingViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<MatchingUiState>(MatchingUiState.Loading)
    val uiState: StateFlow<MatchingUiState> = _uiState.asStateFlow()

    private val _selectedLanguageFilter = MutableStateFlow("전체")
    val selectedLanguageFilter: StateFlow<String> = _selectedLanguageFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allRecommendedUsers = listOf<UserProfile>()

    // 추천 파트너 불러오기
    fun fetchRecommendedPartners(currentUid: String) {
        viewModelScope.launch {
            _uiState.value = MatchingUiState.Loading

            // Dummy 샘플 포함 테스트용 (Firestore 연결 실패 시 기본값 리턴용)
            val result = userRepository.getRecommendedUsers(currentUid)

            result.onSuccess { users ->
                allRecommendedUsers = if (users.isEmpty()) getDummyPartners() else users
                applyFilters()
            }.onFailure {
                allRecommendedUsers = getDummyPartners()
                applyFilters()
            }
        }
    }

    fun onLanguageFilterSelected(language: String) {
        _selectedLanguageFilter.value = language
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    private fun applyFilters() {
        var filtered = allRecommendedUsers

        // 1. 언어 필터링
        if (_selectedLanguageFilter.value != "전체") {
            filtered = filtered.filter { it.nativeLanguage == _selectedLanguageFilter.value }
        }

        // 2. 검색어 필터링
        if (_searchQuery.value.isNotBlank()) {
            val q = _searchQuery.value.lowercase()
            filtered = filtered.filter {
                it.name.lowercase().contains(q) ||
                        it.bio.lowercase().contains(q) ||
                        it.interests.any { tag -> tag.lowercase().contains(q) }
            }
        }

        _uiState.value = MatchingUiState.Success(filtered)
    }

    private fun getDummyPartners(): List<UserProfile> {
        return listOf(
            UserProfile(
                uid = "user_sarah",
                name = "Sarah Johnson",
                nativeLanguage = "English",
                targetLanguage = "Korean",
                bio = "안녕하세요! 한국 드라마와 K-POP을 좋아하는 대학생입니다 🇰🇷🇺🇸",
                interests = listOf("K-POP", "드라마", "카페"),
                rating = 4.9
            ),
            UserProfile(
                uid = "user_kenji",
                name = "Kenji Sato",
                nativeLanguage = "Japanese",
                targetLanguage = "Korean",
                bio = "한국 음식과 여행에 관심이 많아요. 같이 한국어 연습해요!",
                interests = listOf("요리", "여행", "사진"),
                rating = 4.8
            ),
            UserProfile(
                uid = "user_emily",
                name = "Emily Chen",
                nativeLanguage = "English",
                targetLanguage = "Korean",
                bio = "Looking for a language exchange buddy! Let's study together.",
                interests = listOf("영화", "독서", "패션"),
                rating = 5.0
            )
        )
    }
}