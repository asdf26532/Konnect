package com.han.konnect.data.model

data class MissionGoal(
    val id: String,
    val description: String,
    val isCompleted: Boolean = false
)

data class RoleplayScenario(
    val id: String,
    val title: String,
    val category: String,
    val difficulty: String,
    val aiRole: String,
    val userRole: String,
    val initialMessage: String,
    val iconEmoji: String,
    val goals: List<MissionGoal>
)