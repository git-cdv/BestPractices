package com.chkan.bestpractices.jwt_auth.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)