package com.chkan.bestpractices.domain

import com.chkan.bestpractices.data.MyResult
import com.chkan.bestpractices.data.models.ResponsePassengers
import com.chkan.bestpractices.data.sources.network.NetworkSource
import javax.inject.Inject

class GetDataUseCase @Inject constructor(private val networkSource : NetworkSource) {

    suspend fun getPassengers(page: Int,size:Int) : MyResult<ResponsePassengers> {
        return networkSource.getPassengers(page,size)
    }
}