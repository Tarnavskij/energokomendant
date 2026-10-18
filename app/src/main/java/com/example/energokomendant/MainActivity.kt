package com.example.energokomendant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.energokomendant.data.MetaRepository
import com.example.energokomendant.data.repository.LevelRepository
import com.example.energokomendant.ui.game.GameScreen
import com.example.energokomendant.ui.hr.HRScreen
import com.example.energokomendant.ui.menu.CityMapScreen
import com.example.energokomendant.ui.substation.SubstationScreen
import com.example.energokomendant.ui.suppliers.SupplierScreen
import com.example.energokomendant.viewmodel.GameViewModel
import com.example.energokomendant.viewmodel.SubstationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MetaRepository.init(this)
        setContent {
            MaterialTheme {
                AppNavigation(onExit = { finish() })
            }
        }
    }
}

sealed class Screen {
    object Menu : Screen()
    data class Game(val levelId: Int) : Screen()
    object Substation : Screen()
    object Suppliers : Screen()
    object HR : Screen()
}

@Composable
fun AppNavigation(onExit: () -> Unit) {
    val gameViewModel: GameViewModel = viewModel()
    val substationViewModel: SubstationViewModel = viewModel()

    val gameState by gameViewModel.gameState.collectAsState()
    val subState by substationViewModel.state.collectAsState()
    val metaState by MetaRepository.state.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Menu) }
    var showExitDialog by remember { mutableStateOf(false) }

    when (val screen = currentScreen) {
        is Screen.Menu -> {
            BackHandler { showExitDialog = true }
            CityMapScreen(
                coins = metaState.coins,
                onOpenHouse = {
                    gameViewModel.startLevel(1)
                    currentScreen = Screen.Game(1)
                },
                onOpenSubstation = {
                    substationViewModel.reset()
                    currentScreen = Screen.Substation
                },
                onOpenSchool = { },
                onOpenSuppliers = { currentScreen = Screen.Suppliers },
                onOpenHR = { currentScreen = Screen.HR }
            )

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = { Text("Выйти из игры?") },
                    text = { Text("Прогресс сохраняется, но текущий уровень будет сброшен.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showExitDialog = false
                            onExit()
                        }) { Text("Выйти") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showExitDialog = false }) { Text("Отмена") }
                    }
                )
            }
        }
        is Screen.Game -> {
            val level = LevelRepository.getLevels().find { it.id == screen.levelId }
            if (level != null) {
                GameScreen(
                    level = level,
                    gameState = gameState,
                    onBuyUpgrade = { gameViewModel.buyUpgrade(it) },
                    onResolveEvent = { gameViewModel.resolveEvent(it) },
                    onNextDay = { gameViewModel.nextDay() },
                    onBackToMenu = {
                        gameViewModel.reset()
                        currentScreen = Screen.Menu
                    }
                )
            }
        }
        is Screen.Substation -> {
            SubstationScreen(
                state = subState,
                onTapLine = { substationViewModel.tapLine(it) },
                onStart = { substationViewModel.start() },
                onBack = {
                    substationViewModel.reset()
                    currentScreen = Screen.Menu
                }
            )
        }
        is Screen.Suppliers -> {
            SupplierScreen(
                meta = metaState,
                onBuy = { MetaRepository.buyItem(it.id, it.cost) },
                onBack = { currentScreen = Screen.Menu }
            )
        }
        is Screen.HR -> {
            HRScreen(
                meta = metaState,
                onHire = { MetaRepository.hireEmployee(it.id, it.cost) },
                onBack = { currentScreen = Screen.Menu }
            )
        }
    }
}