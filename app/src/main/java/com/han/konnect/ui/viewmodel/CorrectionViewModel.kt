package com.han.konnect.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.han.konnect.data.AppDatabase
import com.han.konnect.data.entity.CorrectionEntity
import com.han.konnect.data.repository.CorrectionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CorrectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CorrectionRepository

    val corrections: StateFlow<List<CorrectionEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).correctionDao()
        repository = CorrectionRepository(dao)

        corrections = repository.allCorrections
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        seedInitialDataIfEmpty()
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

    private fun seedInitialDataIfEmpty() {
        viewModelScope.launch {
            // 초기 1회성 더미 데이터 삽입 (동작 확인용)
            if (corrections.value.isEmpty()) {
                insertCorrection(
                    CorrectionEntity(
                        originalText = "오늘 날씨가 매우 조아요.",
                        correctedText = "오늘 날씨가 매우 좋아요.",
                        reason = "맞춤법 오류",
                        userName = "Sarah Jenkins",
                        timestamp = System.currentTimeMillis()
                    )
                )
                insertCorrection(
                    CorrectionEntity(
                        originalText = "나는 내일 영화를 보러 간다 였다.",
                        correctedText = "나는 내일 영화를 보러 갈 예정이다.",
                        reason = "어색한 표현",
                        userName = "Alex Rivera",
                        timestamp = System.currentTimeMillis() - 100000
                    )
                )
            }
        }
    }
}