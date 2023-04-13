package com.chkan.bestpractices.coroutines.domain.usecases

import com.chkan.bestpractices.coroutines.domain.UserRepository
import javax.inject.Inject

class DeleteAllUsersUseCase @Inject constructor (private val userRepository: UserRepository) {

    suspend fun run() {
        userRepository.deleteAllUsers()
    }
}