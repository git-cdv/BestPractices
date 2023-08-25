package com.chkan.bestpractices.luep_translater.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponse(
    @SerialName("translated_text")
    val translatedText: String
)