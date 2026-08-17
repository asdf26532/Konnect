package com.han.konnect.data.model

data class SkillScore(
    val category: String,
    val score: Int
)

data class GrammarPattern(
    val pattern: String,
    val count: Int,
    val recommendation: String
)

data class ConversationReport(
    val estimatedLevel: String = "TOPIK 3급 (CEFR B1)",
    val totalMessagesSent: Int = 142,
    val totalWordsUsed: Int = 850,
    val correctionRate: Float = 12.5f,
    val skillScores: List<SkillScore> = emptyList(),
    val topGrammarErrors: List<GrammarPattern> = emptyList()
)