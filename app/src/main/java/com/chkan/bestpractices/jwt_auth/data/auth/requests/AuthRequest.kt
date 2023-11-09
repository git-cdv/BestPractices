package com.chkan.bestpractices.jwt_auth.data.auth.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)