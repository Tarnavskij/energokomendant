package com.example.energokomendant.data.model

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val cost: Int,
    val consumptionReduction: Int, // отрицательное число = экономия
    val comfortChange: Int
)