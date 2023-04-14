package com.chkan.bestpractices.coroutines.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getFlowUsers(): Flow<List<User>>
    suspend fun getUsers(): List<User>
    suspend fun addUser(user: User)
    suspend fun deleteAllUsers()
    suspend fun fetchUsers()
    suspend fun fetchUsersInParallel(page:Int) : List<User>
}