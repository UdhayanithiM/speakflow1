package com.freelance.speakflow.ui.screens.vocab

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.freelance.speakflow.data.RetrofitInstance
import com.freelance.speakflow.data.VocabAnswerResult
import com.freelance.speakflow.data.VocabListenClickResponse
import java.util.Locale

@Composable
fun VocabGameScreen(
    category: String,
    onGameComplete: (List<VocabAnswerResult>) -> Unit
) {
    val context = LocalContext.current

    var response by remember { mutableStateOf<VocabListenClickResponse?>(null) }
    var index by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    val results = remember { mutableStateListOf<VocabAnswerResult>() }

    // -------- TTS --------
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var ttsReady by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                ttsReady = true
            }
        }
        onDispose {
            tts?.shutdown()
        }
    }

    fun speak(word: String) {
        if (ttsReady) {
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // -------- API --------
    LaunchedEffect(category) {
        response = RetrofitInstance.api.getVocabListenClick(category, 1)
        isLoading = false
    }

    if (isLoading || response == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // 🔽 LIMIT QUESTIONS HERE (REDUCE ROUNDS)
    val questions = response!!.payload.questions.take(5)
    val question = questions[index]

    LaunchedEffect(index) {
        speak(question.targetWord)
    }

    VocabGameLayout(
        currentQuestion = question,
        questionIndex = index,
        totalQuestions = questions.size,
        onPlayAudio = { speak(question.targetWord) },
        onOptionSelected = { selectedId ->

            results.add(
                VocabAnswerResult(
                    word = question.targetWord,
                    image = question.options.first { it.id == selectedId }.image,
                    isCorrect = selectedId == question.correctOptionId
                )
            )

            if (index < questions.lastIndex) {
                index++
            } else {
                onGameComplete(results.toList())
            }
        }
    )
}
