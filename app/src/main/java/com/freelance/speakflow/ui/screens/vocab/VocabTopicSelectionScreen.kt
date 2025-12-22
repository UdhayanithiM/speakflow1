package com.freelance.speakflow.ui.screens.vocab

import androidx.compose.runtime.Composable
import com.freelance.speakflow.R

@Composable
fun VocabTopicSelectionScreen(
    onTopicChosen: (String) -> Unit,
    onBack: () -> Unit
) {
    val topics = listOf(
        VocabTopic("animals", "Animals", R.drawable.animals),
        VocabTopic("food", "Food & Drinks", R.drawable.food),
        VocabTopic("colors", "Colors", R.drawable.colors),
        VocabTopic("body", "Body Parts", R.drawable.body_parts),
        VocabTopic("family", "Family", R.drawable.family),
        VocabTopic("weather", "Weather", R.drawable.weather),
        VocabTopic("clothing", "Clothing", R.drawable.clothing),
        VocabTopic("transport", "Transportation", R.drawable.transport)
    )

    VocabTopicSelectionLayout(
        topics = topics,
        onTopicSelected = onTopicChosen,
        onBack = onBack
    )
}
