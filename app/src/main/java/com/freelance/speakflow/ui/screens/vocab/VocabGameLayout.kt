package com.freelance.speakflow.ui.screens.vocab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freelance.speakflow.data.QuizQuestion
import com.freelance.speakflow.data.VocabOption
import com.freelance.speakflow.ui.theme.PurplePrimary

@Composable
fun VocabGameLayout(
    currentQuestion: QuizQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    onPlayAudio: () -> Unit,
    onOptionSelected: (String) -> Unit   // OPTION ID
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LinearProgressIndicator(
            progress = { (questionIndex + 1) / totalQuestions.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = PurplePrimary,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Question ${questionIndex + 1} / $totalQuestions",
            color = Color.Gray
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Listen and click the correct image",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onPlayAudio,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            modifier = Modifier.size(80.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                Icons.Default.VolumeUp,
                contentDescription = "Play Audio",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(currentQuestion.options) { option ->
                VocabOptionCard(
                    option = option,
                    onClick = { onOptionSelected(option.id) }
                )
            }
        }
    }
}

@Composable
fun VocabOptionCard(
    option: VocabOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = option.image,
                fontSize = 48.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VocabGamePreview() {
    val previewQuestion = QuizQuestion(
        questionId = "q1",            // ✅ CORRECT
        targetWord = "Lion",
        targetAudio = "lion.mp3",
        correctOptionId = "1",
        options = listOf(
            VocabOption("1", "🦁"),
            VocabOption("2", "🐱"),
            VocabOption("3", "🐶"),
            VocabOption("4", "🐟")
        )
    )

    VocabGameLayout(
        currentQuestion = previewQuestion,
        questionIndex = 0,
        totalQuestions = 5,
        onPlayAudio = {},
        onOptionSelected = {}
    )
}
