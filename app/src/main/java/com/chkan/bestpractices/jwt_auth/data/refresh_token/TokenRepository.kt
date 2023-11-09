package com.chkan.bestpractices.jwt_auth.data.refresh_token

interface TokenRepository {
    suspend fun getSavedToken() : String?
    suspend fun refreshToken() : String?
}