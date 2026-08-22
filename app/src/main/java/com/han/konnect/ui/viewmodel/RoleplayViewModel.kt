package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.han.konnect.data.model.MissionGoal
import com.han.konnect.data.model.RoleplayMessage
import com.han.konnect.data.model.RoleplayScenario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RoleplayViewModel : ViewModel() {

    val scenarios = listOf(
        RoleplayScenario(
            id = "cafe_1",
            title = "카페에서 아이스 아메리카노 주문하기",
            category = "일상 대화",
            difficulty = "초급",
            aiRole = "카페 점원",
            userRole = "손님",
            initialMessage = "어서오세요! 카페 코넥트입니다. 무엇을 주문하시겠어요?",
            iconEmoji = "☕",
            goals = listOf(
                MissionGoal("g1", "원하는 메뉴 및 온도(아이스/핫) 말하기"),
                MissionGoal("g2", "사이즈(라지/레귤러) 지정하기"),
                MissionGoal("g3", "결제 수단 질문에 응답하기")
            )
        ),
        RoleplayScenario(
            id = "airport_1",
            title = "공항 입국 심사대 질문에 답하기",
            category = "여행",
            difficulty = "중급",
            aiRole = "입국 심사관",
            userRole = "여행객",
            initialMessage = "Next, please. Passports and arrival cards, please. What is the purpose of your visit?",
            iconEmoji = "✈️",
            goals = listOf(
                MissionGoal("g1", "방문 목적(여행/출장) 명확히 답하기"),
                MissionGoal("g2", "체류 기간 말하기"),
                MissionGoal("g3", "숙소 위치 답변하기")
            )
        ),
        RoleplayScenario(
            id = "hotel_1",
            title = "호텔 체크인 및 뷰 요청하기",
            category = "여행",
            difficulty = "중급",
            aiRole = "호텔 리셉셔니스트",
            userRole = "투숙객",
            initialMessage = "Welcome to Grand Hotel! How can I assist you today?",
            iconEmoji = "🏨",
            goals = listOf(
                MissionGoal("g1", "예약자 이름 및 예약 사실 전달하기"),
                MissionGoal("g2", "높은 층 또는 뷰 변경 요청해 보기")
            )
        )
    )

    private val _selectedScenario = MutableStateFlow<RoleplayScenario?>(scenarios.first())
    val selectedScenario: StateFlow<RoleplayScenario?> = _selectedScenario.asStateFlow()

    private val _messages = MutableStateFlow<List<RoleplayMessage>>(emptyList())
    val messages: StateFlow<List<RoleplayMessage>> = _messages.asStateFlow()

    private val _currentHint = MutableStateFlow<String?>(null)
    val currentHint: StateFlow<String?> = _currentHint.asStateFlow()

    fun selectScenario(scenario: RoleplayScenario) {
        _selectedScenario.value = scenario

        _messages.value = listOf(
            RoleplayMessage(
                id = "m_init",
                sender = scenario.aiRole,
                text = scenario.initialMessage
            )
        )
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return
        val currentScenario = _selectedScenario.value ?: return

        val userMsg = RoleplayMessage(
            id = System.currentTimeMillis().toString(),
            sender = "User",
            text = userText
        )
        _messages.update { it + userMsg }

        checkMissions(userText)

        val aiReply = RoleplayMessage(
            id = (System.currentTimeMillis() + 1).toString(),
            sender = currentScenario.aiRole,
            text = "네, 알겠습니다! 추가로 필요하신 사항이 있으신가요?"
        )
        _messages.update { it + aiReply }
    }

    private fun checkMissions(text: String) {
        val scenario = _selectedScenario.value ?: return
        val updatedGoals = scenario.goals.map { goal ->
            if (text.contains("아메리카노") || text.contains("아이스") || text.contains("카드") || text.contains("라지")) {
                goal.copy(isCompleted = true)
            } else goal
        }
        _selectedScenario.value = scenario.copy(goals = updatedGoals)
    }

    fun requestHint() {
        _currentHint.value = "💡 힌트: '아이스 아메리카노 라지 사이즈 한 잔 주세요'라고 말해보세요!"
    }

    fun dismissHint() {
        _currentHint.value = null
    }
}