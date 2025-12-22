package com.freelance.speakflow.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.google.gson.annotations.SerializedName

// ==========================================
// 1. AUTH MODELS
// ==========================================

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    @SerializedName("user_id")
    val userId: Int,
    val xp: Int
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("total_xp")
    val totalXp: Int
)

// ==========================================
// 2. DASHBOARD MODELS
// ==========================================

data class DashboardResponse(
    @SerializedName("user_name")
    val userName: String,

    @SerializedName("day_streak")
    val dayStreak: Int,

    @SerializedName("total_xp")
    val totalXp: Int,

    @SerializedName("current_level")
    val currentLevel: Int,

    @SerializedName("level_progress")
    val levelProgress: Float,

    val modules: List<ModuleItem>
)

data class ModuleItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val locked: Boolean
)

// ==========================================
// 3. VOCAB GAME — PART 2 (LISTEN & CLICK)
// ==========================================

data class VocabListenClickResponse(
    val game_type: String,
    val category: String,
    val part: Int,
    val total_parts: Int,
    val payload: ListenClickPayload
)

data class ListenClickPayload(
    val questions: List<QuizQuestion>
)

data class QuizQuestion(

    @SerializedName("question_id")
    val questionId: String,

    @SerializedName("target_word")
    val targetWord: String,

    @SerializedName("target_audio")
    val targetAudio: String,
    @SerializedName("correct_option_id")
    val correctOptionId: String,

    val options: List<VocabOption>
)

data class VocabOption(
    val id: String,
    val image: String
)

// ==========================================
// 4. VOCAB GAME — PART 3 (RESULT)
// ==========================================

data class VocabResultResponse(
    val game_type: String,
    val category: String,
    val part: Int,
    val total_parts: Int,
    val payload: VocabResultPayload
)

data class VocabResultPayload(
    val score: Int,
    val total: Int,
    val xp: Int
)

// ==========================================
// 5. API SERVICE
// ==========================================

interface ApiService {

    // -------- AUTH --------
    @POST("/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    // -------- DASHBOARD --------
    @GET("/dashboard/{user_id}")
    suspend fun getDashboard(
        @Path("user_id") userId: Int
    ): DashboardResponse

    // -------- VOCAB PART 2 --------
    @GET("/vocab/{category}/listen-click")
    suspend fun getVocabListenClick(
        @Path("category") category: String
    ): VocabListenClickResponse

    // -------- VOCAB PART 3 --------
    @POST("/vocab/{category}/result")
    suspend fun submitVocabResult(
        @Path("category") category: String,
        @Query("score") score: Int,
        @Query("total") total: Int
    ): VocabResultResponse
}

// ==========================================
// 6. RETROFIT INSTANCE
// ==========================================

object RetrofitInstance {

    private const val BASE_URL = "http://10.63.87.149:8000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
