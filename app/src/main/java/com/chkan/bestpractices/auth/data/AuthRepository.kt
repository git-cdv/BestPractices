package com.chkan.bestpractices.auth.data

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest

class AuthRepository {

    fun corruptAccessToken() {
        TokenStorage.accessToken = "fake token"
    }

    fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null
    }

    fun getAuthRequest(): AuthorizationRequest {
        return AppAuth.getAuthRequest()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return AppAuth.getEndSessionRequest()
    }

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ) {
        val tokens = AppAuth.performTokenRequestSuspend(authService, tokenRequest)
        //обмен кода на токен произошел успешно, сохраняем токены и завершаем авторизацию
        TokenStorage.accessToken = tokens.accessToken
        TokenStorage.refreshToken = tokens.refreshToken
        TokenStorage.idToken = tokens.idToken
    }
}