package com.chkan.bestpractices.jwt_auth.data.refresh_token

import android.content.SharedPreferences
import com.chkan.bestpractices.jwt_auth.data.auth.requests.AuthRequest
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val api: RefreshTokenApi,
    private val prefs: SharedPreferences
): TokenRepository {

    override suspend fun getSavedToken() : String? {
        return try {
            prefs.getString("jwt", null)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun refreshToken(): String? {
        val request = AuthRequest(username = prefs.getString("name", "") ?: "", password = "")
        return try {
            val token = api.refreshToken(request).token
            prefs.edit()
                .putString("jwt", token)
                .apply()
            token
        } catch (e: Exception) {
            null
        }
    }
}
