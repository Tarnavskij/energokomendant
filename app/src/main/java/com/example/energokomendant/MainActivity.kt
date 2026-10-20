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
import com.example.energokomendant.ui.game.LevelSelectScreen
import com.example.energokomendant.ui.hr.HRScreen
import com.example.energokomendant.ui.menu.CityMapScreen
import com.example.energokomendant.ui.onboarding.OnboardingScreen
import com.example.energokomendant.ui.substation.SubstationScreen
import com.example.energokomendant.ui.suppliers.SupplierScreen
import com.example.energokomendant.viewmodel.GameViewModel
import com.example.energokomendant.viewmodel.SubstationViewModel
import com.example.energokomendant.ui.school.SchoolScreen
import com.example.energokomendant.viewmodel.SchoolViewModel

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
    object Onboarding : Screen()
    object Menu : Screen()
    object LevelSelect : Screen()
    data class Game(val levelId: Int) : Screen()
    object Substation : Screen()
    object Suppliers : Screen()
    object HR : Screen()
    object School : Screen()
}

@Composable
fun AppNavigation(onExit: () -> Unit) {
    val gameViewModel: GameViewModel = viewModel()
    val substationViewModel: SubstationViewModel = viewModel()

    val schoolViewModel: SchoolViewModel = viewModel()
    val schoolState by schoolViewModel.state.collectAsState()

    val gameState by gameViewModel.gameState.collectAsState()
    val subState by substationViewModel.state.collectAsState()
    val metaState by MetaRepository.state.collectAsState()

    // Стартовый экран: онбординг, если ещё не видел
    var currentScreen by remember {
        mutableStateOf<Screen>(
            if (MetaRepository.state.value.hasSeenOnboarding) Screen.Menu
            else Screen.Onboarding
        )
    }
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameState.isLevelCompleted) {
        val levelId = gameState.currentLevelId
        if (gameState.isLevelCompleted && levelId != null) {
            MetaRepository.saveLevelCompleted(levelId, gameState.starsEarned)
        }
    }

    when (val screen = currentScreen) {
        is Screen.Onboarding -> {
            OnboardingScreen(
                onDone = {
                    MetaRepository.markOnboardingSeen()
                    currentScreen = Screen.Menu
                }
            )
        }
        is Screen.Menu -> {
            BackHandler { showExitDialog = true }
            CityMapScreen(
                coins = metaState.coins,
                schoolBest = metaState.schoolBest,
                onOpenHouse = { currentScreen = Screen.LevelSelect },
                onOpenSubstation = {
                    substationViewModel.reset()
                    currentScreen = Screen.Substation
                },
                onOpenSchool = {
                    schoolViewModel.reset()
                    currentScreen = Screen.School
                },
                onOpenSuppliers = { currentScreen = Screen.Suppliers },
                onOpenHR = { currentScreen = Screen.HR }
            )

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = { Text("Выйти из игры?") },
                    text = { Text("Прогресс сохраняется.") },
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
        is Screen.LevelSelect -> {
            LevelSelectScreen(
                completedLevels = metaState.completedLevels,
                onPlayLevel = { levelId ->
                    gameViewModel.startLevel(levelId)
                    currentScreen = Screen.Game(levelId)
                },
                onBack = { currentScreen = Screen.Menu }
            )
        }
        is Screen.Game -> {
            val allLevels = LevelRepository.getLevels()
            val level = allLevels.find { it.id == screen.levelId }
            val nextLevelId = screen.levelId + 1
            val hasNextLevel = allLevels.any { it.id == nextLevelId }
            if (level != null) {
                GameScreen(
                    level = level,
                    gameState = gameState,
                    coins = metaState.coins,
                    onBuyUpgrade = { gameViewModel.buyUpgrade(it) },
                    onResolveEvent = { gameViewModel.resolveEvent(it) },
                    onNextDay = { gameViewModel.nextDay() },
                    onBackToMenu = {
                        gameViewModel.reset()
                        currentScreen = Screen.LevelSelect
                    },
                    onNextLevel = if (hasNextLevel) {
                        {
                            gameViewModel.startLevel(nextLevelId)
                            currentScreen = Screen.Game(nextLevelId)
                        }
                    } else null,
                    onCampaignComplete = {
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

        is Screen.School -> {
            SchoolScreen(
                state = schoolState,
                onToggleDevice = { schoolViewModel.toggleDevice(it) },
                onStart = { schoolViewModel.start() },
                onFinish = { schoolViewModel.finish() },
                onBack = {
                    schoolViewModel.reset()
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