package com.example.energokomendant.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.scale
import androidx.compose.animation.core.spring

@Composable
fun GameScreen(
    level: Level,
    gameState: GameState,
    coins: Int,
    onBuyUpgrade: (Upgrade) -> Unit,
    onResolveEvent: (EventChoice) -> Unit,
    onNextDay: () -> Unit,
    onBackToMenu: () -> Unit,
    onNextLevel: (() -> Unit)? = null,
    onCampaignComplete: (() -> Unit)? = null
) {
    BackHandler { onBackToMenu() }
    val haptic = LocalHapticFeedback.current
    var pendingUpgrade by remember { mutableStateOf<Upgrade?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(level.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFFFFF59D),
                    shape = CircleShape
                ) {
                    Text(
                        "⚡$coins",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("Д${gameState.currentDay}/${gameState.maxDays}", fontSize = 13.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        DayDots(current = gameState.currentDay, total = gameState.maxDays)
        Spacer(modifier = Modifier.height(4.dp))

        StatusBadge(level = level, state = gameState)

        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(90.dp),
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
                        pendingUpgrade = upgrade
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (gameState.isLevelCompleted || gameState.isGameOver) {
            ResultCard(
                gameState = gameState,
                onBackToMenu = onBackToMenu,
                onNextLevel = if (gameState.isLevelCompleted) onNextLevel else null,
                onCampaignComplete = if (gameState.isLevelCompleted && onNextLevel == null) onCampaignComplete else null
            )
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

    pendingUpgrade?.let { upgrade ->
        val newBudget = gameState.budget - upgrade.cost
        val newConsumption = (gameState.consumption + upgrade.consumptionReduction).coerceAtLeast(1)
        val newComfort = (gameState.comfort + upgrade.comfortChange).coerceIn(0, 100)
        val incomeDelta = (newComfort - gameState.comfort) * 10
        val billDelta = (newConsumption - gameState.consumption) * level.tariff
        val newForecastNet = gameState.forecastNet + incomeDelta - billDelta

        AlertDialog(
            onDismissRequest = { pendingUpgrade = null },
            title = { Text(upgrade.name, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(upgrade.description, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    UpgradeDiffRow("Бюджет", "${gameState.budget}₽", "${newBudget}₽", newBudget < gameState.budget)
                    UpgradeDiffRow("Расход", "${gameState.consumption} кВт", "${newConsumption} кВт", newConsumption < gameState.consumption)
                    UpgradeDiffRow("Комфорт", "${gameState.comfort}%", "${newComfort}%", newComfort > gameState.comfort)
                    UpgradeDiffRow("Прогноз/день", "${gameState.forecastNet}₽", "${newForecastNet}₽", newForecastNet > gameState.forecastNet)

                    if (!upgrade.fact.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Text("💡 ", fontSize = 14.sp)
                                Text(
                                    upgrade.fact ?: "",
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onBuyUpgrade(upgrade)
                    pendingUpgrade = null
                }) { Text("Купить") }
            },
            dismissButton = {
                TextButton(onClick = { pendingUpgrade = null }) { Text("Отмена") }
            }
        )
    }
}

@Composable
private fun UpgradeDiffRow(label: String, before: String, after: String, isImprovement: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp)
        Row {
            Text(before, fontSize = 12.sp)
            Text(" → ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                after,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isImprovement) Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }
    }
}

@Composable
fun StatusBadge(level: Level, state: GameState) {
    val daysLeft = (state.maxDays - state.currentDay + 1).coerceAtLeast(1)

    val (badgeColor, message) = when {
        state.comfort < level.minComfort ->
            MaterialTheme.colorScheme.error to "🔴 Комфорт ниже минимума (${level.minComfort}%)"

        state.forecastNet < 0 && state.budget + state.forecastNet * daysLeft < 0 -> {
            val daysToBroke = (state.budget / -state.forecastNet).coerceAtLeast(1)
            MaterialTheme.colorScheme.error to "🔴 Разорение через ~$daysToBroke дн."
        }

        state.consumption > level.targetConsumption ->
            Color(0xFFF57C00) to "⚠️ Расход на ${state.consumption - level.targetConsumption} кВт выше цели"

        else ->
            Color(0xFF2E7D32) to "✅ Всё в норме — доведи до конца"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = badgeColor.copy(alpha = 0.15f))
    ) {
        Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(message, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = badgeColor)
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
fun ResultCard(
    gameState: GameState,
    onBackToMenu: () -> Unit,
    onNextLevel: (() -> Unit)? = null,
    onCampaignComplete: (() -> Unit)? = null
) {
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
                Text(
                    if (onCampaignComplete != null) "🎉 Кампания пройдена!" else "🏆 Победа!",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
                if (gameState.rewardEarned > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "+${gameState.rewardEarned} ⚡",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57F17)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedStars(stars = gameState.starsEarned)
                Spacer(modifier = Modifier.height(8.dp))

                when {
                    onCampaignComplete != null -> {
                        Text(
                            "Ты сдал здание комиссии с лучшим результатом. Дальше — Школа и другие объекты.",
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onCampaignComplete, modifier = Modifier.fillMaxWidth()) {
                            Text("В город")
                        }
                    }
                    onNextLevel != null -> {
                        Button(onClick = onNextLevel, modifier = Modifier.fillMaxWidth()) {
                            Text("Следующий уровень →")
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth()) {
                            Text("К списку уровней")
                        }
                    }
                    else -> {
                        Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth()) {
                            Text("К списку уровней")
                        }
                    }
                }
            } else {
                Text("💀 Провал", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(gameState.lastActionMessage ?: "Цель не достигнута.", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth()) {
                    Text("К списку уровней")
                }
            }
        }
    }
}

@Composable
private fun AnimatedStars(stars: Int) {
    var visibleCount by remember { mutableStateOf(0) }
    LaunchedEffect(stars) {
        visibleCount = 0
        for (i in 1..stars) {
            kotlinx.coroutines.delay(300)
            visibleCount = i
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { i ->
            val isStarred = i < stars
            val isVisible = i < visibleCount
            val scale by animateFloatAsState(
                targetValue = if (isVisible) 1f else if (isStarred) 0.3f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "starScale"
            )
            Text(
                text = if (isStarred) "★" else "☆",
                fontSize = 32.sp,
                color = if (isStarred) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.scale(scale)
            )
        }
    }
}