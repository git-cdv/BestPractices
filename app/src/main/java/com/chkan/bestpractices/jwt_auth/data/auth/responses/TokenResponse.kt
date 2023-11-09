package com.chkan.bestpractices.jwt_auth.data.auth.responses

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)