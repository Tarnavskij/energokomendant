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