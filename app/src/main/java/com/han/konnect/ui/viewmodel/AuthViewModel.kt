package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.konnect.data.model.UserProfile
import com.han.konnect.data.repository.AuthRepository
import com.han.konnect.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val profile: UserProfile) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun registerProfile(
        name: String,
        nativeLanguage: String,
        targetLanguage: String,
        bio: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            val authResult = authRepository.signInAnonymously()

            authResult.onSuccess { user ->
                // 2. UserProfile 생성
                val profile = UserProfile(
                    uid = user.uid,
                    name = name.ifBlank { "학습자_${user.uid.take(4)}" },
                    nativeLanguage = nativeLanguage,
                    targetLanguage = targetLanguage,
                    bio = bio,
                    interests = listOf("K-POP", "드라마", "여행")
                )

                val saveResult = userRepository.saveUserProfile(profile)
                saveResult.onSuccess {
                    _uiState.value = AuthUiState.Success(profile)
                }.onFailure { e ->
                    _uiState.value = AuthUiState.Error("프로필 저장 실패: ${e.localizedMessage}")
                }
            }.onFailure { e ->
                _uiState.value = AuthUiState.Error("인증 실패: ${e.localizedMessage}")
            }
        }
    }
}