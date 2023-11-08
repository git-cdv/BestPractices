package com.chkan.bestpractices.jwt_auth.data.utils

import android.content.SharedPreferences
import android.util.Log
import com.chkan.bestpractices.jwt_auth.data.requests.AuthRequest
import com.chkan.bestpractices.jwt_auth.data.api.RefreshTokenApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val api: RefreshTokenApi,
    private val prefs: SharedPreferences
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val request = AuthRequest(username = prefs.getString("name","") ?: "", password = "" )
            try {
                val token = api.refreshToken(request).token
                prefs.edit()
                    .putString("jwt", token)
                    .apply()
                Log.d("CHKAN", "TOKEN REFRESHED")
                response.request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            }catch (e:Exception){
                Log.d("CHKAN", "ERROR REFRESH: ${e.message}")
               null
            }
        }
    }
}