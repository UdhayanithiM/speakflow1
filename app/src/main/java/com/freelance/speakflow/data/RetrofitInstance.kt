package com.freelance.speakflow.data

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// ==========================================
// AUTH MODELS
// ==========================================

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    @SerializedName("user_id") val userId: Int,
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
    @SerializedName("total_xp") val totalXp: Int
)

// ==========================================
// DASHBOARD MODELS
// ==========================================

data class DashboardResponse(
    @SerializedName("user_name") val userName: String,
    @SerializedName("day_streak") val dayStreak: Int,
    @SerializedName("total_xp") val totalXp: Int,
    @SerializedName("current_level") val currentLevel: Int,
    @SerializedName("level_progress") val levelProgress: Float,
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
// VOCAB MODELS
// ==========================================

data class VocabPreviewResponse(
    val game_type: String,
    val category: String,
    val level: Int,
    val payload: VocabPreviewPayload
)

data class VocabPreviewPayload(
    val items: List<VocabPreviewItem>
)

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
    @SerializedName("question_id") val questionId: String,
    @SerializedName("target_word") val targetWord: String,
    @SerializedName("target_audio") val targetAudio: String,
    @SerializedName("correct_option_id") val correctOptionId: String,
    val options: List<VocabOption>
)

data class VocabOption(
    val id: String,
    val image: String,
    val word: String
)

data class VocabResultRequest(
    val score: Int,
    val total: Int
)

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
    val xp: Int,
    val message: String
)

// ==========================================
// SPEAKING MODELS
// ==========================================

data class SpeakingLesson(
    val id: Int,
    val title: String,
    val description: String,
    val level: Int,
    val image: String,
    @SerializedName("total_dialogues") val totalDialogues: Int,
    @SerializedName("is_locked") val isLocked: Boolean,
    @SerializedName("progress_percent") val progressPercent: Float
)

data class SpeakingLessonsResponse(
    val payload: List<SpeakingLesson>
)

data class SpeakingLessonDetailResponse(
    val payload: SpeakingLessonDetail
)

data class SpeakingLessonDetail(
    val id: Int,
    val title: String,
    val dialogues: List<SpeakingDialogue>
)

data class SpeakingDialogue(
    val id: Int,
    val order: Int,
    @SerializedName("ai_prompt") val aiPrompt: String,
    @SerializedName("target_response") val targetResponse: String,
    @SerializedName("audio_url") val audioUrl: String
)

data class SpeakingAnalysisResponse(
    val status: String,
    val data: SpeakingAnalysisData
)

data class SpeakingAnalysisData(
    val overall_score: Int,
    val metrics: SpeakingMetrics,
    val feedback: SpeakingFeedback,
    val word_analysis: List<WordAnalysis>,
    val debug_transcript: String
)

data class SpeakingMetrics(
    val fluency: Int,
    val clarity: Int,
    val accent: Int
)

data class SpeakingFeedback(
    val summary: String,
    val tips: List<String>
)

data class WordAnalysis(
    val word: String,
    val status: String,
    val score: Int,
    val correction: String?
)

data class SpeakingProgressRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("lesson_id") val lessonId: Int,
    @SerializedName("dialogue_id") val dialogueId: Int,
    val score: Int
)

// ==========================================
// GRAMMAR MODELS
// ==========================================

data class GrammarLevelResponse(
    val payload: GrammarLevelData
)

data class GrammarLevelData(
    val level: Int,
    val title: String,
    val description: String,
    val questions: List<GrammarQuestion>
)

data class GrammarQuestion(
    val id: String,
    val image: String,
    @SerializedName("correct_sentence") val correctSentence: String,
    @SerializedName("words_pool") val wordsPool: List<String>
)

// ==========================================
// SITUATIONS MODELS
// ==========================================

data class Situation(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("turns_count") val turnsCount: Int,
    val image: String
)

data class SituationsListResponse(
    val payload: List<Situation>
)

data class SituationDetailResponse(
    val payload: SituationDetail
)

data class SituationDetail(
    val id: String,
    val title: String,
    @SerializedName("total_turns") val totalTurns: Int,
    val turns: List<Turn>
)

