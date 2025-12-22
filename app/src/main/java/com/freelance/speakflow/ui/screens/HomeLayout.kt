package com.freelance.speakflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freelance.speakflow.R
import com.freelance.speakflow.data.DashboardResponse
import com.freelance.speakflow.data.ModuleItem
import com.freelance.speakflow.ui.theme.*

@Composable
fun HomeLayout(
    data: DashboardResponse?,
    isLoading: Boolean,
    onModuleClick: (String) -> Unit
) {
    if (isLoading || data == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PurplePrimary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(24.dp))

        // ================= DAILY PROGRESS CARD =================
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F8FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Daily\nProgress",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFFFF3BF)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFFF9F43)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "${data.dayStreak} Day\nStreak",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { data.levelProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = PurplePrimary,
                    trackColor = Color.LightGray.copy(alpha = 0.4f)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${data.totalXp} / 1000 XP to Level ${data.currentLevel}",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                    Text(
                        "${(data.levelProgress * 100).toInt()} %",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // ================= MODULES =================
        Text(
            text = "Learn & Practice",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            data.modules.forEach { module ->
                ModuleCard(module, onModuleClick)
            }
        }
    }
}

@Composable
fun ModuleCard(
    module: ModuleItem,
    onClick: (String) -> Unit
) {
    val imageRes = when (module.id) {
        "vocab" -> R.drawable.vocubalary
        "speaking" -> R.drawable.speaking
        "grammar" -> R.drawable.grammar
        "situations" -> R.drawable.situations
        else -> R.drawable.vocubalary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick(module.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = module.title,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = module.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = module.subtitle,
                    fontSize = 13.sp,
                    color = TextGray
                )
            }
        }
    }
}

// ================= PREVIEW =================
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val dummyData = DashboardResponse(
        userName = "Alex",
        dayStreak = 15,
        totalXp = 650,
        currentLevel = 6,
        levelProgress = 0.65f,
        modules = listOf(
            ModuleItem("vocab", "Vocabulary", "Learn new words", "book", false),
            ModuleItem("speaking", "Speaking", "Practice pronunciation", "mic", false),
            ModuleItem("grammar", "Grammar", "Master the rules", "edit", false),
            ModuleItem("situations", "Situations", "Real-life scenarios", "chat", false)
        )
    )

    SpeakFlowTheme {
        HomeLayout(dummyData, false, {})
    }
}
