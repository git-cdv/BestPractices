package com.chkan.bestpractices.auth.data.github.models

import kotlinx.serialization.Serializable


@Serializable
data class RemoteGithubUser(
    val id: Long,
    val login: String,
    val name: String,
)