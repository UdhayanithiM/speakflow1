package com.freelance.speakflow.ui.screens.vocab

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.freelance.speakflow.data.ListenClickPayload
import com.freelance.speakflow.data.RetrofitInstance
import com.freelance.speakflow.data.VocabAnswerResult
import com.freelance.speakflow.data.VocabListenClickResponse

@Composable
fun VocabListenClickScreen(
    category: String,
    onFinish: (List<VocabAnswerResult>) -> Unit
) {
    val context = LocalContext.current

    var response by remember { mutableStateOf<VocabListenClickResponse?>(null) }
    var index by remember { mutableIntStateOf(0) }

    // ✅ STORE PART-3 DATA
    val answerResults = remember { mutableStateListOf<VocabAnswerResult>() }

    // ✅ CORRECT API CALL
    LaunchedEffect(category) {
        try {
            response = RetrofitInstance.api.getVocabListenClick(category)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load vocab game", Toast.LENGTH_SHORT).show()
        }
    }

    if (response == null) return

    val questions = response!!.payload.questions
    val current = questions[index]

    VocabListenClickLayout(
        question = current,
        index = index,
        total = questions.size,
        onPlayAudio = {
            Toast.makeText(context, "Playing audio…", Toast.LENGTH_SHORT).show()
        },
        onOptionClick = { selectedOptionId ->

            val isCorrect = selectedOptionId == current.correctOptionId

            // ✅ STORE RESULT FOR PART-3
            val selectedImage = current.options.first {
                it.id == selectedOptionId
            }.image

            answerResults.add(
                VocabAnswerResult(
                    word = current.targetWord,
                    image = selectedImage,
                    isCorrect = isCorrect
                )
            )

            if (index < questions.lastIndex) {
                index++
            } else {
                onFinish(answerResults.toList())
            }
        }
    )
}
