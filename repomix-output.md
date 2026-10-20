This file is a merged representation of a subset of the codebase, containing files not matching ignore patterns, combined into a single document by Repomix.

<file_summary>
This section contains a summary of this file.

<purpose>
This file contains a packed representation of a subset of the repository's contents that is considered the most important context.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.
</purpose>

<file_format>
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  - File path as an attribute
  - Full contents of the file
</file_format>

<usage_guidelines>
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.
</usage_guidelines>

<notes>
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Files matching these patterns are excluded: **/build/**, **/.gradle/**, **/.idea/**, **/*.iml, local.properties, **/.git/**
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Files are sorted by Git change count (files with more changes are at the bottom)
</notes>

</file_summary>

<directory_structure>
app/
  src/
    androidTest/
      java/
        com/
          example/
            energokomendant/
              ExampleInstrumentedTest.kt
    main/
      java/
        com/
          example/
            energokomendant/
              data/
                model/
                  GameEvent.kt
                  GameState.kt
                  Level.kt
                  Upgrade.kt
                repository/
                  LevelRepository.kt
                Catalog.kt
                MetaRepository.kt
              ui/
                game/
                  GameScreen.kt
                  HouseVisualizer.kt
                  LevelSelectScreen.kt
                hr/
                  HRScreen.kt
                menu/
                  CityMapScreen.kt
                substation/
                  SubstationScreen.kt
                suppliers/
                  SupplierScreen.kt
                theme/
                  Color.kt
                  Theme.kt
                  Type.kt
              viewmodel/
                GameViewModel.kt
                SubstationViewModel.kt
              MainActivity.kt
      res/
        drawable/
          ic_launcher_background.xml
          ic_launcher_foreground.xml
        mipmap-anydpi-v26/
          ic_launcher_round.xml
          ic_launcher.xml
        mipmap-hdpi/
          ic_launcher_round.webp
          ic_launcher.webp
        mipmap-mdpi/
          ic_launcher_round.webp
          ic_launcher.webp
        mipmap-xhdpi/
          ic_launcher_round.webp
          ic_launcher.webp
        mipmap-xxhdpi/
          ic_launcher_round.webp
          ic_launcher.webp
        mipmap-xxxhdpi/
          ic_launcher_round.webp
          ic_launcher.webp
        values/
          colors.xml
          strings.xml
          themes.xml
        xml/
          backup_rules.xml
          data_extraction_rules.xml
      AndroidManifest.xml
    test/
      java/
        com/
          example/
            energokomendant/
              ExampleUnitTest.kt
  .gitignore
  build.gradle.kts
  proguard-rules.pro
gradle/
  wrapper/
    gradle-wrapper.jar
    gradle-wrapper.properties
  libs.versions.toml
.gitignore
build.gradle.kts
gradle.properties
gradlew
gradlew.bat
settings.gradle.kts
</directory_structure>

<files>
This section contains the contents of the repository's files.

<file path="app/src/main/java/com/example/energokomendant/ui/game/LevelSelectScreen.kt">
package com.example.energokomendant.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.data.repository.LevelRepository

@Composable
fun LevelSelectScreen(
    completedLevels: Map<Int, Int>,
    onPlayLevel: (Int) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val levels = LevelRepository.getLevels()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Назад") }
            Text("🏠 Дом", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Выбери миссию", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(levels) { level ->
                val stars = completedLevels[level.id] ?: 0
                val isCompleted = stars > 0

                // Блокируем уровень, если предыдущий не пройден
                val previousCompleted = level.id == 1 || (completedLevels[level.id - 1] ?: 0) > 0
                val isLocked = !previousCompleted

                Card(
                    onClick = { if (!isLocked) onPlayLevel(level.id) },
                    enabled = !isLocked,
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isLocked -> MaterialTheme.colorScheme.surfaceVariant
                            isCompleted -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isLocked) "🔒 ${level.title}" else level.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(level.description, fontSize = 11.sp)
                        }
                        if (isCompleted) {
                            Text("★".repeat(stars) + "☆".repeat(3 - stars), fontSize = 20.sp)
                        } else if (!isLocked) {
                            Text("→", fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}
</file>

<file path="app/src/androidTest/java/com/example/energokomendant/ExampleInstrumentedTest.kt">
package com.example.energokomendant

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.energokomendant", appContext.packageName)
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/data/model/GameEvent.kt">
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
</file>

<file path="app/src/main/java/com/example/energokomendant/data/model/GameState.kt">
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
</file>

<file path="app/src/main/java/com/example/energokomendant/data/model/Level.kt">
package com.example.energokomendant.data.model

data class Level(
    val id: Int,
    val title: String,
    val description: String,
    val startBudget: Int,
    val startConsumption: Int,
    val startComfort: Int,
    val targetConsumption: Int,
    val minComfort: Int,
    val maxDays: Int,
    val tariff: Int,
    val upgrades: List<Upgrade>
)
</file>

<file path="app/src/main/java/com/example/energokomendant/data/model/Upgrade.kt">
package com.example.energokomendant.data.model

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val cost: Int,
    val consumptionReduction: Int, // отрицательное число = экономия
    val comfortChange: Int
)
</file>

<file path="app/src/main/java/com/example/energokomendant/data/repository/LevelRepository.kt">
package com.example.energokomendant.data.repository

import com.example.energokomendant.data.model.EventChoice
import com.example.energokomendant.data.model.GameEvent
import com.example.energokomendant.data.model.Level
import com.example.energokomendant.data.model.Upgrade

object LevelRepository {

    fun getLevels(): List<Level> = listOf(
        Level(
            id = 1, title = "Уровень 1: Освещение",
            description = "Счёт за свет съедает бюджет. Найди способ платить меньше.",
            startBudget = 500, startConsumption = 100, startComfort = 80,
            targetConsumption = 60, minComfort = 50, maxDays = 5, tariff = 3,
            upgrades = listOf(
                Upgrade("led", "LED-лампы", "Заменить лампы накаливания (−20 кВт)", 300, -20, 5),
                Upgrade("sensors", "Датчики движения", "Свет в пустых коридорах выключается (−20 кВт)", 400, -20, -5),
                Upgrade("off_light", "Свет по требованию", "Подсобки без постоянного света (−10 кВт)", 0, -10, -10)
            )
        ),
        Level(
            id = 2, title = "Уровень 2: Тепло",
            description = "Отопление дорого. Сохрани тепло, не заморозив жильцов.",
            startBudget = 600, startConsumption = 120, startComfort = 80,
            targetConsumption = 90, minComfort = 50, maxDays = 5, tariff = 4,
            upgrades = listOf(
                Upgrade("windows", "Утеплить окна", "Стеклопакеты и герметизация (−30 кВт)", 600, -30, 15),
                Upgrade("doors", "Доводчики", "Закрывают двери сами (−10 кВт)", 250, -10, 0),
                Upgrade("temp_down", "Снизить отопление", "Минус 2°C, жильцам прохладнее (−20 кВт)", 0, -20, -15)
            )
        ),
        Level(
            id = 3, title = "Уровень 3: Техника",
            description = "Старая техника ест деньги. Обнови и научи жильцов.",
            startBudget = 800, startConsumption = 150, startComfort = 80,
            targetConsumption = 120, minComfort = 60, maxDays = 5, tariff = 4,
            upgrades = listOf(
                Upgrade("fridge", "Холодильник А++", "Экономит 40% (−40 кВт)", 800, -40, 5),
                Upgrade("wash", "Стирка 30°C", "Экономия на нагреве (−20 кВт)", 0, -20, -5),
                Upgrade("standby", "Убрать standby", "Приборы не жрут в режиме ожидания (−15 кВт)", 150, -15, 0)
            )
        ),
        Level(
            id = 4, title = "Уровень 4: Солнце",
            description = "Солнечные панели — дорого, но окупаются. Пробуй.",
            startBudget = 1000, startConsumption = 180, startComfort = 80,
            targetConsumption = 140, minComfort = 60, maxDays = 5, tariff = 5,
            upgrades = listOf(
                Upgrade("solar", "Солнечные панели", "Энергия от солнца (−60 кВт)", 1500, -60, 0),
                Upgrade("solar_water", "Солнечный нагреватель", "Горячая вода бесплатно (−30 кВт)", 900, -30, 5)
            )
        ),
        Level(
            id = 5, title = "Уровень 5: Умный дом",
            description = "Финальная проверка. Автоматизируй всё или разорись.",
            startBudget = 1500, startConsumption = 220, startComfort = 80,
            targetConsumption = 160, minComfort = 70, maxDays = 5, tariff = 6,
            upgrades = listOf(
                Upgrade("smart_plugs", "Умные розетки", "Отключают по расписанию (−30 кВт)", 800, -30, 5),
                Upgrade("temp_sensors", "Датчики температуры", "Регулируют отопление (−20 кВт)", 600, -20, 10),
                Upgrade("auto_light", "Умный свет", "Свет по расписанию (−40 кВт)", 1000, -40, 5)
            )
        )
    )

    fun getRandomEvent(levelId: Int, day: Int): GameEvent {
        val pool = eventsByLevel[levelId] ?: emptyList()
        if (pool.isEmpty()) return GameEvent("Спокойный день", "Ничего не случилось.", emptyList())
        val index = (levelId * 10 + day * 3) % pool.size
        return pool[index]
    }

    private val eventsByLevel: Map<Int, List<GameEvent>> = mapOf(
        1 to listOf(
            GameEvent(
                "Инспекция энергосбыта",
                "Инспектор проверяет счётчики. Можно попробовать договориться.",
                listOf(
                    EventChoice("Показать экономию", "Инспектор впечатлён. Премия!", budgetDelta = 100),
                    EventChoice("Сказать правду", "Штраф за перерасход.", budgetDelta = -80, comfortDelta = -5),
                    EventChoice(
                        "Договориться",
                        "Результат неизвестен.",
                        isRisky = true,
                        riskSuccessMessage = "Инспектор оказался другом — отсрочка платежа!",
                        riskFailMessage = "Инспектор строгий — штраф!",
                        budgetDelta = 250
                    )
                )
            ),
            GameEvent(
                "Жильцы жалуются на темноту",
                "В подъезде темно, люди спотыкаются.",
                listOf(
                    EventChoice("Включить свет", "Светло, но расход вырос.", comfortDelta = 10, consumptionDelta = 10),
                    EventChoice("Выдать фонарики", "Дешевле, но неуклюже.", budgetDelta = -50, comfortDelta = 3),
                    EventChoice("Пусть привыкают", "Жильцы злятся.", comfortDelta = -15)
                )
            ),
            GameEvent(
                "Соседи предлагают скинуться на LED",
                "Скидываемся поровну — экономия общая.",
                listOf(
                    EventChoice("Согласиться", "Скинулись, стало легче.", budgetDelta = -100, consumptionDelta = -15),
                    EventChoice("Отказаться", "Соседи обиделись.", comfortDelta = -10),
                    EventChoice(
                        "Убедить их заплатить больше",
                        "Переговоры.",
                        isRisky = true,
                        riskSuccessMessage = "Соседи согласились на 70%!",
                        riskFailMessage = "Соседи обиделись и ушли.",
                        budgetDelta = 0,
                        comfortDelta = 5
                    )
                )
            )
        ),
        2 to listOf(
            GameEvent(
                "Мороз ударил",
                "−20°C за окном. Жильцы мёрзнут.",
                listOf(
                    EventChoice("Включить обогрев", "Тепло, но счёт вырастет.", comfortDelta = 10, consumptionDelta = 15),
                    EventChoice("Сказать одеваться теплее", "Жильцы недовольны.", comfortDelta = -15),
                    EventChoice(
                        "Найти утечку тепла",
                        "Ищем, где дует.",
                        isRisky = true,
                        riskSuccessMessage = "Нашли щель! Заделали бесплатно.",
                        riskFailMessage = "Не нашли, только потратились.",
                        budgetDelta = -100
                    )
                )
            ),
            GameEvent(
                "Город выделил субсидию",
                "Деньги на утепление пришли.",
                listOf(
                    EventChoice("Принять", "Деньги на счету.", budgetDelta = 200),
                    EventChoice("Отдать соседям", "Репутация растёт.", comfortDelta = 10)
                )
            ),
            GameEvent(
                "Жильцы открывают окна",
                "Жарко, проветривают — тепло уходит.",
                listOf(
                    EventChoice("Снизить отопление", "Прохладнее, экономия.", consumptionDelta = -10, comfortDelta = -3),
                    EventChoice("Оставить", "Духота и перерасход.", consumptionDelta = 10)
                )
            )
        ),
        3 to listOf(
            GameEvent(
                "Старый холодильник сломался",
                "Еда портится. Жильцы злятся.",
                listOf(
                    EventChoice("Починить", "Дорого, но работает.", budgetDelta = -150, comfortDelta = 5),
                    EventChoice("Купить новый", "Инвестиция в экономию.", budgetDelta = -400, consumptionDelta = -25),
                    EventChoice("Оставить", "Портится еда.", comfortDelta = -15)
                )
            ),
            GameEvent(
                "Стиралка работает в пик",
                "Все стирают вечером — расход скачет.",
                listOf(
                    EventChoice("Договориться о дневной стирке", "Помогло.", consumptionDelta = -10, comfortDelta = -5),
                    EventChoice("Купить таймеры", "Автоматика.", budgetDelta = -150, consumptionDelta = -15)
                )
            ),
            GameEvent(
                "Акция в магазине техники",
                "Скидки на А++ технику только сегодня.",
                listOf(
                    EventChoice("Взять кредит", "Рискованно.",
                        isRisky = true,
                        riskSuccessMessage = "Окупилось с первого месяца!",
                        riskFailMessage = "Проценты съели всё.",
                        budgetDelta = -300,
                        consumptionDelta = -30
                    ),
                    EventChoice("Пропустить", "Без изменений.")
                )
            )
        ),
        4 to listOf(
            GameEvent(
                "Облачный день",
                "Солнца мало, панели работают слабо.",
                listOf(
                    EventChoice("Включить сеть", "Расход вырос.", consumptionDelta = 15),
                    EventChoice("Сэкономить", "Жильцы недовольны.", comfortDelta = -5)
                )
            ),
            GameEvent(
                "Пик солнца",
                "Полдень. Можно продать излишки.",
                listOf(
                    EventChoice("Запасти энергию", "Экономия!", consumptionDelta = -10),
                    EventChoice(
                        "Продать излишки",
                        "Риск: сеть может не принять.",
                        isRisky = true,
                        riskSuccessMessage = "Сеть приняла! Прибыль.",
                        riskFailMessage = "Сеть перегружена, штраф.",
                        budgetDelta = 250
                    )
                )
            ),
            GameEvent(
                "Инвестор предлагает контракт",
                "Купить энергию по фиксированной цене.",
                listOf(
                    EventChoice("Подписать", "Стабильно, но дорого в мороз.", budgetDelta = 100, consumptionDelta = 5),
                    EventChoice("Отказаться", "Рискованно.")
                )
            )
        ),
        5 to listOf(
            GameEvent(
                "Сбой системы умного дома",
                "Всё зависло, свет горит везде.",
                listOf(
                    EventChoice("Перезагрузить", "Обошлось.", budgetDelta = -100, comfortDelta = 5),
                    EventChoice("Оставить", "Расход растёт.", consumptionDelta = 15),
                    EventChoice(
                        "Взломать систему",
                        "Попробовать починить самому.",
                        isRisky = true,
                        riskSuccessMessage = "Получилось! Всё работает.",
                        riskFailMessage = "Сломалось ещё сильнее.",
                        consumptionDelta = 20
                    )
                )
            ),
            GameEvent(
                "Жильцы просят инструктаж",
                "Хотят научиться экономить.",
                listOf(
                    EventChoice("Провести лекцию", "Жильцы поняли.", consumptionDelta = -10, comfortDelta = 5),
                    EventChoice("Раздать памятки", "Слабее эффект.", consumptionDelta = -5)
                )
            ),
            GameEvent(
                "Финальная проверка",
                "Комиссия оценивает результаты.",
                listOf(
                    EventChoice("Показать отчёты", "Гладко.", budgetDelta = 100),
                    EventChoice("Импровизировать", "Рискованно.",
                        isRisky = true,
                        riskSuccessMessage = "Комиссия в восторге!",
                        riskFailMessage = "Комиссия не оценила.",
                        budgetDelta = 200,
                        comfortDelta = -10
                    )
                )
            )
        )
    )
}
</file>

<file path="app/src/main/java/com/example/energokomendant/data/Catalog.kt">
package com.example.energokomendant.data

data class ShopItem(
    val id: String,
    val name: String,
    val description: String,
    val cost: Int,
    val emoji: String
)

data class Employee(
    val id: String,
    val name: String,
    val description: String,
    val cost: Int,
    val emoji: String
)

object Catalog {
    val items = listOf(
        ShopItem("led", "LED-комплект", "+5₽ к доходу в день (Дом)", 100, "💡"),
        ShopItem("insulation", "Утеплитель", "−5 к стартовому расходу (Дом)", 150, "🧱"),
        ShopItem("tools", "Инструменты", "+100₽ к стартовому бюджету (Дом)", 200, "🔧"),
        ShopItem("stabilizer", "Стабилизатор", "+10 сек к таймеру (Подстанция)", 250, "🔌")
    )

    val employees = listOf(
        Employee("electrician", "Электрик", "−5 к стартовому расходу (Дом)", 150, "👷"),
        Employee("plumber", "Сантехник", "+5 к стартовому комфорту (Дом)", 200, "🚿"),
        Employee("economist", "Экономист", "+10₽ к доходу в день (Дом)", 300, "📊"),
        Employee("dispatcher", "Диспетчер", "−30% к росту нагрузки (Подстанция)", 400, "🎧")
    )
}
</file>

<file path="app/src/main/java/com/example/energokomendant/data/MetaRepository.kt">
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
    val completedLevels: Map<Int, Int> = emptyMap() // id уровня -> количество звезд
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
            completedLevels = levelsMap
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

    fun saveLevelCompleted(levelId: Int, stars: Int) {
        val current = _state.value.completedLevels
        val bestStars = maxOf(current[levelId] ?: 0, stars)
        _state.update { it.copy(completedLevels = current + (levelId to bestStars)) }
        save()
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
            ?.apply()
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/game/GameScreen.kt">
package com.example.energokomendant.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.data.model.EventChoice
import com.example.energokomendant.data.model.GameEvent
import com.example.energokomendant.data.model.GameState
import com.example.energokomendant.data.model.Level
import com.example.energokomendant.data.model.Upgrade

@Composable
fun GameScreen(
    level: Level,
    gameState: GameState,
    onBuyUpgrade: (Upgrade) -> Unit,
    onResolveEvent: (EventChoice) -> Unit,
    onNextDay: () -> Unit,
    onBackToMenu: () -> Unit
) {
    BackHandler { onBackToMenu() }
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(level.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text("День ${gameState.currentDay}/${gameState.maxDays}", fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        DayDots(current = gameState.currentDay, total = gameState.maxDays)

        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            HouseView(
                levelId = level.id,
                purchasedUpgrades = gameState.purchasedUpgrades,
                modifier = Modifier.fillMaxHeight().aspectRatio(0.95f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        StatRow(gameState)
        Spacer(modifier = Modifier.height(4.dp))

        ForecastRow(gameState = gameState, level = level)

        Spacer(modifier = Modifier.height(6.dp))

        if (gameState.currentEvent != null && !gameState.eventResolved) {
            EventCard(
                event = gameState.currentEvent,
                onChoose = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onResolveEvent(it)
                }
            )
        }

        AnimatedVisibility(visible = gameState.lastActionMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (gameState.isBudgetCritical) MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = gameState.lastActionMessage ?: "",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item { Text("🛒 Магазин улучшений", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            items(level.upgrades) { upgrade ->
                val isPurchased = gameState.purchasedUpgrades.contains(upgrade.id)
                AnimatedUpgradeCard(
                    upgrade = upgrade,
                    isPurchased = isPurchased,
                    canAfford = gameState.budget >= upgrade.cost,
                    onBuy = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onBuyUpgrade(upgrade)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (gameState.isLevelCompleted || gameState.isGameOver) {
            ResultCard(gameState = gameState, onBackToMenu = onBackToMenu)
        } else {
            Button(
                onClick = onNextDay,
                enabled = gameState.eventResolved,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(
                    when {
                        !gameState.eventResolved -> "Сначала прими решение"
                        else -> "Закончить день (${if (gameState.forecastNet >= 0) "+" else ""}${gameState.forecastNet} ₽)"
                    },
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ForecastRow(gameState: GameState, level: Level) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (gameState.forecastNet < 0)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Прогноз за день:", fontSize = 11.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("+${gameState.forecastIncome}₽", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                Text("−${gameState.forecastBill}₽", fontSize = 12.sp, color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                Text(
                    "= ${if (gameState.forecastNet >= 0) "+" else ""}${gameState.forecastNet}₽",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (gameState.forecastNet < 0) Color(0xFFC62828) else Color(0xFF2E7D32)
                )
            }
        }
    }
}

@Composable
fun EventCard(event: GameEvent, onChoose: (EventChoice) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("📢 ${event.title}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(event.description, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            event.choices.forEach { choice ->
                Button(
                    onClick = { onChoose(choice) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (choice.isRisky) Color(0xFFD84315)
                        else MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(
                        if (choice.isRisky) "🎲 ${choice.label}" else choice.label,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DayDots(current: Int, total: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(total) { i ->
            val dayNum = i + 1
            val isActive = dayNum < current
            val isCurrent = dayNum == current
            val size = if (isCurrent) 14.dp else 10.dp
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCurrent -> MaterialTheme.colorScheme.primary
                            isActive -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
            )
        }
    }
}

@Composable
fun StatRow(gameState: GameState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedStatBox(
            label = "Бюджет",
            value = "${gameState.budget}₽",
            modifier = Modifier.weight(1f),
            isCritical = gameState.budget < 50
        )
        AnimatedStatBox("Расход", "${gameState.consumption} кВт", Modifier.weight(1f), false)
        AnimatedStatBox("Комфорт", "${gameState.comfort}%", Modifier.weight(1f), gameState.comfort < 60)
    }
}

@Composable
fun AnimatedStatBox(label: String, value: String, modifier: Modifier = Modifier, isCritical: Boolean) {
    val bg by animateColorAsState(
        targetValue = if (isCritical) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(500),
        label = "statBg"
    )
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = bg)) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 10.sp)
            AnimatedContent(
                targetState = value,
                transitionSpec = { fadeIn() + scaleIn() togetherWith fadeOut() },
                label = "statValue"
            ) { v ->
                Text(v, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AnimatedUpgradeCard(
    upgrade: Upgrade,
    isPurchased: Boolean,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = if (isPurchased) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(400),
        label = "bg"
    )
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = bg)) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(upgrade.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(upgrade.description, fontSize = 10.sp)
                Text(
                    "${upgrade.cost}₽ | −${-upgrade.consumptionReduction}кВт | ${if (upgrade.comfortChange >= 0) "+" else ""}${upgrade.comfortChange}%",
                    fontSize = 11.sp
                )
            }
            if (isPurchased) {
                Text("✓", fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
            } else {
                Button(onClick = onBuy, enabled = canAfford) {
                    Text("Купить", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ResultCard(gameState: GameState, onBackToMenu: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (gameState.isLevelCompleted) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (gameState.isLevelCompleted) {
                Text("🏆 Победа!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if (gameState.rewardEarned > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "+${gameState.rewardEarned} ⚡",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57F17)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "★".repeat(gameState.starsEarned) + "☆".repeat(3 - gameState.starsEarned),
                    fontSize = 30.sp
                )
            } else {
                Text("💀 Провал", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(gameState.lastActionMessage ?: "Цель не достигнута.", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth()) {
                Text("В меню")
            }
        }
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/game/HouseVisualizer.kt">
package com.example.energokomendant.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

data class HouseWindowState(
    val color: Color,
    val glow: Float
)

private val COLOR_WASTE = Color(0xFFFFB74D)
private val COLOR_EFFICIENT = Color(0xFFE3F2FD)
private val COLOR_DIM = Color(0xFF546E7A)
private val COLOR_OFF = Color(0xFF1A1A1A)
private val COLOR_SOLAR = Color(0xFFFFF59D)
private val COLOR_INSULATED = Color(0xFF90CAF9)

@Composable
fun HouseView(
    levelId: Int,
    purchasedUpgrades: List<String>,
    modifier: Modifier = Modifier
) {
    val windowStates = remember(levelId, purchasedUpgrades) {
        computeWindowStates(levelId, purchasedUpgrades.toSet())
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val bodyTop = h * 0.32f
            val bodyBottom = h * 0.97f
            val bodyLeft = w * 0.15f
            val bodyRight = w * 0.85f

            // Корпус здания
            drawRect(
                color = Color(0xFF37474F),
                topLeft = Offset(bodyLeft, bodyTop),
                size = Size(bodyRight - bodyLeft, bodyBottom - bodyTop)
            )

            // Крыша
            val roofPath = Path().apply {
                moveTo(bodyLeft - w * 0.03f, bodyTop)
                lineTo(w / 2f, h * 0.05f)
                lineTo(bodyRight + w * 0.03f, bodyTop)
                close()
            }
            drawPath(roofPath, Color(0xFF6D4C41))

            // Труба
            drawRect(
                color = Color(0xFF5D4037),
                topLeft = Offset(w * 0.72f, h * 0.14f),
                size = Size(w * 0.06f, h * 0.14f)
            )
        }

        // Сетка окон 3x3
        Column(
            modifier = Modifier
                .fillMaxWidth(0.56f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (col in 0 until 3) {
                        val state = windowStates[row * 3 + col]
                        AnimatedWindow(
                            state = state,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedWindow(
    state: HouseWindowState,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        targetValue = state.color,
        animationSpec = tween(500),
        label = "windowColor"
    )
    val animatedGlow by animateFloatAsState(
        targetValue = state.glow,
        animationSpec = tween(500),
        label = "windowGlow"
    )

    Box(
        modifier = modifier.drawBehind {
            // Свечение
            if (animatedGlow > 0.01f) {
                drawRoundRect(
                    color = animatedColor.copy(alpha = 0.3f * animatedGlow),
                    cornerRadius = CornerRadius(size.minDimension * 0.4f)
                )
            }
            // Рама окна
            val outerInset = size.minDimension * 0.10f
            drawRoundRect(
                color = Color(0xFF1A1A1A),
                topLeft = Offset(outerInset, outerInset),
                size = Size(size.width - 2 * outerInset, size.height - 2 * outerInset),
                cornerRadius = CornerRadius(3f)
            )
            // Стекло
            val innerInset = size.minDimension * 0.18f
            drawRoundRect(
                color = animatedColor,
                topLeft = Offset(innerInset, innerInset),
                size = Size(size.width - 2 * innerInset, size.height - 2 * innerInset),
                cornerRadius = CornerRadius(2f)
            )
        }
    )
}

private fun computeWindowStates(levelId: Int, purchased: Set<String>): List<HouseWindowState> {
    val states = MutableList(9) {
        HouseWindowState(color = COLOR_WASTE, glow = 1f)
    }

    when (levelId) {
        1 -> {
            if ("led" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_EFFICIENT)
            }
            if ("sensors" in purchased) {
                for (i in listOf(0, 2, 6, 8)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.4f)
            }
            if ("off_light" in purchased) {
                for (i in listOf(6, 7, 8)) states[i] = states[i].copy(color = COLOR_OFF, glow = 0f)
            }
        }
        2 -> {
            if ("windows" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_INSULATED)
            }
            if ("temp_down" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_INSULATED, glow = 0.5f)
            }
        }
        3 -> {
            if ("fridge" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_EFFICIENT)
            }
            if ("wash" in purchased) {
                for (i in listOf(6, 7, 8)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.4f)
            }
            if ("standby" in purchased) {
                for (i in listOf(0, 2)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.3f)
            }
        }
        4 -> {
            if ("solar" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_SOLAR, glow = 0.8f)
            }
        }
        5 -> {
            if ("smart_plugs" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_EFFICIENT)
            }
            if ("temp_sensors" in purchased) {
                for (i in listOf(0, 2, 6, 8)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.3f)
            }
        }
    }

    return states
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/hr/HRScreen.kt">
package com.example.energokomendant.ui.hr

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.data.Catalog
import com.example.energokomendant.data.Employee
import com.example.energokomendant.data.MetaState

@Composable
fun HRScreen(
    meta: MetaState,
    onHire: (Employee) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Назад") }
            Text("👥 Отдел кадров", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Text(
                    "⚡ ${meta.coins}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Сотрудники работают постоянно, нанимаются один раз.", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Catalog.employees) { emp ->
                val hired = meta.employees.contains(emp.id)
                EmployeeCard(
                    employee = emp,
                    hired = hired,
                    canAfford = meta.coins >= emp.cost,
                    onHire = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onHire(emp)
                    }
                )
            }
        }
    }
}

@Composable
private fun EmployeeCard(
    employee: Employee,
    hired: Boolean,
    canAfford: Boolean,
    onHire: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (hired) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(employee.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(employee.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(employee.description, fontSize = 11.sp)
                Text("${employee.cost} ⚡", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            if (hired) {
                Text("✓", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
            } else {
                Button(onClick = onHire, enabled = canAfford) {
                    Text("Нанять")
                }
            }
        }
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/menu/CityMapScreen.kt">
package com.example.energokomendant.ui.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CityMapScreen(
    coins: Int,
    onOpenHouse: () -> Unit,
    onOpenSubstation: () -> Unit,
    onOpenSchool: () -> Unit,
    onOpenSuppliers: () -> Unit,
    onOpenHR: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("ЭнергоКомендант", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("Выбери объект", fontSize = 13.sp)
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF59D))
            ) {
                Text(
                    "⚡ $coins",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        CityObjectCard(
            title = "🏠 Дом",
            subtitle = "Кампания · 5 уровней · награда +50⚡",
            color = Color(0xFF1976D2),
            locked = false,
            onClick = onOpenHouse
        ) { mod ->
            Canvas(modifier = mod) {
                val w = size.width; val h = size.height
                drawRect(Color(0xFF37474F), Offset(w*0.15f, h*0.45f), Size(w*0.7f, h*0.5f))
                val roof = Path().apply {
                    moveTo(w*0.1f, h*0.45f); lineTo(w*0.5f, h*0.1f); lineTo(w*0.9f, h*0.45f); close()
                }
                drawPath(roof, Color(0xFF6D4C41))
                drawRect(Color(0xFFFFF59D), Offset(w*0.25f, h*0.55f), Size(w*0.15f, h*0.15f))
                drawRect(Color(0xFFFFF59D), Offset(w*0.6f, h*0.55f), Size(w*0.15f, h*0.15f))
                drawRect(Color(0xFFFFF59D), Offset(w*0.25f, h*0.75f), Size(w*0.15f, h*0.15f))
                drawRect(Color(0xFFFFF59D), Offset(w*0.6f, h*0.75f), Size(w*0.15f, h*0.15f))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        CityObjectCard(
            title = "⚡ Подстанция",
            subtitle = "Аркада · 60 сек · награда +30⚡",
            color = Color(0xFFF57C00),
            locked = false,
            onClick = onOpenSubstation
        ) { mod ->
            Canvas(modifier = mod) {
                val w = size.width; val h = size.height
                drawRect(Color(0xFF5D4037), Offset(w*0.45f, h*0.15f), Size(w*0.1f, h*0.75f))
                drawRect(Color(0xFF5D4037), Offset(w*0.2f, h*0.25f), Size(w*0.6f, h*0.06f))
                drawRect(Color(0xFF5D4037), Offset(w*0.25f, h*0.4f), Size(w*0.5f, h*0.05f))
                drawRect(Color(0xFF455A64), Offset(w*0.25f, h*0.6f), Size(w*0.5f, h*0.3f))
                val bolt = Path().apply {
                    moveTo(w*0.55f, h*0.1f); lineTo(w*0.4f, h*0.45f); lineTo(w*0.5f, h*0.45f)
                    lineTo(w*0.45f, h*0.75f); lineTo(w*0.65f, h*0.4f); lineTo(w*0.55f, h*0.4f)
                    lineTo(w*0.6f, h*0.1f); close()
                }
                drawPath(bolt, Color(0xFFFFEB3B))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(modifier = Modifier.weight(1f)) {
                CityObjectCard(
                    title = "🏪 Поставщики",
                    subtitle = "Предметы за ⚡",
                    color = Color(0xFF7B1FA2),
                    locked = false,
                    compact = true,
                    onClick = onOpenSuppliers
                ) { mod ->
                    Canvas(modifier = mod) {
                        val w = size.width; val h = size.height
                        drawRect(Color(0xFF6A1B9A), Offset(w*0.15f, h*0.4f), Size(w*0.7f, h*0.55f))
                        drawRect(Color(0xFF9C27B0), Offset(w*0.1f, h*0.3f), Size(w*0.8f, h*0.15f))
                        drawRect(Color(0xFFAED581), Offset(w*0.25f, h*0.55f), Size(w*0.12f, h*0.15f))
                        drawRect(Color(0xFFAED581), Offset(w*0.45f, h*0.55f), Size(w*0.12f, h*0.15f))
                        drawRect(Color(0xFFAED581), Offset(w*0.65f, h*0.55f), Size(w*0.12f, h*0.15f))
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                CityObjectCard(
                    title = "👥 HR",
                    subtitle = "Сотрудники за ⚡",
                    color = Color(0xFF00838F),
                    locked = false,
                    compact = true,
                    onClick = onOpenHR
                ) { mod ->
                    Canvas(modifier = mod) {
                        val w = size.width; val h = size.height
                        // офисное здание
                        drawRect(Color(0xFF37474F), Offset(w*0.2f, h*0.2f), Size(w*0.6f, h*0.75f))
                        // окна
                        for (r in 0..2) for (c in 0..1) {
                            drawRect(
                                Color(0xFFB3E5FC),
                                Offset(w*(0.28f + c*0.28f), h*(0.3f + r*0.22f)),
                                Size(w*0.14f, h*0.14f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        CityObjectCard(
            title = "🏫 Школа",
            subtitle = "Головоломка · скоро",
            color = Color(0xFF388E3C),
            locked = true,
            onClick = onOpenSchool
        ) { mod ->
            Canvas(modifier = mod) {
                val w = size.width; val h = size.height
                drawRect(Color(0xFF8D6E63), Offset(w*0.15f, h*0.35f), Size(w*0.7f, h*0.6f))
                drawRect(Color(0xFF5D4037), Offset(w*0.1f, h*0.3f), Size(w*0.8f, h*0.08f))
                drawRect(Color(0xFF3E2723), Offset(w*0.42f, h*0.7f), Size(w*0.16f, h*0.25f))
                drawRect(Color(0xFFB3E5FC), Offset(w*0.22f, h*0.45f), Size(w*0.12f, h*0.15f))
                drawRect(Color(0xFFB3E5FC), Offset(w*0.66f, h*0.45f), Size(w*0.12f, h*0.15f))
                drawRect(Color(0xFFB3E5FC), Offset(w*0.22f, h*0.65f), Size(w*0.12f, h*0.15f))
                drawRect(Color(0xFFB3E5FC), Offset(w*0.66f, h*0.65f), Size(w*0.12f, h*0.15f))
            }
        }
    }
}

@Composable
private fun CityObjectCard(
    title: String,
    subtitle: String,
    color: Color,
    locked: Boolean,
    compact: Boolean = false,
    onClick: () -> Unit,
    icon: @Composable (Modifier) -> Unit
) {
    Card(
        onClick = onClick,
        enabled = !locked,
        modifier = Modifier.fillMaxWidth().height(if (compact) 110.dp else 100.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (locked) MaterialTheme.colorScheme.surfaceVariant
            else color.copy(alpha = 0.12f)
        ),
        border = BorderStroke(2.dp, if (locked) MaterialTheme.colorScheme.outlineVariant else color)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(if (compact) 8.dp else 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(if (compact) 56.dp else 64.dp)) {
                icon(Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(if (compact) 8.dp else 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = if (compact) 14.sp else 17.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = if (compact) 10.sp else 11.sp)
                if (locked) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text("🔒 В разработке", fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                }
            }
            if (!locked && !compact) {
                Text("→", fontSize = 24.sp, color = color)
            }
        }
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/substation/SubstationScreen.kt">
package com.example.energokomendant.ui.substation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.viewmodel.SubstationState

@Composable
fun SubstationScreen(
    state: SubstationState,
    onTapLine: (Int) -> Unit,
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Назад") }
            Text("⚡ Подстанция", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("${state.timeLeft.toInt()}с", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LoadGauge(load = state.load, spikeFlash = state.spikeFlash)

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Держи нагрузку сети под контролем.", fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("• Нагрузка растёт сама", fontSize = 12.sp)
                Text("• Случайные пики +15-25%", fontSize = 12.sp)
                Text("• Жми кнопки линий — сбрасывают −20%", fontSize = 12.sp)
                Text("• Кулдаун линии 2 секунды", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Продержись 60 секунд, не дав сети перегореть.", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("🏆 Награда за победу: +30 ⚡", fontSize = 12.sp, color = Color(0xFFF57F17), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (i in 0..2) {
                LineButton(
                    index = i,
                    cooldown = state.cooldowns[i],
                    enabled = state.isRunning,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onTapLine(i)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isFinished) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.isWin) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (state.isWin) "🏆 Сеть выдержала!" else "💥 Сеть перегорела",
                        fontSize = 20.sp, fontWeight = FontWeight.Bold
                    )
                    if (state.isWin && state.rewardEarned > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "+${state.rewardEarned} ⚡",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57F17)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
                        Text("Ещё раз")
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                        Text("На карту")
                    }
                }
            }
        } else if (!state.isRunning) {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Начать смену", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Смена идёт...", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun LoadGauge(load: Float, spikeFlash: Boolean) {
    val progress by animateFloatAsState(
        targetValue = (load / 100f).coerceIn(0f, 1f),
        animationSpec = tween(200),
        label = "load"
    )
    val color by animateColorAsState(
        targetValue = when {
            load > 85f -> Color(0xFFD32F2F)
            load > 65f -> Color(0xFFF57C00)
            else -> Color(0xFF388E3C)
        },
        animationSpec = tween(300),
        label = "loadColor"
    )
    val flash by rememberInfiniteTransition(label = "flash").animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flashAlpha"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Нагрузка сети", fontSize = 13.sp)
            Text("${load.toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF263238))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (spikeFlash) color.copy(alpha = flash)
                        else color
                    )
            )
        }
    }
}

@Composable
fun LineButton(
    index: Int,
    cooldown: Float,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val ready = cooldown <= 0f && enabled
    val bg by animateColorAsState(
        targetValue = if (ready) Color(0xFF1976D2) else Color(0xFF546E7A),
        animationSpec = tween(300),
        label = "lineBg"
    )

    val title = when(index) {
        0 -> "Аварийный"
        1 -> "Штатный"
        else -> "Форсаж"
    }
    val subtitle = when(index) {
        0 -> "-30%"
        1 -> "-20%"
        else -> "-40%"
    }

    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        if (ready) {
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = Color.White, fontSize = 12.sp)
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                Text("${cooldown.toInt() + 1}с", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/suppliers/SupplierScreen.kt">
package com.example.energokomendant.ui.suppliers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.data.Catalog
import com.example.energokomendant.data.MetaState
import com.example.energokomendant.data.ShopItem

@Composable
fun SupplierScreen(
    meta: MetaState,
    onBuy: (ShopItem) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Назад") }
            Text("🏪 Поставщики", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Text(
                    "⚡ ${meta.coins}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Постоянные улучшения — покупаются один раз.", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Catalog.items) { item ->
                val owned = meta.ownedItems.contains(item.id)
                ShopItemCard(
                    item = item,
                    owned = owned,
                    canAfford = meta.coins >= item.cost,
                    onBuy = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onBuy(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    item: ShopItem,
    owned: Boolean,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (owned) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(item.description, fontSize = 11.sp)
                Text("${item.cost} ⚡", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            if (owned) {
                Text("✓", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
            } else {
                Button(onClick = onBuy, enabled = canAfford) {
                    Text("Купить")
                }
            }
        }
    }
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/theme/Color.kt">
package com.example.energokomendant.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/theme/Theme.kt">
package com.example.energokomendant.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun EnergoKomendantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
</file>

<file path="app/src/main/java/com/example/energokomendant/ui/theme/Type.kt">
package com.example.energokomendant.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
</file>

<file path="app/src/main/java/com/example/energokomendant/viewmodel/GameViewModel.kt">
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
            currentEvent = LevelRepository.getRandomEvent(levelId, 1),
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

        // Доход теперь x10, чтобы игрок мог выживать на старте
        val income = state.comfort * 10 + incomeBonus
        val bill = state.consumption * level.tariff
        val newBudget = state.budget + income - bill

        // Убрали тупую деградацию. Теперь выживание зависит только от решений игрока.
        val newConsumption = state.consumption
        val newComfort = state.comfort

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
                    currentEvent = LevelRepository.getRandomEvent(level.id, nextDayNum),
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
        val income = comfort * 10 + incomeBonus
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
</file>

<file path="app/src/main/java/com/example/energokomendant/viewmodel/SubstationViewModel.kt">
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
        val total = 60f // Убрали бонусное время, теперь Стабилизатор работает иначе
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

        // Теперь и Стабилизатор, и Диспетчер замедляют рост нагрузки
        val growthMultiplier = when {
            MetaRepository.hasEmployee("dispatcher") && MetaRepository.hasItem("stabilizer") -> 0.5f
            MetaRepository.hasEmployee("dispatcher") -> 0.7f
            MetaRepository.hasItem("stabilizer") -> 0.8f
            else -> 1f
        }
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

        // Теперь у каждой линии своя сила и свой кулдаун
        val (reduction, cooldown) = when (index) {
            0 -> 30f to 4f // Аварийный: сильно сбрасывает, долгий кулдаун
            1 -> 20f to 2f // Штатный: стандартный
            else -> 40f to 6f // Форсаж: очень сильно сбрасывает, очень долгий кулдаун
        }

        val newLoad = (s.load - reduction).coerceAtLeast(0f)
        val newCd = s.cooldowns.toMutableList().also { it[index] = cooldown }
        _state.value = s.copy(load = newLoad, cooldowns = newCd)
    }

    fun reset() {
        _state.value = SubstationState()
    }
}
</file>

<file path="app/src/main/res/drawable/ic_launcher_background.xml">
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#3DDC84"
        android:pathData="M0,0h108v108h-108z" />
    <path
        android:fillColor="#00000000"
        android:pathData="M9,0L9,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,0L19,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,0L29,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,0L39,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,0L49,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,0L59,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,0L69,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,0L79,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M89,0L89,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M99,0L99,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,9L108,9"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,19L108,19"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,29L108,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,39L108,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,49L108,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,59L108,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,69L108,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,79L108,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,89L108,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,99L108,99"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,29L89,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,39L89,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,49L89,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,59L89,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,69L89,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,79L89,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,19L29,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,19L39,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,19L49,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,19L59,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,19L69,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,19L79,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
</vector>
</file>

<file path="app/src/main/res/drawable/ic_launcher_foreground.xml">
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path android:pathData="M31,63.928c0,0 6.4,-11 12.1,-13.1c7.2,-2.6 26,-1.4 26,-1.4l38.1,38.1L107,108.928l-32,-1L31,63.928z">
        <aapt:attr name="android:fillColor">
            <gradient
                android:endX="85.84757"
                android:endY="92.4963"
                android:startX="42.9492"
                android:startY="49.59793"
                android:type="linear">
                <item
                    android:color="#44000000"
                    android:offset="0.0" />
                <item
                    android:color="#00000000"
                    android:offset="1.0" />
            </gradient>
        </aapt:attr>
    </path>
    <path
        android:fillColor="#FFFFFF"
        android:fillType="nonZero"
        android:pathData="M65.3,45.828l3.8,-6.6c0.2,-0.4 0.1,-0.9 -0.3,-1.1c-0.4,-0.2 -0.9,-0.1 -1.1,0.3l-3.9,6.7c-6.3,-2.8 -13.4,-2.8 -19.7,0l-3.9,-6.7c-0.2,-0.4 -0.7,-0.5 -1.1,-0.3C38.8,38.328 38.7,38.828 38.9,39.228l3.8,6.6C36.2,49.428 31.7,56.028 31,63.928h46C76.3,56.028 71.8,49.428 65.3,45.828zM43.4,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2c-0.3,-0.7 -0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C45.3,56.528 44.5,57.328 43.4,57.328L43.4,57.328zM64.6,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2s-0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C66.5,56.528 65.6,57.328 64.6,57.328L64.6,57.328z"
        android:strokeWidth="1"
        android:strokeColor="#00000000" />
</vector>
</file>

<file path="app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml">
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
</file>

<file path="app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml">
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
</file>

<file path="app/src/main/res/values/colors.xml">
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
</file>

<file path="app/src/main/res/values/strings.xml">
<resources>
    <string name="app_name">EnergoKomendant</string>
</resources>
</file>

<file path="app/src/main/res/values/themes.xml">
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <style name="Theme.EnergoKomendant" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
</file>

<file path="app/src/main/res/xml/backup_rules.xml">
<?xml version="1.0" encoding="utf-8"?><!--
   Sample backup rules file; uncomment and customize as necessary.
   See https://developer.android.com/guide/topics/data/autobackup
   for details.
   Note: This file is ignored for devices older than API 31
   See https://developer.android.com/about/versions/12/backup-restore
-->
<full-backup-content>
    <!--
   <include domain="sharedpref" path="."/>
   <exclude domain="sharedpref" path="device.xml"/>
-->
</full-backup-content>
</file>

<file path="app/src/main/res/xml/data_extraction_rules.xml">
<?xml version="1.0" encoding="utf-8"?><!--
   Sample data extraction rules file; uncomment and customize as necessary.
   See https://developer.android.com/about/versions/12/backup-restore#xml-changes
   for details.
-->
<data-extraction-rules>
    <cloud-backup>
        <!-- TODO: Use <include> and <exclude> to control what is backed up.
        <include .../>
        <exclude .../>
        -->
    </cloud-backup>
    <!--
    <device-transfer>
        <include .../>
        <exclude .../>
    </device-transfer>
    -->
</data-extraction-rules>
</file>

<file path="app/src/main/AndroidManifest.xml">
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.EnergoKomendant">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.EnergoKomendant">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
</file>

<file path="app/src/test/java/com/example/energokomendant/ExampleUnitTest.kt">
package com.example.energokomendant

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
</file>

<file path="app/.gitignore">
/build
</file>

<file path="app/proguard-rules.pro">
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
</file>

<file path="gradle/wrapper/gradle-wrapper.properties">
#Sun Oct 18 18:48:18 MSK 2026
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.13-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
</file>

<file path="build.gradle.kts">
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
</file>

<file path="gradle.properties">
# Project-wide Gradle settings.
# IDE (e.g. Android Studio) users:
# Gradle settings configured through the IDE *will override*
# any settings specified in this file.
# For more details on how to configure your build environment visit
# http://www.gradle.org/docs/current/userguide/build_environment.html
# Specifies the JVM arguments used for the daemon process.
# The setting is particularly useful for tweaking memory settings.
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
# When configured, Gradle will run in incubating parallel mode.
# This option should only be used with decoupled projects. For more details, visit
# https://developer.android.com/r/tools/gradle-multi-project-decoupled-projects
# org.gradle.parallel=true
# AndroidX package structure to make it clearer which packages are bundled with the
# Android operating system, and which are packaged with your app's APK
# https://developer.android.com/topic/libraries/support-library/androidx-rn
android.useAndroidX=true
# Kotlin code style for this project: "official" or "obsolete":
kotlin.code.style=official
# Enables namespacing of each library's R class so that its R class includes only the
# resources declared in the library itself and none from the library's dependencies,
# thereby reducing the size of the R class for that library
android.nonTransitiveRClass=true
</file>

<file path="gradlew">
#!/bin/sh

#
# Copyright © 2015 the original authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0
#

##############################################################################
#
#   Gradle start up script for POSIX generated by Gradle.
#
#   Important for running:
#
#   (1) You need a POSIX-compliant shell to run this script. If your /bin/sh is
#       noncompliant, but you have some other compliant shell such as ksh or
#       bash, then to run this script, type that shell name before the whole
#       command line, like:
#
#           ksh Gradle
#
#       Busybox and similar reduced shells will NOT work, because this script
#       requires all of these POSIX shell features:
#         * functions;
#         * expansions «$var», «${var}», «${var:-default}», «${var+SET}»,
#           «${var#prefix}», «${var%suffix}», and «$( cmd )»;
#         * compound commands having a testable exit status, especially «case»;
#         * various built-in commands including «command», «set», and «ulimit».
#
#   Important for patching:
#
#   (2) This script targets any POSIX shell, so it avoids extensions provided
#       by Bash, Ksh, etc; in particular arrays are avoided.
#
#       The "traditional" practice of packing multiple parameters into a
#       space-separated string is a well documented source of bugs and security
#       problems, so this is (mostly) avoided, by progressively accumulating
#       options in "$@", and eventually passing that to Java.
#
#       Where the inherited environment variables (DEFAULT_JVM_OPTS, JAVA_OPTS,
#       and GRADLE_OPTS) rely on word-splitting, this is performed explicitly;
#       see the in-line comments for details.
#
#       There are tweaks for specific operating systems such as AIX, CygWin,
#       Darwin, MinGW, and NonStop.
#
#   (3) This script is generated from the Groovy template
#       https://github.com/gradle/gradle/blob/HEAD/platforms/jvm/plugins-application/src/main/resources/org/gradle/api/internal/plugins/unixStartScript.txt
#       within the Gradle project.
#
#       You can find Gradle at https://github.com/gradle/gradle/.
#
##############################################################################

# Attempt to set APP_HOME

# Resolve links: $0 may be a link
app_path=$0

# Need this for daisy-chained symlinks.
while
    APP_HOME=${app_path%"${app_path##*/}"}  # leaves a trailing /; empty if no leading path
    [ -h "$app_path" ]
