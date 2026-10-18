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
                    Text("Линия ${index + 1}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("−20%", color = Color.White, fontSize = 12.sp)
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Линия ${index + 1}", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                Text("${cooldown.toInt() + 1}с", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
    }
}