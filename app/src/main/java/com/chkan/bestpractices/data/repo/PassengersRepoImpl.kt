package com.chkan.bestpractices.data.repo

import com.chkan.bestpractices.core.MyResult
import com.chkan.bestpractices.data.sources.network.NetworkPassengersSource
import com.chkan.bestpractices.data.sources.network.models.PassengersDataModel
import com.chkan.bestpractices.domain.models.PassengersDomainModel
import com.chkan.bestpractices.domain.repo.PassengersRepo
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PassengersRepoImpl @Inject constructor(private val networkApi: NetworkPassengersSource) : PassengersRepo {

    override suspend fun getPassengers(page: Int, size:Int) =
        try {
            val list = networkApi.getPassengers(page,size)
            MyResult.success(list.mapToPassengersDomain())
        } catch (e: Exception) {
            MyResult.error(e)
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