do
    ls=$( ls -ld "$app_path" )
    link=${ls#*' -> '}
    case $link in             #(
      /*)   app_path=$link ;; #(
      *)    app_path=$APP_HOME$link ;;
    esac
done

# This is normally unused
# shellcheck disable=SC2034
APP_BASE_NAME=${0##*/}
# Discard cd standard output in case $CDPATH is set (https://github.com/gradle/gradle/issues/25036)
APP_HOME=$( cd -P "${APP_HOME:-./}" > /dev/null && printf '%s\n' "$PWD" ) || exit

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

warn () {
    echo "$*"
} >&2

die () {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "$( uname )" in                #(
  CYGWIN* )         cygwin=true  ;; #(
  Darwin* )         darwin=true  ;; #(
  MSYS* | MINGW* )  msys=true    ;; #(
  NONSTOP* )        nonstop=true ;;
esac

CLASSPATH="\\\"\\\""


# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD=$JAVA_HOME/jre/sh/java
    else
        JAVACMD=$JAVA_HOME/bin/java
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD=java
    if ! command -v java >/dev/null 2>&1
    then
        die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
fi

# Increase the maximum file descriptors if we can.
if ! "$cygwin" && ! "$darwin" && ! "$nonstop" ; then
    case $MAX_FD in #(
      max*)
        # In POSIX sh, ulimit -H is undefined. That's why the result is checked to see if it worked.
        # shellcheck disable=SC2039,SC3045
        MAX_FD=$( ulimit -H -n ) ||
            warn "Could not query maximum file descriptor limit"
    esac
    case $MAX_FD in  #(
      '' | soft) :;; #(
      *)
        # In POSIX sh, ulimit -n is undefined. That's why the result is checked to see if it worked.
        # shellcheck disable=SC2039,SC3045
        ulimit -n "$MAX_FD" ||
            warn "Could not set maximum file descriptor limit to $MAX_FD"
    esac
