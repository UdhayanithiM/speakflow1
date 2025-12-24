package com.freelance.speakflow.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box // ✅ Added
import androidx.compose.foundation.layout.fillMaxSize // ✅ Added
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // ✅ Added
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun MainHubScreen(
    userId: Int,
    onNavigateToModule: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Games") },
                    label = { Text("Games") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 2,
                    onClick = {
                        Toast.makeText(context, "Profile Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == 3,
                    onClick = {
                        Toast.makeText(context, "Settings Coming Soon", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) { // ✅ Added Box
            when (selectedTab) {
                0 -> HomeScreen(
                    userId = userId,
                    onNavigateToGame = onNavigateToModule
                )
                1 -> GamesScreen(
                    onGameClick = onNavigateToModule
                )
                else -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Coming Soon")
                    }
                }
            }
        }
    }
}