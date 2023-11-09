package com.chkan.bestpractices.jwt_auth.data.auth

import android.content.SharedPreferences
import android.util.Log
import com.chkan.bestpractices.jwt_auth.data.auth.JwtAuthRepository
import com.chkan.bestpractices.jwt_auth.data.auth.api.AuthApi
import com.chkan.bestpractices.jwt_auth.data.auth.requests.AuthRequest
import com.chkan.bestpractices.jwt_auth.data.utils.AuthResult
import retrofit2.HttpException
import javax.inject.Inject

class JwtAuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): JwtAuthRepository {

    override suspend fun signUp(username: String, password: String): AuthResult<String> {
        return try {
            api.signUp(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            signIn(username, password)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.d("CHKAN", "ERROR: ${e.message}")
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<String> {
        return try {
            val response = api.signIn(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .putString("name", username)
                .apply()
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<String> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun getInfo(): AuthResult<String> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            val infoResponse = api.getInfo("Bearer $token")
            AuthResult.Authorized(data = infoResponse.message)
        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.d("CHKAN", "ERROR: ${e.message}")
            AuthResult.UnknownError()
        }
    }
}