package com.chkan.bestpractices.luep_translater.data

import com.chkan.bestpractices.luep_translater.data.models.AuthResponse
import com.chkan.bestpractices.luep_translater.data.models.BaseResponse
import com.chkan.bestpractices.luep_translater.data.models.LoginRequest
import com.chkan.bestpractices.luep_translater.data.models.TranslateRequest
import com.chkan.bestpractices.luep_translater.data.models.TranslateResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LuepService {

    @POST(LOGIN)
    suspend fun loginUser(
        @Body body: LoginRequest,
    ): BaseResponse<AuthResponse>

    @POST(TRANSLATE)
    suspend fun translate(
        @Header("Authorization") token: String,
        @Body body: TranslateRequest,
    ): BaseResponse<TranslateResponse>

    companion object {
        private const val LOGIN = "/api/v2/auth/signin"
        private const val TRANSLATE = "/api/v2/translate"
    }

}