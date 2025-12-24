package com.freelance.speakflow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.freelance.speakflow.data.*
import com.freelance.speakflow.ui.screens.*
import com.freelance.speakflow.ui.screens.vocab.*
import com.freelance.speakflow.ui.screens.speaking.*
import com.freelance.speakflow.ui.screens.grammar.*
import com.freelance.speakflow.ui.screens.situations.*
import com.freelance.speakflow.ui.screens.gamification.*
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

@Composable
fun AppNavigation() {
    val context = LocalContext.current

    // ================= GLOBAL STATE =================
    var currentScreen by remember { mutableStateOf("login") }
    var currentUserId by remember { mutableIntStateOf(0) }

    // ================= VOCAB STATE =================
    var selectedCategory by remember { mutableStateOf("") }
    var vocabResults by remember { mutableStateOf<List<VocabAnswerResult>>(emptyList()) }

    // ================= SPEAKING STATE =================
    var selectedSpeakingLesson by remember { mutableStateOf<SpeakingLesson?>(null) }
    var currentLessonId by remember { mutableIntStateOf(0) }
    var currentDialogueIndex by remember { mutableIntStateOf(0) }
    var analysisResult by remember { mutableStateOf<SpeakingAnalysisData?>(null) }

    // ================= GRAMMAR STATE =================
    var grammarLevelData by remember { mutableStateOf<GrammarLevelData?>(null) }
    var grammarScore by remember { mutableIntStateOf(0) }

    // ================= SITUATIONS STATE =================
    var selectedSituationId by remember { mutableStateOf("") }

    when (currentScreen) {

        // ================= AUTH FLOW =================
        "login" -> LoginScreen(
            onLoginSuccess = { userId ->
                currentUserId = userId
                currentScreen = "home"
            },
            onNavigateToRegister = { currentScreen = "register" }
        )

        "register" -> RegisterScreen(
            onRegisterSuccess = { currentScreen = "login" },
            onNavigateToLogin = { currentScreen = "login" }
        )

        // ================= HOME DASHBOARD (MAIN HUB) =================
        "home" -> MainHubScreen(
            userId = currentUserId,
            onNavigateToModule = { moduleId ->
                when (moduleId) {
                    // --- TAB 1: LEARN MODULES ---
                    "vocab" -> currentScreen = "vocab_topics"
                    "speaking" -> currentScreen = "speaking_lessons"
                    "grammar" -> currentScreen = "grammar_intro"
                    "situations" -> currentScreen = "situations"

                    // --- TAB 2: GAME MODULES ---
                    "voice_match" -> currentScreen = "voice_match"
                    "echo_game" -> currentScreen = "echo_game"

                    // ✅ FIXED: ADDED THIS MISSING LINE
                    "speed_race" -> currentScreen = "speed_race"

                    else -> Toast.makeText(
                        context,
                        "Coming Soon!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        // ================= VOCAB FLOW =================
        "vocab_topics" -> VocabTopicSelectionScreen(
            onTopicChosen = {
                selectedCategory = it
                currentScreen = "vocab_preview"
            },
            onBack = { currentScreen = "home" }
        )

        "vocab_preview" -> VocabPreviewScreen(
            category = selectedCategory,
            onStartGame = { currentScreen = "vocab_game" },
            onBack = { currentScreen = "vocab_topics" }
        )

        "vocab_game" -> VocabGameScreen(
            category = selectedCategory,
            onGameComplete = {
                vocabResults = it
                currentScreen = "vocab_result"
            }
        )

        "vocab_result" -> VocabResultScreen(
            results = vocabResults,
            onBackHome = {
                vocabResults = emptyList()
                currentScreen = "home"
            }
        )

        // ================= SPEAKING FLOW =================
        "speaking_lessons" -> SpeakingLessonsScreen(
            userId = currentUserId,
            onLessonClick = { lesson ->
                selectedSpeakingLesson = lesson
                currentLessonId = lesson.id
                currentDialogueIndex = 0
                analysisResult = null
                currentScreen = "speaking_dialogue"
            },
            onBack = { currentScreen = "home" }
        )

        "speaking_dialogue" -> {
            val totalDialogues = selectedSpeakingLesson?.totalDialogues ?: 0
            val dialogueId = (currentLessonId * 100) + (currentDialogueIndex + 1)

            if (analysisResult != null) {
                // 🔵 ANALYSIS
                SpeakingAnalysisScreen(
                    userId = currentUserId,
                    lessonId = currentLessonId,
                    dialogueId = dialogueId,
                    result = analysisResult!!,
                    onBackToLessons = {
                        analysisResult = null
                        currentDialogueIndex++

                        if (currentDialogueIndex >= totalDialogues) {
                            currentDialogueIndex = 0
                            currentScreen = "speaking_lessons"
                        } else {
                            currentScreen = "speaking_dialogue"
                        }
                    }
                )
            } else {
                // 🟢 DIALOGUE
                SpeakingDialogueScreen(
                    lessonId = currentLessonId,
                    dialogueIndex = currentDialogueIndex,
                    onFinishDialogue = { result ->
                        analysisResult = result
                    }
                )
            }
        }

        // ================= GRAMMAR FLOW =================
        "grammar_intro" -> GrammarIntroScreen(
            levelId = 1,
            onStartGame = { data ->
                grammarLevelData = data
                currentScreen = "grammar_game"
            },
            onBack = { currentScreen = "home" }
        )

        "grammar_game" -> {
            grammarLevelData?.let { data ->
                GrammarGameScreen(
                    levelData = data,
                    onLevelComplete = { score ->
                        grammarScore = score
                        currentScreen = "grammar_result"
                    }
                )
            }
        }

        "grammar_result" -> GrammarResultScreen(
            score = grammarScore,
            onHome = {
                grammarLevelData = null
                grammarScore = 0
                currentScreen = "home"
            }
        )

        // ================= SITUATIONS FLOW =================
        "situations" -> SituationsListScreen(
            onBack = { currentScreen = "home" },
            onSituationClick = { id ->
                selectedSituationId = id
                currentScreen = "situation_game"
            }
        )

        "situation_game" -> SituationGameScreen(
            situationId = selectedSituationId,
            onBack = { currentScreen = "situations" }
        )

        // ================= GAMIFICATION (Games) =================
        "voice_match" -> VoiceMatchGameScreen(
            onBack = { currentScreen = "home" }
        )

        "echo_game" -> EchoGameScreen(
            onBack = { currentScreen = "home" }
        )

        "speed_race" -> SpeedRaceGameScreen(
            onBack = { currentScreen = "home" }
        )
    }
}