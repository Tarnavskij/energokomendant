package com.example.energokomendant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainMenuScreen(
                    onStartGame = { /* позже: переход на экран игры */ }
                )
            }
        }
    }
}

@Composable
fun MainMenuScreen(onStartGame: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ЭнергоКомендант",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Управляй зданием. Экономь энергию. Сохраняй комфорт.",
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onStartGame,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Начать игру", fontSize = 18.sp)
        }
    }
}