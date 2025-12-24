package com.freelance.speakflow.ui.screens.speaking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.freelance.speakflow.data.RetrofitInstance
import com.freelance.speakflow.data.SpeakingAnalysisData
import com.freelance.speakflow.data.SpeakingProgressRequest
import kotlinx.coroutines.launch

@Composable
fun SpeakingAnalysisScreen(
    userId: Int,
    lessonId: Int,
    dialogueId: Int,
    result: SpeakingAnalysisData,
    onBackToLessons: () -> Unit
) {
    val scope = rememberCoroutineScope()

    SpeakingAnalysisLayout(
        overallScore = result.overall_score,
        fluency = result.metrics.fluency,
        clarity = result.metrics.clarity,
        accent = result.metrics.accent,
        transcript = result.debug_transcript,
        tips = result.feedback.tips,
        wordFeedback = result.word_analysis,
        onBack = {
            scope.launch {
                try {
                    RetrofitInstance.api.saveSpeakingProgress(
                        SpeakingProgressRequest(
                            userId = userId,
                            lessonId = lessonId,
                            dialogueId = dialogueId,
                            score = result.overall_score
                        )
                    )
                } catch (e: Exception) {
                    // Intentionally silent: user should not be blocked from leaving
                    e.printStackTrace()
                } finally {
                    onBackToLessons()
                }
            }
        }
    )
}
