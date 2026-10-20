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
    val choices: List<EventChoice>,
    /** Если это улучшение куплено — событие не выпадает */
    val blockedByUpgrade: String? = null,
    /** Если это улучшение куплено — эффекты события уменьшаются вдвое */
    val mitigatedByUpgrade: String? = null,
    /** Текст-подсказка, который показывается, если улучшение сработало */
    val mitigationNote: String? = null
)