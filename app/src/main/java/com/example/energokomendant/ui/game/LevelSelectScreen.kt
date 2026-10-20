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