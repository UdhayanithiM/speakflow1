package com.freelance.speakflow.ui.screens.vocab

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.freelance.speakflow.R

@Composable
fun VocabPreviewScreen(
    category: String,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // TEMP static data (later backend-driven)
    val previewItems = listOf(
        VocabPreviewItem("Lion", R.drawable.lion),
        VocabPreviewItem("Tiger", R.drawable.tiger),
        VocabPreviewItem("Elephant", R.drawable.elephant),
        VocabPreviewItem("Monkey", R.drawable.monkey),
        VocabPreviewItem("Dog", R.drawable.dog),
        VocabPreviewItem("Cat", R.drawable.cat),
        VocabPreviewItem("Cow", R.drawable.cow),
        VocabPreviewItem("Sheep", R.drawable.sheep),
        VocabPreviewItem("Horse", R.drawable.horse),
        VocabPreviewItem("Goat", R.drawable.goat)
    )

    VocabPreviewLayout(
        items = previewItems,
        onListenClick = { word ->
            // Placeholder – replace with TTS later
            Toast.makeText(
                context,
                "Listening: $word",
                Toast.LENGTH_SHORT
            ).show()
        },
        onStartGame = onStartGame,
        onBack = onBack
    )
}
