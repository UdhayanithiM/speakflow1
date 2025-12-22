package com.freelance.speakflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.freelance.speakflow.ui.screens.HomeScreen
import com.freelance.speakflow.ui.screens.LoginScreen
import com.freelance.speakflow.ui.screens.RegisterScreen
import com.freelance.speakflow.ui.screens.vocab.VocabGameScreen
import com.freelance.speakflow.ui.screens.vocab.VocabPreviewScreen
import com.freelance.speakflow.ui.screens.vocab.VocabTopicSelectionScreen
import com.freelance.speakflow.ui.theme.SpeakFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeakFlowTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * ----------------------------------------------------
 * SIMPLE STATE-BASED NAVIGATION (NO NAV COMPONENT)
 * ----------------------------------------------------
 */
@Composable
fun AppNavigation() {

    var currentScreen by remember { mutableStateOf("login") }
    var currentUserId by remember { mutableIntStateOf(0) }

    // Shared vocab state
    var selectedCategory by remember { mutableStateOf("") }

    when (currentScreen) {

        // ---------------- LOGIN ----------------
        "login" -> LoginScreen(
            onLoginSuccess = { userId ->
                currentUserId = userId
                currentScreen = "home"
            },
            onNavigateToRegister = {
                currentScreen = "register"
            }
        )

        // ---------------- REGISTER ----------------
        "register" -> RegisterScreen(
            onRegisterSuccess = {
                currentScreen = "login"
            },
            onNavigateToLogin = {
                currentScreen = "login"
            }
        )

        // ---------------- HOME ----------------
        "home" -> HomeScreen(
            userId = currentUserId,
            onNavigateToGame = { moduleId ->
                when (moduleId) {
                    "vocab" -> currentScreen = "vocab_topics"
                    else -> println("Unknown module: $moduleId")
                }
            }
        )

        // ---------------- VOCAB TOPICS ----------------
        "vocab_topics" -> VocabTopicSelectionScreen(
            onTopicChosen = { category ->
                selectedCategory = category
                currentScreen = "vocab_preview"
            },
            onBack = {
                currentScreen = "home"
            }
        )

        // ---------------- VOCAB PREVIEW ----------------
        "vocab_preview" -> VocabPreviewScreen(
            category = selectedCategory,
            onStartGame = {
                currentScreen = "vocab_game"
            },
            onBack = {
                currentScreen = "vocab_topics"
            }
        )

        // ---------------- VOCAB GAME ----------------
        "vocab_game" -> VocabGameScreen(
            category = selectedCategory,
            onGameComplete = { score ->
                println("Game Over! Score: $score")
                currentScreen = "home"
            }
        )
    }
}
