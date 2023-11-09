package com.chkan.bestpractices.jwt_auth.data.auth.api

import com.chkan.bestpractices.jwt_auth.data.auth.requests.AuthRequest
import com.chkan.bestpractices.jwt_auth.data.auth.responses.InfoResponse
import com.chkan.bestpractices.jwt_auth.data.auth.responses.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("signup")
    suspend fun signUp(
        @Body request: AuthRequest
    )

    @POST("signin")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

    @GET("secret")
    suspend fun getInfo(
        @Header("Authorization") token: String
    ) : InfoResponse
}