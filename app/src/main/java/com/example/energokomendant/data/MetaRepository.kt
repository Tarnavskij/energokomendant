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
    val employees: Set<String> = emptySet()
)

object MetaRepository {
    private val _state = MutableStateFlow(MetaState())
    val state: StateFlow<MetaState> = _state.asStateFlow()

    private var prefs: SharedPreferences? = null

    fun init(ctx: Context) {
        val p = ctx.applicationContext.getSharedPreferences("meta", Context.MODE_PRIVATE)
        prefs = p
        _state.value = MetaState(
            coins = p.getInt("coins", 0),
            ownedItems = p.getString("items", "")!!.split(",").filter { it.isNotBlank() }.toSet(),
            employees = p.getString("employees", "")!!.split(",").filter { it.isNotBlank() }.toSet()
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

    fun hasItem(id: String) = _state.value.ownedItems.contains(id)
    fun hasEmployee(id: String) = _state.value.employees.contains(id)

    private fun save() {
        prefs?.edit()
            ?.putInt("coins", _state.value.coins)
            ?.putString("items", _state.value.ownedItems.joinToString(","))
            ?.putString("employees", _state.value.employees.joinToString(","))
            ?.apply()
    }
}