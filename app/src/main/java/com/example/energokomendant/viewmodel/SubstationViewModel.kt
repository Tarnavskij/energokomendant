package com.example.energokomendant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energokomendant.data.MetaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SubstationState(
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val isWin: Boolean = false,
    val load: Float = 20f,
    val timeLeft: Float = 60f,
    val totalTime: Float = 60f,
    val cooldowns: List<Float> = listOf(0f, 0f, 0f),
    val nextSpikeIn: Float = 5f,
    val spikeFlash: Boolean = false,
    val rewardEarned: Int = 0
)

class SubstationViewModel : ViewModel() {
    private val _state = MutableStateFlow(SubstationState())
    val state: StateFlow<SubstationState> = _state.asStateFlow()

    fun start() {
        val bonusTime = if (MetaRepository.hasItem("stabilizer")) 10f else 0f
        val total = 60f + bonusTime
        _state.value = SubstationState(isRunning = true, load = 20f, timeLeft = total, totalTime = total)
        viewModelScope.launch {
            var lastTime = System.currentTimeMillis()
            while (_state.value.isRunning) {
                delay(50)
                val now = System.currentTimeMillis()
                val dt = (now - lastTime) / 1000f
                lastTime = now
                tick(dt)
            }
        }
    }

    private fun tick(dt: Float) {
        val s = _state.value
        if (!s.isRunning) return

        val growthMultiplier = if (MetaRepository.hasEmployee("dispatcher")) 0.7f else 1f
        var load = s.load + 2f * growthMultiplier * dt
        var nextSpike = s.nextSpikeIn - dt
        var spikeFlash = s.spikeFlash

        if (nextSpike <= 0f) {
            load += (15..25).random()
            nextSpike = (4..7).random().toFloat()
            spikeFlash = true
        } else if (nextSpike < 4.5f) {
            spikeFlash = false
        }

        val newTime = s.timeLeft - dt
        val newCd = s.cooldowns.map { (it - dt).coerceAtLeast(0f) }

        if (load >= 100f) {
            _state.value = s.copy(load = 100f, isRunning = false, isFinished = true, isWin = false)
            return
        }
        if (newTime <= 0f) {
            val reward = 30
            MetaRepository.addCoins(reward)
            _state.value = s.copy(
                load = load, timeLeft = 0f, isRunning = false,
                isFinished = true, isWin = true, rewardEarned = reward
            )
            return
        }
        _state.value = s.copy(
            load = load, timeLeft = newTime, cooldowns = newCd,
            nextSpikeIn = nextSpike, spikeFlash = spikeFlash
        )
    }

    fun tapLine(index: Int) {
        val s = _state.value
        if (!s.isRunning) return
        if (s.cooldowns[index] > 0f) return
        val newLoad = (s.load - 20f).coerceAtLeast(0f)
        val newCd = s.cooldowns.toMutableList().also { it[index] = 2f }
        _state.value = s.copy(load = newLoad, cooldowns = newCd)
    }

    fun reset() {
        _state.value = SubstationState()
    }
}