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