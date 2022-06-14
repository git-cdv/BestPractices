package com.chkan.bestpractices.domain.usecases

import com.chkan.bestpractices.domain.repo.PassengersRepo
import javax.inject.Inject

class GetPassengersUseCase @Inject constructor(private val passengersRepo : PassengersRepo) {
    suspend fun getPassengers(page: Int,limit:Int) = passengersRepo.getPassengers(page,limit)
}