package com.chkan.bestpractices.simple_paging.domain.repo


import com.chkan.bestpractices.core.ResultOf
import com.chkan.bestpractices.simple_paging.domain.models.PassengersDomainModel

/**
 * @author Dmytro Chkan on 14.06.2022.
 */
interface PassengersRepo {
    suspend fun getPassengers(page: Int, limit:Int) : ResultOf<List<PassengersDomainModel>>
}