fi

# Collect all arguments for the java command, stacking in reverse order:
#   * args from the command line
#   * the main class name
#   * -classpath
#   * -D...appname settings
#   * --module-path (only if needed)
#   * DEFAULT_JVM_OPTS, JAVA_OPTS, and GRADLE_OPTS environment variables.

# For Cygwin or MSYS, switch paths to Windows format before running java
if "$cygwin" || "$msys" ; then
    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
    CLASSPATH=$( cygpath --path --mixed "$CLASSPATH" )

    JAVACMD=$( cygpath --unix "$JAVACMD" )

    # Now convert the arguments - kludge to limit ourselves to /bin/sh
    for arg do
        if
            case $arg in                                #(
              -*)   false ;;                            # don't mess with options #(
              /?*)  t=${arg#/} t=/${t%%/*}              # looks like a POSIX filepath
                    [ -e "$t" ] ;;                      #(
              *)    false ;;
            esac
        then
            arg=$( cygpath --path --ignore --mixed "$arg" )
        fi
        # Roll the args list around exactly as many times as the number of
        # args, so each arg winds up back in the position where it started, but
        # possibly modified.
        #
        # NB: a `for` loop captures its iteration list before it begins, so
        # changing the positional parameters here affects neither the number of
        # iterations, nor the values presented in `arg`.
        shift                   # remove old arg
        set -- "$@" "$arg"      # push replacement arg
    done
