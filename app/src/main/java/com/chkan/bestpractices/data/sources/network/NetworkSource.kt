package com.chkan.bestpractices.data.sources.network

import com.chkan.bestpractices.data.MyResult
import com.chkan.bestpractices.data.models.ResponsePassengers
import java.lang.Exception
import javax.inject.Inject

class NetworkSource @Inject constructor(private val api: MainService) {

    suspend fun getPassengers(page: Int,size:Int) : MyResult<ResponsePassengers> {
        return try {
            val list = api.getPassengers(page,size)
            MyResult.success(list)
        } catch (e: Exception) {
            MyResult.error(e)
        }
    }

}

