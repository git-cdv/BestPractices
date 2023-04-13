package com.chkan.bestpractices.simple_paging.data.repo

import com.chkan.bestpractices.core.ResultOf
import com.chkan.bestpractices.simple_paging.data.sources.network.NetworkPassengersSource
import com.chkan.bestpractices.simple_paging.data.sources.network.models.PassengersDataModel
import com.chkan.bestpractices.simple_paging.domain.models.PassengersDomainModel
import com.chkan.bestpractices.simple_paging.domain.repo.PassengersRepo
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PassengersRepoImpl @Inject constructor(private val networkApi: NetworkPassengersSource) :
    PassengersRepo {

    override suspend fun getPassengers(page: Int, size:Int) =
        try {
            val list = networkApi.getPassengers(page,size)
            ResultOf.Success(list.mapToPassengersDomain())
        } catch (e: Exception) {
            ResultOf.Error(e.message,e)
        }

    /**
     * Convert Network results to Domain objects
     */
    private fun PassengersDataModel.mapToPassengersDomain(): List<PassengersDomainModel> {
        return list.map {
            PassengersDomainModel(
                id = it.id,
                firstName = it.firstName,
                lastName = it.lastName,
                picture = it.picture,
                total = total
            )
        }
    }
}

