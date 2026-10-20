package com.example.energokomendant.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MetaState(
    val coins: Int = 0,
    val ownedItems: Set<String> = emptySet(),
    val employees: Set<String> = emptySet(),
    val completedLevels: Map<Int, Int> = emptyMap(),
    val hasSeenOnboarding: Boolean = false,
    val schoolBest: Int = 0
)

object MetaRepository {
    private val _state = MutableStateFlow(MetaState())
    val state: StateFlow<MetaState> = _state.asStateFlow()

    private var prefs: SharedPreferences? = null

    fun init(ctx: Context) {
        val p = ctx.applicationContext.getSharedPreferences("meta", Context.MODE_PRIVATE)
        prefs = p

        // Парсим сохраненные уровни
        val levelsString = p.getString("completedLevels", "") ?: ""
        val levelsMap = if (levelsString.isNotBlank()) {
            levelsString.split(",").mapNotNull { pair ->
                val parts = pair.split(":")
                if (parts.size == 2) parts[0].toIntOrNull()?.let { id -> id to (parts[1].toIntOrNull() ?: 0) } else null
            }.toMap()
        } else emptyMap()

        _state.value = MetaState(
            coins = p.getInt("coins", 0),
            ownedItems = p.getString("items", "")!!.split(",").filter { it.isNotBlank() }.toSet(),
            employees = p.getString("employees", "")!!.split(",").filter { it.isNotBlank() }.toSet(),
            completedLevels = levelsMap,
            hasSeenOnboarding = p.getBoolean("hasSeenOnboarding", false),
            schoolBest = p.getInt("schoolBest", 0)
        )
    }

    fun addCoins(n: Int) {
        _state.update { it.copy(coins = it.coins + n) }
        save()
    }

    fun buyItem(id: String, cost: Int): Boolean {
        val s = _state.value
        if (id in s.ownedItems) return false
        if (s.coins < cost) return false
        _state.update { it.copy(coins = it.coins - cost, ownedItems = it.ownedItems + id) }
        save()
        return true
    }

    fun hireEmployee(id: String, cost: Int): Boolean {
        val s = _state.value
        if (id in s.employees) return false
        if (s.coins < cost) return false
        _state.update { it.copy(coins = it.coins - cost, employees = it.employees + id) }
        save()
        return true
    }

    /**
     * Возвращает количество НОВЫХ звёзд (только те, что лучше прошлого рекорда).
     * Если игрок уже получал 3 звезды и снова получает 3 — вернёт 0.
     */
    fun saveLevelCompleted(levelId: Int, stars: Int): Int {
        val current = _state.value.completedLevels
        val previousBest = current[levelId] ?: 0
        val newStars = (stars - previousBest).coerceAtLeast(0)
        val bestStars = maxOf(previousBest, stars)
        _state.update { it.copy(completedLevels = current + (levelId to bestStars)) }
        save()
        return newStars
    }

    fun markOnboardingSeen() {
        _state.update { it.copy(hasSeenOnboarding = true) }
        save()
    }

    /**
     * Сохраняет результат Школы. Возвращает награду в коинах.
     * Коины только за новый рекорд 3/3.
     */
    fun saveSchoolResult(correct: Int): Int {
        val previousBest = _state.value.schoolBest
        val reward = if (correct == 3 && previousBest < 3) 25 else 0
        if (correct > previousBest) {
            _state.update { it.copy(schoolBest = correct) }
            save()
        }
        if (reward > 0) {
            _state.update { it.copy(coins = it.coins + reward) }
            save()
        }
        return reward
    }

    fun hasItem(id: String) = _state.value.ownedItems.contains(id)
    fun hasEmployee(id: String) = _state.value.employees.contains(id)

    private fun save() {
        val levelsString = _state.value.completedLevels.entries.joinToString(",") { "${it.key}:${it.value}" }
        prefs?.edit()
            ?.putInt("coins", _state.value.coins)
            ?.putString("items", _state.value.ownedItems.joinToString(","))
            ?.putString("employees", _state.value.employees.joinToString(","))
            ?.putString("completedLevels", levelsString)
            ?.putBoolean("hasSeenOnboarding", _state.value.hasSeenOnboarding)
            ?.putInt("schoolBest", _state.value.schoolBest)
            ?.apply()
    }
}