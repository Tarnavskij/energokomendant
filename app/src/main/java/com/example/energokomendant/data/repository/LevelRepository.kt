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
                Upgrade(
                    "led", "LED-лампы", "Заменить лампы накаливания (−20 кВт)", 300, -20, 5,
                    fact = "LED-лампа расходует в 8 раз меньше энергии, чем лампа накаливания, и служит в 10 раз дольше."
                ),
                Upgrade(
                    "sensors", "Датчики движения", "Свет в пустых коридорах выключается (−20 кВт)", 400, -20, -5,
                    fact = "Датчики движения экономят до 30% электроэнергии на освещение коридоров и подъездов."
                ),
                Upgrade(
                    "off_light", "Свет по требованию", "Подсобки без постоянного света (−10 кВт)", 0, -10, -10,
                    fact = "Освещение пустых помещений — самая частая утечка энергии в зданиях: до 15% счёта."
                )
            )
        ),
        Level(
            id = 2, title = "Уровень 2: Тепло",
            description = "Отопление дорого. Сохрани тепло, не заморозив жильцов.",
            startBudget = 600, startConsumption = 120, startComfort = 80,
            targetConsumption = 90, minComfort = 50, maxDays = 5, tariff = 4,
            upgrades = listOf(
                Upgrade(
                    "windows", "Утеплить окна", "Стеклопакеты и герметизация (−30 кВт)", 600, -30, 15,
                    fact = "Через неутеплённые окна уходит до 30% тепла. Стеклопакет снижает потери в 2 раза."
                ),
                Upgrade(
                    "doors", "Доводчики", "Закрывают двери сами (−10 кВт)", 250, -10, 0,
                    fact = "Открытая входная дверь зимой выпускает столько тепла, сколько обогреватель за час работы."
                ),
                Upgrade(
                    "temp_down", "Снизить отопление", "Минус 2°C, жильцам прохладнее (−20 кВт)", 0, -20, -15,
                    fact = "Каждый градус ниже в отопительный сезон экономит 5-7% энергии на отопление."
                )
            )
        ),
        Level(
            id = 3, title = "Уровень 3: Техника",
            description = "Старая техника ест деньги. Обнови и научи жильцов.",
            startBudget = 800, startConsumption = 150, startComfort = 80,
            targetConsumption = 120, minComfort = 60, maxDays = 5, tariff = 4,
            upgrades = listOf(
                Upgrade(
                    "fridge", "Холодильник А++", "Экономит 40% (−40 кВт)", 800, -40, 5,
                    fact = "Холодильник класса А++ расходует на 40-50% меньше энергии, чем старый. Он работает 24/7."
                ),
                Upgrade(
                    "wash", "Стирка 30°C", "Экономия на нагреве (−20 кВт)", 0, -20, -5,
                    fact = "Стирка при 30°C вместо 60°C экономит до 50% энергии — нагрев воды — самая затратная часть."
                ),
                Upgrade(
                    "standby", "Убрать standby", "Приборы не жрут в режиме ожидания (−15 кВт)", 150, -15, 0,
                    fact = "Режим ожидания 10 приборов в квартире съедает до 300 кВт·ч в год — как месяц работы холодильника."
                )
            )
        ),
        Level(
            id = 4, title = "Уровень 4: Солнце",
            description = "Солнечные панели — дорого, но окупаются. Пробуй.",
            startBudget = 1000, startConsumption = 180, startComfort = 80,
            targetConsumption = 140, minComfort = 60, maxDays = 5, tariff = 5,
            upgrades = listOf(
                Upgrade(
                    "solar", "Солнечные панели", "Энергия от солнца (−60 кВт)", 1500, -60, 0,
                    fact = "Одна солнечная панель за год вырабатывает столько энергии, сколько нужно на 2000 часов работы LED-лампы."
                ),
                Upgrade(
                    "solar_water", "Солнечный нагреватель", "Горячая вода бесплатно (−30 кВт)", 900, -30, 5,
                    fact = "Солнечный водонагреватель покрывает до 70% годовой потребности в горячей воде в средней полосе."
                )
            )
        ),
        Level(
            id = 5, title = "Уровень 5: Умный дом",
            description = "Финальная проверка. Автоматизируй всё или разорись.",
            startBudget = 1500, startConsumption = 220, startComfort = 80,
            targetConsumption = 160, minComfort = 70, maxDays = 5, tariff = 6,
            upgrades = listOf(
                Upgrade(
                    "smart_plugs", "Умные розетки", "Отключают по расписанию (−30 кВт)", 800, -30, 5,
                    fact = "Умные розетки снимают фантомное потребление техники, когда её никто не использует."
                ),
                Upgrade(
                    "temp_sensors", "Датчики температуры", "Регулируют отопление (−20 кВт)", 600, -20, 10,
                    fact = "Датчики температуры позволяют держать в каждой комнате свою норму — не топить пустые помещения."
                ),
                Upgrade(
                    "auto_light", "Умный свет", "Свет по расписанию (−40 кВт)", 1000, -40, 5,
                    fact = "Автоматизация освещения по датчикам и расписанию экономит до 40% на освещении."
                )
            )
        )
    )

    fun getRandomEvent(levelId: Int, day: Int, purchased: Set<String> = emptySet()): GameEvent {
        val pool = eventsByLevel[levelId] ?: emptyList()
        if (pool.isEmpty()) return GameEvent("Спокойный день", "Ничего не случилось.", emptyList())

        // Фильтруем события, заблокированные купленными улучшениями
        val available = pool.filter { event ->
            event.blockedByUpgrade == null || event.blockedByUpgrade !in purchased
        }
        if (available.isEmpty()) {
            return GameEvent("Спокойный день", "В доме тихо — улучшения работают.", emptyList())
        }

        val base = available[(levelId * 10 + day * 3) % available.size]
        return applyModifiers(base, purchased)
    }

    private fun applyModifiers(event: GameEvent, purchased: Set<String>): GameEvent {
        val mitigator = event.mitigatedByUpgrade ?: return event
        if (mitigator !in purchased) return event

        // Улучшение сработало — уменьшаем эффект в 2 раза
        return event.copy(
            description = event.description + (event.mitigationNote?.let { "\n\n✅ $it" } ?: ""),
            choices = event.choices.map { c ->
                c.copy(
                    budgetDelta = c.budgetDelta / 2,
                    consumptionDelta = c.consumptionDelta / 2,
                    comfortDelta = c.comfortDelta / 2
                )
            }
        )
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
                ),
                blockedByUpgrade = "sensors"
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
                ),
                mitigatedByUpgrade = "windows",
                mitigationNote = "Утеплённые окна держат тепло — эффект ослаблен вдвое."
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
                ),
                blockedByUpgrade = "fridge"
            ),
            GameEvent(
                "Стиралка работает в пик",
                "Все стирают вечером — расход скачет.",
                listOf(
                    EventChoice("Договориться о дневной стирке", "Помогло.", consumptionDelta = -10, comfortDelta = -5),
                    EventChoice("Купить таймеры", "Автоматика.", budgetDelta = -150, consumptionDelta = -15)
                ),
                blockedByUpgrade = "wash"
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
                ),
                mitigatedByUpgrade = "solar",
                mitigationNote = "Панели дают запас даже в пасмурный день."
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
                ),
                blockedByUpgrade = "auto_light"
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