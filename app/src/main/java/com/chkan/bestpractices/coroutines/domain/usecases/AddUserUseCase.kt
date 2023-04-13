package com.chkan.bestpractices.coroutines.domain.usecases

import com.chkan.bestpractices.core.ResultOf
import com.chkan.bestpractices.coroutines.domain.User
import com.chkan.bestpractices.coroutines.domain.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor (
    private val userRepository: UserRepository,
) {

    suspend fun run(user: User): ResultOf<Unit> {

        try {
            userRepository.addUser(user)
        } catch (e: Exception) {
            return ResultOf.Error(e.message,e)
        }

        return ResultOf.Success(Unit)
    }
}