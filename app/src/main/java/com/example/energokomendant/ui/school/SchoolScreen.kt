package com.example.energokomendant.ui.school

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.viewmodel.SchoolDevice
import com.example.energokomendant.viewmodel.SchoolState

@Composable
fun SchoolScreen(
    state: SchoolState,
    onToggleDevice: (String) -> Unit,
    onStart: () -> Unit,
    onFinish: () -> Unit,
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
            Text("🏫 Школа", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("${state.timeLeft.toInt()}с", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (state.scenario != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(state.scenario.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(state.scenario.hint, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Выбери 3 прибора, которые нужно выключить:",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.scenario != null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.scenario.devices, key = { it.id }) { device ->
                    DeviceCard(
                        device = device,
                        isSelected = device.id in state.selectedIds,
                        enabled = state.isRunning,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleDevice(device.id)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isFinished) {
            ResultCard(state = state, onStart = onStart, onBack = onBack)
        } else if (state.isRunning) {
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = state.selectedIds.isNotEmpty()
            ) {
                Text("Проверить (${state.selectedIds.size}/3)", fontSize = 15.sp)
            }
        } else {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Начать", fontSize = 18.sp)
            }
        }

        if (!state.isFinished && state.bestResult > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Личный рекорд: ${state.bestResult}/3",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DeviceCard(
    device: SchoolDevice,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(200),
        label = "deviceBg"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(device.emoji, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                device.name,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                maxLines = 2
            )
            if (isSelected) {
                Spacer(modifier = Modifier.height(2.dp))
                Text("✓", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun ResultCard(
    state: SchoolState,
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                state.isPerfect -> MaterialTheme.colorScheme.primaryContainer
                state.isWin -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = when {
                state.isPerfect -> "🏆 Идеально!"
                state.isWin -> "✅ Победа"
                else -> "💀 Провал"
            }
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Правильно: ${state.correctCount}/3" +
                        if (state.wrongCount > 0) ", лишних: ${state.wrongCount}" else "",
                fontSize = 13.sp
            )
            if (state.rewardEarned > 0) {
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
}