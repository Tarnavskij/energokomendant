package com.example.energokomendant.data.model

data class GameState(
    val currentLevelId: Int? = null,
    val budget: Int = 0,
    val consumption: Int = 0,
    val comfort: Int = 0,
    val purchasedUpgrades: List<String> = emptyList(),
    val currentDay: Int = 1,
    val maxDays: Int = 5,
    val currentEvent: GameEvent? = null,
    val eventResolved: Boolean = false,
    val lastActionMessage: String? = null,
    val isLevelCompleted: Boolean = false,
    val isGameOver: Boolean = false,
    val starsEarned: Int = 0,
    val rewardEarned: Int = 0,
    val forecastIncome: Int = 0,
    val forecastBill: Int = 0,
    val forecastNet: Int = 0,
    val isBudgetCritical: Boolean = false
)