package com.example.energokomendant.data.model

data class EventChoice(
    val label: String,
    val resultMessage: String,
    val budgetDelta: Int = 0,
    val consumptionDelta: Int = 0,
    val comfortDelta: Int = 0,
    val isRisky: Boolean = false,
    val riskSuccessMessage: String = "",
    val riskFailMessage: String = ""
)

data class GameEvent(
    val title: String,
    val description: String,
    val choices: List<EventChoice>
)