data class Turn(
    @SerializedName("turn_id") val turnId: Int,
    @SerializedName("ai_avatar") val aiAvatar: String,
    @SerializedName("ai_text") val aiText: String,
    val options: List<TurnOption>?
)

data class TurnOption(
    val text: String,
    @SerializedName("is_correct") val isCorrect: Boolean,
    val points: Int
)

// ==========================================
// VOICE MATCH MODELS
// ==========================================

data class VoiceMatchLevelResponse(
    val payload: VoiceMatchLevel
)

data class VoiceMatchLevel(
    @SerializedName("level_id") val levelId: Int,
    val title: String,
    val description: String,
    val questions: List<VoiceMatchQuestion>
)

data class VoiceMatchQuestion(
    val id: String,
    @SerializedName("target_word") val targetWord: String,
    val image: String
)

// ==========================================
// ECHO GAME MODELS ✅ ADDED
// ==========================================

data class EchoLevelResponse(
    val payload: EchoLevel
)

data class EchoLevel(
    @SerializedName("level_id") val levelId: Int,
    val title: String,
    val description: String,
    val questions: List<EchoQuestion>
)

data class EchoQuestion(
    val id: String,
    val text: String
)
data class SpeedRaceLevelResponse(
    val payload: SpeedRaceLevel
)

data class SpeedRaceLevel(
    @SerializedName("level_id")
    val levelId: Int,
    val title: String,
    val description: String,
    @SerializedName("time_limit")
    val timeLimit: Long, // e.g., 60 seconds
    val questions: List<SpeedRaceQuestion>
)

data class SpeedRaceQuestion(
    val id: String,
    val text: String
)

// ==========================================
// API SERVICE
// ==========================================

interface ApiService {

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("/dashboard/{user_id}")
    suspend fun getDashboard(@Path("user_id") userId: Int): DashboardResponse

    @GET("/vocab/{category}/{level}/preview")
    suspend fun getVocabPreview(
        @Path("category") category: String,
        @Path("level") level: Int
    ): VocabPreviewResponse

    @GET("/vocab/{category}/{level}/listen-click")
    suspend fun getVocabListenClick(
        @Path("category") category: String,
        @Path("level") level: Int
    ): VocabListenClickResponse

    @POST("/vocab/{category}/result")
    suspend fun submitVocabResult(
        @Path("category") category: String,
        @Body request: VocabResultRequest
    ): VocabResultResponse

    @GET("/speaking/lessons/{user_id}")
    suspend fun getSpeakingLessons(@Path("user_id") userId: Int): SpeakingLessonsResponse

    @GET("/speaking/lesson/{lesson_id}")
    suspend fun getSpeakingLessonDetail(
        @Path("lesson_id") lessonId: Int
    ): SpeakingLessonDetailResponse

    @Multipart
    @POST("/speaking/analyze")
    suspend fun analyzeSpeaking(
        @Part file: MultipartBody.Part,
        @Part("target_sentence") targetSentence: RequestBody,
        @Part("lesson_id") lessonId: RequestBody
    ): SpeakingAnalysisResponse

    @POST("/speaking/progress")
    suspend fun saveSpeakingProgress(@Body request: SpeakingProgressRequest)

    @GET("/grammar/level/{level_id}")
    suspend fun getGrammarLevel(@Path("level_id") levelId: Int): GrammarLevelResponse

    @GET("/situations/all")
    suspend fun getSituationsList(): SituationsListResponse

    @GET("/situations/{id}")
    suspend fun getSituationDetail(@Path("id") id: String): SituationDetailResponse

    @GET("/games/voice-match/level/{level_id}")
    suspend fun getVoiceMatchLevel(
        @Path("level_id") levelId: Int
    ): VoiceMatchLevelResponse

    // -------- ECHO GAME ✅ ADDED --------
    @GET("/games/echo/level/{level_id}")
    suspend fun getEchoLevel(
        @Path("level_id") levelId: Int
    ): EchoLevelResponse

    // -------- SPEED RACE --------
    @GET("/games/speed-race/level/{level_id}")
    suspend fun getSpeedRaceLevel(
        @Path("level_id") levelId: Int
    ): SpeedRaceLevelResponse
}

// ==========================================
// RETROFIT INSTANCE
// ==========================================

object RetrofitInstance {

    const val BASE_URL = "http://10.63.87.149:8000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
