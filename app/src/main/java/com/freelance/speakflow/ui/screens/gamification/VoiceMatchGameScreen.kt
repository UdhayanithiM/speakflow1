package com.freelance.speakflow.ui.screens.gamification

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.freelance.speakflow.data.RetrofitInstance
import com.freelance.speakflow.data.VoiceMatchLevel
import kotlinx.coroutines.delay
import java.util.Locale

/* ---------------------------------------------------------
   SCREEN LOADER
--------------------------------------------------------- */

@Composable
fun VoiceMatchGameScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var levelData by remember { mutableStateOf<VoiceMatchLevel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            levelData = RetrofitInstance.api.getVoiceMatchLevel(1).payload
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load level", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    if (isLoading || levelData == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        VoiceMatchGameLogic(levelData!!, onBack)
    }
}

/* ---------------------------------------------------------
   GAME LOGIC & UI
--------------------------------------------------------- */

@Composable
fun VoiceMatchGameLogic(level: VoiceMatchLevel, onBack: () -> Unit) {
    val context = LocalContext.current

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var isListening by remember { mutableStateOf(false) }

    var userSpokenText by remember { mutableStateOf("") }
    var matchResult by remember { mutableStateOf<Boolean?>(null) }

    // STATES FOR SCREEN FLOW
    var showCorrectScreen by remember { mutableStateOf(false) } // ✅ New State
    var showResult by remember { mutableStateOf(false) }

    val currentQuestion = level.questions[currentIndex]

    /* ---------------------------------------------------------
       SPEECH RECOGNIZER
    --------------------------------------------------------- */

    val speechRecognizer = remember(currentIndex) {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                isListening = true
                userSpokenText = "Listening..."
                matchResult = null
                speechRecognizer.startListening(speechIntent)
            } else {
                Toast.makeText(context, "Microphone permission required", Toast.LENGTH_SHORT).show()
            }
        }

    DisposableEffect(currentIndex) {
        val listener = object : RecognitionListener {
            override fun onEndOfSpeech() { isListening = false }
            override fun onError(error: Int) {
                isListening = false
                userSpokenText = "Try again"
                matchResult = false
            }
            override fun onResults(results: Bundle?) {
                isListening = false
                val spokenRaw = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull() ?: return
                userSpokenText = spokenRaw

                val spokenWords = spokenRaw.lowercase(Locale.US).replace(Regex("[^a-z ]"), "").split(" ")
                val target = currentQuestion.targetWord.lowercase(Locale.US)

                // Check if spoken words contain the target
                matchResult = spokenWords.contains(target)
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
        onDispose { speechRecognizer.destroy() }
    }

    /* ---------------------------------------------------------
       LOGIC: HANDLE SUCCESS
    --------------------------------------------------------- */

    LaunchedEffect(matchResult) {
        if (matchResult == true) {
            score += 10
            delay(500) // Small delay to see the green text
            showCorrectScreen = true // ✅ Pause on Correct Screen
        }
    }

    /* ---------------------------------------------------------
       UI BRANCHING
    --------------------------------------------------------- */

    if (showResult) {
        // --- GAME OVER SCREEN ---
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆", fontSize = 72.sp)
                Text("Level Complete", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("Score: $score", fontSize = 18.sp)
                Spacer(Modifier.height(24.dp))
                Button(onClick = onBack) { Text("Back to Home") }
            }
        }
    }
    else if (showCorrectScreen) {
        // --- CORRECT ANSWER SCREEN (Matches Screenshot) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)) // Off-white bg
                .padding(24.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* No back action during result usually */ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Spacer(Modifier.weight(1f))
                Text("Voice Match: Correct!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Correct!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Great job! You correctly identified the word.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(32.dp))

                // Image Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = currentQuestion.image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Word Info
            Text(currentQuestion.targetWord, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "The word that matches the mascot's expression.",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(8.dp))
            Text(currentQuestion.targetWord, fontSize = 16.sp, color = Color(0xFFBCAAA4))

            Spacer(Modifier.weight(1f))

            // Next Button
            Button(
                onClick = {
                    // ✅ Move to next question logic
                    if (currentIndex < level.questions.lastIndex) {
                        currentIndex++
                        userSpokenText = ""
                        matchResult = null
                        showCorrectScreen = false
                    } else {
                        showResult = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)), // Orange color
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Next Question", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    else {
        // --- LISTENING SCREEN (Original) ---
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(level.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Question ${currentIndex + 1} / ${level.questions.size}")

            Spacer(Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.size(260.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    model = currentQuestion.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.height(24.dp))

            // Hint shown only if wrong
            if (matchResult == false) {
                Text("Hint: Say \"${currentQuestion.targetWord}\"", color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
            }

            Text(
                text = if (userSpokenText.isEmpty()) "Tap mic & speak" else "Heard: \"$userSpokenText\"",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = when (matchResult) {
                    true -> Color(0xFF4CAF50)
                    false -> Color(0xFFF44336)
                    else -> Color.Black
                },
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            val scale by animateFloatAsState(if (isListening) 1.2f else 1f, label = "mic-scale")

            FloatingActionButton(
                onClick = {
                    if (SpeechRecognizer.isRecognitionAvailable(context)) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        Toast.makeText(context, "Speech not supported", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(80.dp).scale(scale),
                shape = CircleShape,
                containerColor = if (isListening) Color.Red else MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Speak",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}