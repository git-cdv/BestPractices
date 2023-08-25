package com.chkan.bestpractices.luep_translater.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val smsVerificationEnabled: Boolean? = null
)