fi


# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Collect all arguments for the java command:
#   * DEFAULT_JVM_OPTS, JAVA_OPTS, and optsEnvironmentVar are not allowed to contain shell fragments,
#     and any embedded shellness will be escaped.
#   * For example: A user cannot expect ${Hostname} to be expanded, as it is an environment variable and will be
#     treated as '${Hostname}' itself on the command line.

set -- \
        "-Dorg.gradle.appname=$APP_BASE_NAME" \
        -classpath "$CLASSPATH" \
        -jar "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
        "$@"

# Stop when "xargs" is not available.
if ! command -v xargs >/dev/null 2>&1
then
    die "xargs is not available"
fi

# Use "xargs" to parse quoted args.
#
# With -n1 it outputs one arg per line, with the quotes and backslashes removed.
#
# In Bash we could simply go:
#
#   readarray ARGS < <( xargs -n1 <<<"$var" ) &&
#   set -- "${ARGS[@]}" "$@"
#
# but POSIX shell has neither arrays nor command substitution, so instead we
# post-process each arg (as a line of input to sed) to backslash-escape any
# character that might be a shell metacharacter, then use eval to reverse
# that process (while maintaining the separation between arguments), and wrap
# the whole thing up as a single "set" statement.
#
# This will of course break if any of these variables contains a newline or
# an unmatched quote.
#

