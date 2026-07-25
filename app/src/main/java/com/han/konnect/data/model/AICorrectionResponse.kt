package com.han.konnect.data.model

import com.google.gson.annotations.SerializedName

data class AICorrectionResponse(
    @SerializedName("original_text")
    val originalText: String,

    @SerializedName("corrected_text")
    val correctedText: String,

    @SerializedName("reason")
    val reason: String,

    @SerializedName("correction_type")
    val correctionType: String
)