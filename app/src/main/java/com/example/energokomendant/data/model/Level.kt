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