eval "set -- $(
        printf '%s\n' "$DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS" |
        xargs -n1 |
        sed ' s~[^-[:alnum:]+,./:=@_]~\\&~g; ' |
        tr '\n' ' '
    )" '"$@"'

exec "$JAVACMD" "$@"
</file>

<file path="gradlew.bat">
@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=


@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%GRADLE_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
</file>

<file path="settings.gradle.kts">
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "EnergoKomendant"
include(":app")
</file>

<file path="app/src/main/java/com/example/energokomendant/MainActivity.kt">
package com.example.energokomendant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.energokomendant.data.MetaRepository
import com.example.energokomendant.data.repository.LevelRepository
import com.example.energokomendant.ui.game.GameScreen
import com.example.energokomendant.ui.game.LevelSelectScreen
import com.example.energokomendant.ui.hr.HRScreen
import com.example.energokomendant.ui.menu.CityMapScreen
import com.example.energokomendant.ui.substation.SubstationScreen
import com.example.energokomendant.ui.suppliers.SupplierScreen
import com.example.energokomendant.viewmodel.GameViewModel
import com.example.energokomendant.viewmodel.SubstationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MetaRepository.init(this)
        setContent {
            MaterialTheme {
                AppNavigation(onExit = { finish() })
            }
        }
    }
}

