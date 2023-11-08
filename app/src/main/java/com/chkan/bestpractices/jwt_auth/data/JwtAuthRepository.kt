package com.chkan.bestpractices.jwt_auth.data

import com.chkan.bestpractices.jwt_auth.data.utils.AuthResult

interface JwtAuthRepository {
    suspend fun signUp(username: String, password: String): AuthResult<String>
    suspend fun signIn(username: String, password: String): AuthResult<String>
    suspend fun authenticate(): AuthResult<String>
    suspend fun getInfo(): AuthResult<String>
}