package com.chkan.bestpractices.coroutines.domain.usecases

import com.chkan.bestpractices.core.ResultOf
import com.chkan.bestpractices.coroutines.domain.User
import com.chkan.bestpractices.coroutines.domain.UserRepository
import javax.inject.Inject

class FetchOrGetUsersUseCase @Inject constructor (
    private val userRepository: UserRepository
) {
    suspend fun run() : ResultOf<Unit> {
            return try {
                userRepository.fetchUsers()
                ResultOf.Success(Unit)
            } catch (e: Exception) {
                ResultOf.Error(e.message,e)
            }
    }

    suspend fun runInParallel(page:Int) : List<User> {
        return userRepository.fetchUsersInParallel(page)
    }
}