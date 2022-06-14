package com.chkan.bestpractices.data.sources.network

import com.chkan.bestpractices.data.sources.network.models.PassengersDataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkPassengersSource {

    @GET(API_GET_PASSENGERS)
    suspend fun getPassengers(@Query("page") page: Int,
                             @Query("limit") limit: Int): PassengersDataModel

    companion object {
        const val API_GET_PASSENGERS = "/data/v1/user"
    }
}