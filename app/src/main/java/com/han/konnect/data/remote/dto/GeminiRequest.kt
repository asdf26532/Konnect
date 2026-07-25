package com.han.konnect.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents")
    val contents: List<GeminiContent>,

    @SerializedName("generationConfig")
    val generationConfig: GeminiGenerationConfig? = GeminiGenerationConfig()
)

data class GeminiContent(
    @SerializedName("role")
    val role: String = "user",
    @SerializedName("parts")
    val parts: List<GeminiPart>
)

data class GeminiPart(
    @SerializedName("text")
    val text: String
)

data class GeminiGenerationConfig(
    @SerializedName("responseMimeType")
    val responseMimeType: String = "application/json",
    @SerializedName("temperature")
    val temperature: Float = 0.2f
)