package com.chkan.bestpractices.luep_translater.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TranslateRequest(
    val translatedLocale: String,
    val originalText: String
)