package com.freelance.speakflow.ui.screens.vocab

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freelance.speakflow.data.QuizQuestion
import com.freelance.speakflow.ui.theme.PurplePrimary

@Composable
fun VocabListenClickLayout(
    question: QuizQuestion,
    index: Int,
    total: Int,
    onPlayAudio: () -> Unit,
    onOptionClick: (String) -> Unit   // OPTION ID
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Progress
        LinearProgressIndicator(
            progress = { (index + 1) / total.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = PurplePrimary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Question ${index + 1} / $total",
            color = Color.Gray
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Listen and choose the correct image",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // AUDIO BUTTON
        IconButton(
            onClick = onPlayAudio,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(PurplePrimary)
        ) {
            Icon(
                Icons.Default.VolumeUp,
                contentDescription = "Play Audio",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        // IMAGE OPTIONS
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(question.options) { option ->
                Card(
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        .clickable {
                            // ✅ FIXED: PASS OPTION ID, NOT WORD
                            onOptionClick(option.id)
                        },
                    shape = RoundedCornerShape(16.dp)
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
        }
    }
}
