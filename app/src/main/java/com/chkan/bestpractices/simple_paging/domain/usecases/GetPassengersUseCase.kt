package com.chkan.bestpractices.simple_paging.domain.usecases

import com.chkan.bestpractices.simple_paging.domain.repo.PassengersRepo
import javax.inject.Inject

class GetPassengersUseCase @Inject constructor(private val passengersRepo : PassengersRepo) {
    suspend fun getPassengers(page: Int,limit:Int) = passengersRepo.getPassengers(page,limit)
}