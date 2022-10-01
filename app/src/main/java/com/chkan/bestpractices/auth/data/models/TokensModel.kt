package com.chkan.bestpractices.auth.data.models

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)
