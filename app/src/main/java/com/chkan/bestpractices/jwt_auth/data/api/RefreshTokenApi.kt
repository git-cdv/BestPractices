package com.chkan.bestpractices.jwt_auth.data.api

import com.chkan.bestpractices.jwt_auth.data.requests.AuthRequest
import com.chkan.bestpractices.jwt_auth.data.responses.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("refreshtoken")
    suspend fun refreshToken(
        @Body request: AuthRequest
    ) : TokenResponse
}