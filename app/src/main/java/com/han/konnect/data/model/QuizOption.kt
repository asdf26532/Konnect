package com.han.konnect.data.model

data class QuizOption(
    val id: Int,
    val text: String,
    val isCorrect: Boolean
)

data class DailyChallenge(
    val id: String = "",
    val date: String = "",
    val expression: String = "",
    val meaning: String = "",
    val exampleSentence: String = "",
    val question: String = "",
    val options: List<QuizOption> = emptyList(),
    val rewardPoints: Int = 50
)