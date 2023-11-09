package com.chkan.bestpractices.jwt_auth.data.refresh_token

import com.chkan.bestpractices.jwt_auth.data.auth.requests.AuthRequest
import com.chkan.bestpractices.jwt_auth.data.auth.responses.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("refreshtoken")
    suspend fun refreshToken(
        @Body request: AuthRequest
    ) : TokenResponse
}