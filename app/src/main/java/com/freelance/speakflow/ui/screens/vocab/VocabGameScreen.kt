package com.freelance.speakflow.ui.screens.vocab

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.freelance.speakflow.data.RetrofitInstance
import com.freelance.speakflow.data.VocabListenClickResponse
import com.freelance.speakflow.ui.theme.PurplePrimary

@Composable
fun VocabGameScreen(
    category: String,
    onGameComplete: (Int) -> Unit
) {
    val context = LocalContext.current

    // ---------------- STATE ----------------
    var response by remember { mutableStateOf<VocabListenClickResponse?>(null) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // ---------------- API CALL ----------------
    LaunchedEffect(category) {
        try {
            response = RetrofitInstance.api.getVocabListenClick(category)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Failed to load game",
                Toast.LENGTH_SHORT
            ).show()
        } finally {
            isLoading = false
        }
    }

    // ---------------- LOADING ----------------
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PurplePrimary)
        }
        return
    }

    // ---------------- SAFETY ----------------
    val questions = response?.payload?.questions
    if (questions.isNullOrEmpty()) {
        Toast.makeText(context, "No questions available", Toast.LENGTH_SHORT).show()
        onGameComplete(score)
        return
    }

    val currentQuestion = questions[currentQuestionIndex]

    // ---------------- GAME UI ----------------
    VocabGameLayout(
        currentQuestion = currentQuestion,
        questionIndex = currentQuestionIndex,
        totalQuestions = questions.size,

        onPlayAudio = {
            Toast.makeText(
                context,
                "Playing audio for: ${currentQuestion.targetWord}",
                Toast.LENGTH_SHORT
            ).show()
        },

        onOptionSelected = { selectedOptionId ->

            // backend never sends word in options — only image + id
            val isCorrect = currentQuestion.options
                .first { it.id == selectedOptionId }
                .image == currentQuestion.targetWord

            if (isCorrect) {
                score += 1
                Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Wrong! Answer: ${currentQuestion.targetWord}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (currentQuestionIndex < questions.lastIndex) {
                currentQuestionIndex++
            } else {
                onGameComplete(score)
            }
        }
    )
}
