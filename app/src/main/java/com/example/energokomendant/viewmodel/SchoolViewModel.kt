package com.example.energokomendant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energokomendant.data.MetaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SchoolDevice(
    val id: String,
    val emoji: String,
    val name: String,
    val shouldTurnOff: Boolean
)

data class SchoolScenario(
    val title: String,
    val hint: String,
    val devices: List<SchoolDevice>
)

data class SchoolState(
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val scenario: SchoolScenario? = null,
    val selectedIds: Set<String> = emptySet(),
    val timeLeft: Float = 60f,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isPerfect: Boolean = false,
    val isWin: Boolean = false,
    val rewardEarned: Int = 0,
    val bestResult: Int = 0
)

class SchoolViewModel : ViewModel() {
    private val _state = MutableStateFlow(SchoolState())
    val state: StateFlow<SchoolState> = _state.asStateFlow()

    private var lastScenarioIndex = -1

    fun start() {
        val scenarios = SchoolScenarios.all
        var idx = (scenarios.indices).random()
        if (scenarios.size > 1 && idx == lastScenarioIndex) {
            idx = (idx + 1) % scenarios.size
        }
        lastScenarioIndex = idx

        _state.value = SchoolState(
            isRunning = true,
            scenario = scenarios[idx],
            timeLeft = 60f,
            bestResult = MetaRepository.state.value.schoolBest
        )
        viewModelScope.launch {
            var lastTime = System.currentTimeMillis()
            while (_state.value.isRunning) {
                delay(50)
                val now = System.currentTimeMillis()
                val dt = (now - lastTime) / 1000f
                lastTime = now
                val t = (_state.value.timeLeft - dt).coerceAtLeast(0f)
                if (t <= 0f) {
                    finishInternal()
                    break
                }
                _state.value = _state.value.copy(timeLeft = t)
            }
        }
    }

    fun toggleDevice(id: String) {
        val s = _state.value
        if (!s.isRunning) return
        val newSet = if (id in s.selectedIds) s.selectedIds - id else s.selectedIds + id
        _state.value = s.copy(selectedIds = newSet)
    }

    fun finish() {
        if (!_state.value.isRunning) return
        finishInternal()
    }

    private fun finishInternal() {
        val s = _state.value
        val scenario = s.scenario ?: return
        val correct = scenario.devices.count { it.shouldTurnOff && it.id in s.selectedIds }
        val wrong = scenario.devices.count { !it.shouldTurnOff && it.id in s.selectedIds }

        val isPerfect = correct == 3 && wrong == 0
        val isWin = correct >= 2
        val reward = if (isPerfect) MetaRepository.saveSchoolResult(3) else {
            if (isWin) MetaRepository.saveSchoolResult(correct) else MetaRepository.saveSchoolResult(0)
            0
        }

        _state.update {
            it.copy(
                isRunning = false,
                isFinished = true,
                correctCount = correct,
                wrongCount = wrong,
                isPerfect = isPerfect,
                isWin = isWin,
                rewardEarned = reward,
                bestResult = MetaRepository.state.value.schoolBest
            )
        }
    }

    fun reset() {
        _state.value = SchoolState(bestResult = MetaRepository.state.value.schoolBest)
    }
}

