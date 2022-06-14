package com.chkan.bestpractices.domain.repo


import com.chkan.bestpractices.core.MyResult
import com.chkan.bestpractices.domain.models.PassengersDomainModel

/**
 * @author Dmytro Chkan on 14.06.2022.
 */
interface PassengersRepo {
    suspend fun getPassengers(page: Int, limit:Int) : MyResult<List<PassengersDomainModel>>
}