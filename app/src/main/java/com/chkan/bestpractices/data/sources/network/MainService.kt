package com.chkan.bestpractices.data.sources.network

import com.chkan.bestpractices.data.models.ResponsePassengers
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {

    @GET(API_GET_PASSENGERS)
    suspend fun getPassengers(@Query("page") page: Int,
                             @Query("size") size: Int): ResponsePassengers

    companion object {
        const val API_GET_PASSENGERS = "/v1/passenger"
    }
}