package com.example.energokomendant.viewmodel

import androidx.lifecycle.ViewModel
import com.example.energokomendant.data.MetaRepository
import com.example.energokomendant.data.model.EventChoice
import com.example.energokomendant.data.model.GameState
import com.example.energokomendant.data.model.Level
import com.example.energokomendant.data.model.Upgrade
import com.example.energokomendant.data.repository.LevelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var currentLevel: Level? = null

    fun startLevel(levelId: Int) {
        val level = LevelRepository.getLevels().find { it.id == levelId } ?: return
        currentLevel = level

        val budgetBonus = if (MetaRepository.hasItem("tools")) 100 else 0
        val comfortBonus = if (MetaRepository.hasEmployee("plumber")) 5 else 0
        val consBonus = (if (MetaRepository.hasItem("insulation")) 5 else 0) +
                (if (MetaRepository.hasEmployee("electrician")) 5 else 0)

        _gameState.value = GameState(
            currentLevelId = levelId,
            budget = level.startBudget + budgetBonus,
            consumption = (level.startConsumption - consBonus).coerceAtLeast(10),
            comfort = (level.startComfort + comfortBonus).coerceAtMost(100),
            currentDay = 1,
            maxDays = level.maxDays,
            currentEvent = LevelRepository.getRandomEvent(levelId, emptySet()),
            eventResolved = false
        ).recalcForecast(level)
    }

    fun resolveEvent(choice: EventChoice) {
        val level = currentLevel ?: return
        val state = _gameState.value
        if (state.eventResolved) return

        val usedBudgetDelta: Int
        val usedConsDelta: Int
        val usedComfDelta: Int
        val message: String

        if (choice.isRisky) {
            val success = (0..1).random() == 1
            if (success) {
                usedBudgetDelta = choice.budgetDelta
                usedConsDelta = choice.consumptionDelta
                usedComfDelta = choice.comfortDelta
                message = choice.riskSuccessMessage
            } else {
                usedBudgetDelta = -100
                usedConsDelta = if (choice.consumptionDelta < 0) 0 else choice.consumptionDelta
                usedComfDelta = -5
                message = choice.riskFailMessage
            }
        } else {
            usedBudgetDelta = choice.budgetDelta
            usedConsDelta = choice.consumptionDelta
            usedComfDelta = choice.comfortDelta
            message = choice.resultMessage
        }

        _gameState.update {
            it.copy(
                budget = (it.budget + usedBudgetDelta).coerceAtLeast(0),
                consumption = (it.consumption + usedConsDelta).coerceAtLeast(1),
                comfort = (it.comfort + usedComfDelta).coerceIn(0, 100),
                eventResolved = true,
                currentEvent = null,
                lastActionMessage = message
            ).recalcForecast(level)
        }
    }

    fun buyUpgrade(upgrade: Upgrade) {
        val level = currentLevel ?: return
        val state = _gameState.value
        if (state.isLevelCompleted || state.isGameOver) return
        if (state.purchasedUpgrades.contains(upgrade.id)) return
        if (state.budget < upgrade.cost) return
        _gameState.update {
            it.copy(
                budget = it.budget - upgrade.cost,
                consumption = (it.consumption + upgrade.consumptionReduction).coerceAtLeast(1),
                comfort = (it.comfort + upgrade.comfortChange).coerceIn(0, 100),
                purchasedUpgrades = it.purchasedUpgrades + upgrade.id
            ).recalcForecast(level)
        }
    }

    fun nextDay() {
        val level = currentLevel ?: return
        val state = _gameState.value
        if (!state.eventResolved) return
        if (state.isLevelCompleted || state.isGameOver) return

        val incomeBonus = (if (MetaRepository.hasItem("led")) 5 else 0) +
                (if (MetaRepository.hasEmployee("economist")) 10 else 0)
        val income = state.comfort * 3 + incomeBonus
        val bill = state.consumption * level.tariff
        val newBudget = state.budget + income - bill

        val newConsumption = state.consumption + 2
        val newComfort = (state.comfort - 2).coerceIn(0, 100)

        val nextDayNum = state.currentDay + 1
        val isBroke = newBudget < 0
        val isFrozen = newComfort < level.minComfort
        val isLastDay = state.currentDay >= state.maxDays
        val isSuccess = newConsumption <= level.targetConsumption && newComfort >= level.minComfort

        val isGameOver = isBroke || isFrozen
        val isCompleted = !isGameOver && isLastDay && isSuccess

        val gameOverMessage = when {
            isBroke -> "Разорение: бюджет ушёл в минус."
            isFrozen -> "Жильцы замёрзли и уехали."
            isLastDay && !isSuccess -> "Цель не достигнута."
            else -> null
        }

        if (isLastDay || isGameOver) {
            val stars = if (isCompleted) calculateStars(newConsumption, newComfort, level) else 0
            val reward = if (isCompleted) 20 + stars * 10 else 0
            if (reward > 0) MetaRepository.addCoins(reward)

            _gameState.update {
                it.copy(
                    budget = newBudget.coerceAtLeast(0),
                    consumption = newConsumption,
                    comfort = newComfort,
                    currentDay = nextDayNum.coerceAtMost(state.maxDays),
                    currentEvent = null,
                    eventResolved = false,
                    lastActionMessage = gameOverMessage,
                    isGameOver = isGameOver || (isLastDay && !isSuccess),
                    isLevelCompleted = isCompleted,
                    starsEarned = stars,
                    rewardEarned = reward,
                    isBudgetCritical = isBroke
                ).recalcForecast(level)
            }
        } else {
            _gameState.update {
                it.copy(
                    budget = newBudget,
                    consumption = newConsumption,
                    comfort = newComfort,
                    currentDay = nextDayNum,
                    currentEvent = LevelRepository.getRandomEvent(level.id, it.purchasedUpgrades.toSet()),
                    eventResolved = false,
                    lastActionMessage = null,
                    isBudgetCritical = newBudget < 50
                ).recalcForecast(level)
            }
        }
    }

    private fun GameState.recalcForecast(level: Level): GameState {
        val incomeBonus = (if (MetaRepository.hasItem("led")) 5 else 0) +
                (if (MetaRepository.hasEmployee("economist")) 10 else 0)
        val income = comfort * 3 + incomeBonus
        val bill = consumption * level.tariff
        return copy(
            forecastIncome = income,
            forecastBill = bill,
            forecastNet = income - bill
        )
    }

    private fun calculateStars(consumption: Int, comfort: Int, level: Level): Int {
        if (consumption > level.targetConsumption || comfort < level.minComfort) return 0
        return when {
            consumption <= level.targetConsumption - 15 && comfort >= level.minComfort + 15 -> 3
            consumption <= level.targetConsumption - 5 && comfort >= level.minComfort + 5 -> 2
            else -> 1
        }
    }

    fun reset() {
        _gameState.value = GameState()
        currentLevel = null
    }
}