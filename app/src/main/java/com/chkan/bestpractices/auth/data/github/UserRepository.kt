package com.chkan.bestpractices.auth.data.github

import com.chkan.bestpractices.auth.data.github.models.RemoteGithubUser
import javax.inject.Inject


class UserRepository @Inject constructor (val service: GithubApi) {
    suspend fun getUserInformation(): RemoteGithubUser {
        return service.getCurrentUser()
    }
}