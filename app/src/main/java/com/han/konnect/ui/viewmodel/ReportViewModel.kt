package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.han.konnect.data.model.ConversationReport
import com.han.konnect.data.model.GrammarPattern
import com.han.konnect.data.model.SkillScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportViewModel : ViewModel() {

    private val _reportState = MutableStateFlow(
        ConversationReport(
            estimatedLevel = "TOPIK 3급 (CEFR B1)",
            totalMessagesSent = 142,
            totalWordsUsed = 850,
            correctionRate = 12.5f,
            skillScores = listOf(
                SkillScore("어휘력", 78),
                SkillScore("문법", 65),
                SkillScore("유창성", 82),
                SkillScore("표현력", 70),
                SkillScore("이해도", 88)
            ),
            topGrammarErrors = listOf(
                GrammarPattern("조사 사용 오류 (은/는 vs 이/가)", 5, "'내가'와 '나는'의 쓰임새 구분이 필요해요."),
                GrammarPattern("과거 시제 어미 미숙", 3, "'갔어요' 대신 '가았어요'로 잘못 작성함."),
                GrammarPattern("존댓말 어미 혼용", 2, "반말과 존댓말을 한 문장에서 섞어 씀.")
            )
        )
    )
    val reportState: StateFlow<ConversationReport> = _reportState.asStateFlow()
}