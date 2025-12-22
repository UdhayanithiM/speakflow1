package com.freelance.speakflow.ui.screens

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.freelance.speakflow.data.LoginRequest
import com.freelance.speakflow.data.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit,        // ✅ UPDATED
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun doLogin() {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        scope.launch {
            try {
                // ✅ Capture response
                val response = RetrofitInstance.api.login(
                    LoginRequest(email.trim(), password.trim())
                )

                // ✅ Pass user_id upward
                onLoginSuccess(response.userId)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    // UI-only composable stays untouched
    LoginLayout(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        isLoading = isLoading,
        onLoginClick = { doLogin() },
        onNavigateToRegister = onNavigateToRegister
    )
}
