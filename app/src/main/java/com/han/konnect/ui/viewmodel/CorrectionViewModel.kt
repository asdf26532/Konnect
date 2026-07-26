package com.han.konnect.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.han.konnect.data.db.AppDatabase
import com.han.konnect.data.entity.CorrectionEntity
import com.han.konnect.data.model.AICorrectionResponse
import com.han.konnect.data.repository.AIRepository
import com.han.konnect.data.repository.CorrectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AIState {
    object Idle : AIState
    object Loading : AIState
    data class Success(val data: AICorrectionResponse) : AIState
    data class Error(val message: String) : AIState
}

class CorrectionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = CorrectionRepository(db.correctionDao())
    private val aiRepository = AIRepository()

    val allCorrections = repository.allCorrections

    private val _aiState = MutableStateFlow<AIState>(AIState.Idle)
    val aiState: StateFlow<AIState> = _aiState.asStateFlow()

    fun insertCorrection(correction: CorrectionEntity) {
        viewModelScope.launch {
            repository.insertCorrection(correction)
        }
    }

    fun deleteCorrection(correction: CorrectionEntity) {
        viewModelScope.launch {
            repository.deleteCorrection(correction)
        }
    }

    fun requestAICorrection(originalText: String) {
        viewModelScope.launch {
            _aiState.value = AIState.Loading
            val result = aiRepository.fetchAICorrection(originalText)
            result.onSuccess { response ->
                _aiState.value = AIState.Success(response)
            }.onFailure { error ->
                _aiState.value = AIState.Error(error.localizedMessage ?: "알 수 없는 오류 발생")
            }
        }
    }

    fun resetAIState() {
        _aiState.value = AIState.Idle
    }
}