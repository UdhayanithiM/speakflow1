package com.freelance.speakflow.ui.screens.vocab

import androidx.compose.runtime.Composable
import com.freelance.speakflow.data.VocabAnswerResult

@Composable
fun VocabResultScreen(
    results: List<VocabAnswerResult>,
    onBackHome: () -> Unit
) {
    val total = results.size
    val correct = results.count { it.isCorrect }
    val xp = correct // 1 XP per correct answer

    VocabResultLayout(
        total = total,
        correct = correct,
        xp = xp,
        results = results,
        onReview = { /* Already on review screen */ },
        onBackHome = onBackHome
    )
}
