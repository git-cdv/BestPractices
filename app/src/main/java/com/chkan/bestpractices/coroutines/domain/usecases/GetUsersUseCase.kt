package com.chkan.bestpractices.coroutines.domain.usecases

import com.chkan.bestpractices.coroutines.domain.User
import com.chkan.bestpractices.coroutines.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor (private val userRepository: UserRepository) {

    fun run(): Flow<List<User>> {
        return userRepository.getFlowUsers()
    }

}