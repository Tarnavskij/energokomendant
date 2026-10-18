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