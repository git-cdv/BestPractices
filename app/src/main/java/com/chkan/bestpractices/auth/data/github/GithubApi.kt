package com.chkan.bestpractices.auth.data.github

import com.chkan.bestpractices.auth.data.github.models.RemoteGithubUser
import retrofit2.http.GET

interface GithubApi {
    @GET("user")
    suspend fun getCurrentUser(
    ): RemoteGithubUser
}
