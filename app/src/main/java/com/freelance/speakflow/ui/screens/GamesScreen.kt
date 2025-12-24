package com.freelance.speakflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GamesScreen(
    onGameClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        // --- Header ---
        Text(
            text = "Beginner Stage",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(8.dp))

        // --- Level Progress ---
        Text("Level 1", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = 0.2f, // Example: 200/1000 XP
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFFFFA726),
            trackColor = Color(0xFFE0E0E0)
        )
        Text("200/1000 XP", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))

        Spacer(Modifier.height(32.dp))

        // --- Game List ---

        // 1. Echo Game
        // 1. Echo Game (NOW ACTIVE)
        GameItemCard(
            title = "Echo Game",
            status = "Play Now", // Changed from "Coming Soon"
            iconBg = Color(0xFFFFF3E0),
            iconColor = Color(0xFFFFA726),
            isLocked = false, // Unlock
            onClick = { onGameClick("echo_game") }
        )

        Spacer(Modifier.height(16.dp))

        // 2. Speed Speak
        GameItemCard(
            title = "Speed Speak",
            status = "Play Now",
            iconBg = Color(0xFFFFEBEE),
            iconColor = Color(0xFFEF5350), // Red
            isLocked = false, // Unlock
            onClick = { onGameClick("speed_race") }
        )

        Spacer(Modifier.height(16.dp))

        // 3. Voice Match (ACTIVE)
        GameItemCard(
            title = "Voice Match",
            status = "Play Now",
            iconBg = Color(0xFFE8F5E9),
            iconColor = Color(0xFF66BB6A), // Green
            isLocked = false, // ✅ Active
            onClick = { onGameClick("voice_match") }
        )
    }
}

@Composable
fun GameItemCard(
    title: String,
    status: String,
    iconBg: Color,
    iconColor: Color,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked) { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = iconColor
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    status,
                    fontSize = 12.sp,
                    color = if (isLocked) Color.Gray else iconColor
                )
            }
        }
    }
}