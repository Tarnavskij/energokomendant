package com.example.energokomendant.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

data class HouseWindowState(
    val color: Color,
    val glow: Float
)

private val COLOR_WASTE = Color(0xFFFFB74D)
private val COLOR_EFFICIENT = Color(0xFFE3F2FD)
private val COLOR_DIM = Color(0xFF546E7A)
private val COLOR_OFF = Color(0xFF1A1A1A)
private val COLOR_SOLAR = Color(0xFFFFF59D)
private val COLOR_INSULATED = Color(0xFF90CAF9)

@Composable
fun HouseView(
    levelId: Int,
    purchasedUpgrades: List<String>,
    modifier: Modifier = Modifier
) {
    val windowStates = remember(levelId, purchasedUpgrades) {
        computeWindowStates(levelId, purchasedUpgrades.toSet())
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val bodyTop = h * 0.32f
            val bodyBottom = h * 0.97f
            val bodyLeft = w * 0.15f
            val bodyRight = w * 0.85f

            // Корпус здания
            drawRect(
                color = Color(0xFF37474F),
                topLeft = Offset(bodyLeft, bodyTop),
                size = Size(bodyRight - bodyLeft, bodyBottom - bodyTop)
            )

            // Крыша
            val roofPath = Path().apply {
                moveTo(bodyLeft - w * 0.03f, bodyTop)
                lineTo(w / 2f, h * 0.05f)
                lineTo(bodyRight + w * 0.03f, bodyTop)
                close()
            }
            drawPath(roofPath, Color(0xFF6D4C41))

            // Труба
            drawRect(
                color = Color(0xFF5D4037),
                topLeft = Offset(w * 0.72f, h * 0.14f),
                size = Size(w * 0.06f, h * 0.14f)
            )
        }

        // Сетка окон 3x3
        Column(
            modifier = Modifier
                .fillMaxWidth(0.56f)
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (col in 0 until 3) {
                        val state = windowStates[row * 3 + col]
                        AnimatedWindow(
                            state = state,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedWindow(
    state: HouseWindowState,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        targetValue = state.color,
        animationSpec = tween(500),
        label = "windowColor"
    )
    val animatedGlow by animateFloatAsState(
        targetValue = state.glow,
        animationSpec = tween(500),
        label = "windowGlow"
    )

    Box(
        modifier = modifier.drawBehind {
            // Свечение
            if (animatedGlow > 0.01f) {
                drawRoundRect(
                    color = animatedColor.copy(alpha = 0.3f * animatedGlow),
                    cornerRadius = CornerRadius(size.minDimension * 0.4f)
                )
            }
            // Рама окна
            val outerInset = size.minDimension * 0.10f
            drawRoundRect(
                color = Color(0xFF1A1A1A),
                topLeft = Offset(outerInset, outerInset),
                size = Size(size.width - 2 * outerInset, size.height - 2 * outerInset),
                cornerRadius = CornerRadius(3f)
            )
            // Стекло
            val innerInset = size.minDimension * 0.18f
            drawRoundRect(
                color = animatedColor,
                topLeft = Offset(innerInset, innerInset),
                size = Size(size.width - 2 * innerInset, size.height - 2 * innerInset),
                cornerRadius = CornerRadius(2f)
            )
        }
    )
}

private fun computeWindowStates(levelId: Int, purchased: Set<String>): List<HouseWindowState> {
    val states = MutableList(9) {
        HouseWindowState(color = COLOR_WASTE, glow = 1f)
    }

    when (levelId) {
        1 -> {
            if ("led" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_EFFICIENT)
            }
            if ("sensors" in purchased) {
                for (i in listOf(0, 2, 6, 8)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.4f)
            }
            if ("off_light" in purchased) {
                for (i in listOf(6, 7, 8)) states[i] = states[i].copy(color = COLOR_OFF, glow = 0f)
            }
        }
        2 -> {
            if ("windows" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_INSULATED)
            }
            if ("temp_down" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_INSULATED, glow = 0.5f)
            }
        }
        3 -> {
            if ("fridge" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_EFFICIENT)
            }
            if ("wash" in purchased) {
                for (i in listOf(6, 7, 8)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.4f)
            }
            if ("standby" in purchased) {
                for (i in listOf(0, 2)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.3f)
            }
        }
        4 -> {
            if ("solar" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_SOLAR, glow = 0.8f)
            }
        }
        5 -> {
            if ("smart_plugs" in purchased) {
                for (i in states.indices) states[i] = states[i].copy(color = COLOR_EFFICIENT)
            }
            if ("temp_sensors" in purchased) {
                for (i in listOf(0, 2, 6, 8)) states[i] = states[i].copy(color = COLOR_DIM, glow = 0.3f)
            }
        }
    }

    return states
}