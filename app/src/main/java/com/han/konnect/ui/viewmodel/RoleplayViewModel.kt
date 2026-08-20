package com.han.konnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.han.konnect.data.model.MissionGoal
import com.han.konnect.data.model.RoleplayScenario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _selectedScenario = MutableStateFlow<RoleplayScenario?>(null)
    val selectedScenario: StateFlow<RoleplayScenario?> = _selectedScenario.asStateFlow()

    fun selectScenario(scenario: RoleplayScenario) {
        _selectedScenario.value = scenario
    }
}