object SchoolScenarios {
    val all = listOf(
        SchoolScenario(
            title = "Вечерний перегруз",
            hint = "Вечер, занятия кончились. В школе ещё что-то работает впустую.",
            devices = listOf(
                SchoolDevice("kettle", "☕", "Чайник в учительской", true),
                SchoolDevice("projector", "📽", "Проектор в пустом классе", true),
                SchoolDevice("corridor_light", "💡", "Свет в пустых коридорах", true),
                SchoolDevice("gym_heat", "🌡", "Отопление спортзала", false),
                SchoolDevice("it_servers", "💻", "Сервер информатики", false),
                SchoolDevice("fridge", "🧊", "Холодильник столовой", false)
            )
        ),
        SchoolScenario(
            title = "Зимние каникулы",
            hint = "Школа закрыта на каникулы. Но что-то продолжает работать.",
            devices = listOf(
                SchoolDevice("class_light", "💡", "Освещение классов", true),
                SchoolDevice("computers", "🖥", "20 компьютеров работают", true),
                SchoolDevice("standby_proj", "📽", "Проекторы в standby", true),
                SchoolDevice("emergency", "🚨", "Аварийное освещение", false),
                SchoolDevice("fridge", "🧊", "Холодильник столовой", false),
                SchoolDevice("security", "📹", "Камеры и охрана", false)
            )
        ),
        SchoolScenario(
            title = "Морозный день",
            hint = "На улице −20°C. Школа пытается согреться, но зря тратит тепло.",
            devices = listOf(
                SchoolDevice("open_window_heater", "🔥", "Обогреватель у открытого окна", true),
                SchoolDevice("open_door", "🚪", "Открытая дверь на улицу", true),
                SchoolDevice("extra_heater", "🔥", "Лишний обогреватель в тёплом коридоре", true),
                SchoolDevice("main_heat", "🌡", "Основное отопление", false),
                SchoolDevice("boiler", "⚙️", "Котёл", false),
                SchoolDevice("vent", "💨", "Вентиляция столовой", false)
            )
        ),
        SchoolScenario(
            title = "Жаркий день",
            hint = "На улице +30°C. Кондиционеры работают, но не везде логично.",
            devices = listOf(
                SchoolDevice("ac_window", "❄️", "Кондиционер у открытых окон", true),
                SchoolDevice("ac_empty", "❄️", "Кондиционер в пустом классе", true),
                SchoolDevice("ac_cold_room", "❄️", "Кондиционер в кабинете с +18°C", true),
                SchoolDevice("med_room", "🏥", "Медпункт", false),
                SchoolDevice("vent_kitchen", "💨", "Вентиляция столовой", false),
                SchoolDevice("fridge", "🧊", "Холодильники", false)
            )
        ),
        SchoolScenario(
            title = "Ночная смена",
            hint = "Ночь. В школе только сторож и камеры. Но что-то не выключили.",
            devices = listOf(
                SchoolDevice("hall_chandeliers", "💡", "Люстры в актовом зале", true),
                SchoolDevice("dishwasher", "🍽", "Посудомойка в столовой", true),
                SchoolDevice("director_lamp", "💡", "Лампа в кабинете директора", true),
                SchoolDevice("emergency", "🚨", "Аварийное освещение", false),
                SchoolDevice("security", "📹", "Камеры", false),
                SchoolDevice("fridge", "🧊", "Холодильники", false)
            )
        ),
        SchoolScenario(
            title = "Утро понедельника",
            hint = "Все включили всё сразу. Нагрузка на сеть скачет.",
            devices = listOf(
                SchoolDevice("all_computers", "💻", "Все 30 компьютеров одновременно", true),
                SchoolDevice("gym_light", "💡", "Всё освещение спортзала", true),
                SchoolDevice("kitchen_equip", "🍳", "Всё оборудование столовой", true),
                SchoolDevice("class_light", "💡", "Освещение в классах", false),
                SchoolDevice("heating", "🌡", "Отопление", false),
                SchoolDevice("vent", "💨", "Вентиляция", false)
            )
        ),
        SchoolScenario(
            title = "Проверка счётчиков",
            hint = "Счётчик крутится слишком быстро. Найди утечки.",
            devices = listOf(
                SchoolDevice("old_fridge", "🧊", "Старый шумный холодильник", true),
                SchoolDevice("charger", "🔌", "Зарядка в розетке без телефона", true),
                SchoolDevice("workshop_heater", "🔥", "Обогреватель в мастерской ночью", true),
                SchoolDevice("class_light", "💡", "Освещение классов", false),
                SchoolDevice("computers", "💻", "Компьютеры", false),
                SchoolDevice("heating", "🌡", "Отопление", false)
            )
        )
    )
}