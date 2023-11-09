package com.chkan.bestpractices.jwt_auth.data.refresh_token

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

//only @SingleTon
class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return if (isRequestRequiresAuth(response)) {
            val request = response.request
            authenticateRequestUsingFreshAccessToken(request, retryCount(request) + 1)
        } else {
            null
        }
    }

    private fun retryCount(request: Request): Int =
        request.header("RetryCount")?.toInt() ?: 0

    @Synchronized
    private fun authenticateRequestUsingFreshAccessToken(
        request: Request,
        retryCount: Int
    ): Request? {
        return runBlocking {
            if (retryCount > 2) return@runBlocking null

            tokenRepository.getSavedToken()?.let { lastSavedAccessToken ->
                val accessTokenOfRequest = request.header("Authorization")?.substringAfter("Bearer ") // Some string manipulation needed here to get the token if you have a Bearer token

                if (accessTokenOfRequest != lastSavedAccessToken) {
                    Log.d("CHKAN", "TOKEN REFRESHED - 1 way")
                    return@runBlocking getNewRequest(request, retryCount, lastSavedAccessToken)
                } else {
                    tokenRepository.refreshToken()?.let { freshAccessToken ->
                        Log.d("CHKAN", "TOKEN REFRESHED - 2 way")
                        return@runBlocking getNewRequest(request, retryCount, freshAccessToken)
                    }
                }
            }
            Log.d("CHKAN", "ERROR REFRESH")
            return@runBlocking null
        }
    }

    private fun getNewRequest(request: Request, retryCount: Int, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .header("RetryCount", "$retryCount")
            .build()
    }

    // check that this request needed auth
    private fun isRequestRequiresAuth(response: Response): Boolean {
        val header = response.request.header("Authorization")
        return header != null && header.startsWith("Bearer ")
    }
}