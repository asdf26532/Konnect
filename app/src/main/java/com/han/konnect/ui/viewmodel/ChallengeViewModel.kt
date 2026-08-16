package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.han.konnect.data.model.DailyChallenge
import com.han.konnect.data.model.QuizOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChallengeViewModel : ViewModel() {

    private val _streakCount = MutableStateFlow(5)
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    private val _userPoints = MutableStateFlow(350)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _selectedOptionId = MutableStateFlow<Int?>(null)
    val selectedOptionId: StateFlow<Int?> = _selectedOptionId.asStateFlow()

    private val _isSubmitted = MutableStateFlow(false)
    val isSubmitted: StateFlow<Boolean> = _isSubmitted.asStateFlow()

    private val _showRewardDialog = MutableStateFlow<Boolean?>(null)
    val showRewardDialog: StateFlow<Boolean?> = _showRewardDialog.asStateFlow()

    val todayChallenge = DailyChallenge(
        id = "c1",
        date = "Today",
        expression = "식은 죽 먹기 (Piece of cake)",
        meaning = "아주 하기 쉽고 간단한 일을 비유적으로 이르는 말.",
        exampleSentence = "한국어 인사말 배우기는 식은 죽 먹기예요!",
        question = "'식은 죽 먹기'와 같은 의미의 영어 표현은 무엇일까요?",
        options = listOf(
            QuizOption(1, "Break a leg", false),
            QuizOption(2, "Piece of cake", true),
            QuizOption(3, "Under the weather", false),
            QuizOption(4, "Spill the beans", false)
        ),
        rewardPoints = 50
    )

    fun selectOption(optionId: Int) {
        if (_isSubmitted.value) return
        _selectedOptionId.value = optionId
    }

    fun submitAnswer() {
        val selectedId = _selectedOptionId.value ?: return
        if (_isSubmitted.value) return

        _isSubmitted.value = true
        val correctOption = todayChallenge.options.find { it.isCorrect }

        if (selectedId == correctOption?.id) {
            _userPoints.update { it + todayChallenge.rewardPoints }
            _streakCount.update { it + 1 }
            _showRewardDialog.value = true
        } else {
            _showRewardDialog.value = false
        }
    }

    fun dismissRewardDialog() {
        _showRewardDialog.value = null
    }
}