sealed class Screen {
    object Menu : Screen()
    object LevelSelect : Screen()
    data class Game(val levelId: Int) : Screen()
    object Substation : Screen()
    object Suppliers : Screen()
    object HR : Screen()
}

@Composable
fun AppNavigation(onExit: () -> Unit) {
    val gameViewModel: GameViewModel = viewModel()
    val substationViewModel: SubstationViewModel = viewModel()

    val gameState by gameViewModel.gameState.collectAsState()
    val subState by substationViewModel.state.collectAsState()
    val metaState by MetaRepository.state.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Menu) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Сохраняем прогресс, когда уровень пройден
    LaunchedEffect(gameState.isLevelCompleted) {
        val levelId = gameState.currentLevelId
        if (gameState.isLevelCompleted && levelId != null) {
            MetaRepository.saveLevelCompleted(levelId, gameState.starsEarned)
        }
    }

    when (val screen = currentScreen) {
        is Screen.Menu -> {
            BackHandler { showExitDialog = true }
            CityMapScreen(
                coins = metaState.coins,
                onOpenHouse = { currentScreen = Screen.LevelSelect },
                onOpenSubstation = {
                    substationViewModel.reset()
                    currentScreen = Screen.Substation
                },
                onOpenSchool = { },
                onOpenSuppliers = { currentScreen = Screen.Suppliers },
                onOpenHR = { currentScreen = Screen.HR }
            )

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = { Text("Выйти из игры?") },
                    text = { Text("Прогресс сохраняется.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showExitDialog = false
                            onExit()
                        }) { Text("Выйти") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showExitDialog = false }) { Text("Отмена") }
                    }
                )
            }
        }
        is Screen.LevelSelect -> {
            LevelSelectScreen(
                completedLevels = metaState.completedLevels,
                onPlayLevel = { levelId ->
                    gameViewModel.startLevel(levelId)
                    currentScreen = Screen.Game(levelId)
                },
                onBack = { currentScreen = Screen.Menu }
            )
        }
        is Screen.Game -> {
            val level = LevelRepository.getLevels().find { it.id == screen.levelId }
            if (level != null) {
                GameScreen(
                    level = level,
                    gameState = gameState,
                    onBuyUpgrade = { gameViewModel.buyUpgrade(it) },
                    onResolveEvent = { gameViewModel.resolveEvent(it) },
                    onNextDay = { gameViewModel.nextDay() },
                    onBackToMenu = {
                        gameViewModel.reset()
                        currentScreen = Screen.LevelSelect
                    }
                )
            }
        }
        is Screen.Substation -> {
            SubstationScreen(
                state = subState,
                onTapLine = { substationViewModel.tapLine(it) },
                onStart = { substationViewModel.start() },
                onBack = {
                    substationViewModel.reset()
                    currentScreen = Screen.Menu
                }
            )
        }
        is Screen.Suppliers -> {
            SupplierScreen(
                meta = metaState,
                onBuy = { MetaRepository.buyItem(it.id, it.cost) },
                onBack = { currentScreen = Screen.Menu }
            )
        }
        is Screen.HR -> {
            HRScreen(
                meta = metaState,
                onHire = { MetaRepository.hireEmployee(it.id, it.cost) },
                onBack = { currentScreen = Screen.Menu }
            )
        }
    }
}
</file>

<file path="app/build.gradle.kts">
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.energokomendant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.energokomendant"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
</file>

<file path="gradle/libs.versions.toml">
[versions]
agp = "8.13.2"
kotlin = "2.0.21"
coreKtx = "1.13.1"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
lifecycleRuntimeKtx = "2.8.4"
activityCompose = "1.9.2"
composeBom = "2024.09.00"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
</file>

<file path=".gitignore">
*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
local.properties
repomix-output.mdrepomix-output.md
</file>

</files>
