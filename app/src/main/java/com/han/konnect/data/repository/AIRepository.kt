package com.han.konnect.data.repository

import com.google.gson.Gson
import com.han.konnect.data.model.AICorrectionResponse
import com.han.konnect.data.remote.ApiClient
import com.han.konnect.data.remote.AIPrompts
import com.han.konnect.data.remote.dto.GeminiContent
import com.han.konnect.data.remote.dto.GeminiPart
import com.han.konnect.data.remote.dto.GeminiRequest

class AIRepository {

    private val apiService = ApiClient.apiService
    private val gson = Gson()

    private val apiKey = "API_KEY"

    suspend fun fetchAICorrection(originalText: String): Result<AICorrectionResponse> {
        return try {
            val systemPrompt = AIPrompts.getCorrectionSystemPrompt()
            val userPrompt = "다음 문장을 교정해줘: \"$originalText\""

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        role = "user",
                        parts = listOf(
                            GeminiPart(text = "$systemPrompt\n\n$userPrompt")
                        )
                    )
                )
            )

            val response = apiService.generateCorrection(apiKey = apiKey, request = request)

            if (response.isSuccessful && response.body() != null) {
                val jsonString = response.body()
                    ?.candidates?.firstOrNull()
                    ?.content?.parts?.firstOrNull()?.text

                if (!jsonString.isNullOrBlank()) {
                    val parsedResponse = gson.fromJson(jsonString, AICorrectionResponse::class.java)
                    Result.success(parsedResponse)
                } else {
                    Result.failure(Exception("AI 응답이 비어있습니다."))
                }
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}