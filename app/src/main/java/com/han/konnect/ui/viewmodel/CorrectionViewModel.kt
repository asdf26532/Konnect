package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.han.konnect.data.entity.CorrectionEntity
import com.han.konnect.data.model.AICorrectionResult
import com.han.konnect.data.repository.CorrectionRepository
import com.han.konnect.data.repository.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 💡 Gemini AI 분석 상태 Sealed Interface
sealed interface AIState {
    object Idle : AIState
    object Loading : AIState
    data class Success(val data: AICorrectionResult) : AIState
    data class Error(val message: String) : AIState
}

class CorrectionViewModel(
    private val repository: CorrectionRepository,
    private val geminiRepository: GeminiRepository = GeminiRepository()
) : ViewModel() {

    val allCorrections: StateFlow<List<CorrectionEntity>> = repository.allCorrections
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _aiState = MutableStateFlow<AIState>(AIState.Idle)
    val aiState: StateFlow<AIState> = _aiState.asStateFlow()

    fun analyzeTextWithAI(originalText: String) {
        viewModelScope.launch {
            _aiState.value = AIState.Loading
            val result = geminiRepository.analyzeCorrection(originalText)

            result.onSuccess { correctionResult ->
                _aiState.value = AIState.Success(correctionResult)
            }.onFailure { error ->
                _aiState.value = AIState.Error(error.localizedMessage ?: "AI 분석 실패")
            }
        }
    }

    fun resetAIState() {
        _aiState.value = AIState.Idle
    }

    fun saveCorrection(userUid: String, correction: CorrectionEntity) {
        viewModelScope.launch {
            repository.insertCorrection(userUid, correction)
        }
    }

    fun insertCorrection(correction: CorrectionEntity) {
        viewModelScope.launch {
            repository.insert(correction)
        }
    }

    fun deleteCorrection(correction: CorrectionEntity) {
        viewModelScope.launch {
            repository.delete(correction)
        }
    }
}