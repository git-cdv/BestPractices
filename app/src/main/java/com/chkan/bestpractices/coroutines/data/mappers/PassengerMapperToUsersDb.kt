package com.chkan.bestpractices.coroutines.data.mappers

import com.chkan.bestpractices.coroutines.data.room.UserDb
import com.chkan.bestpractices.simple_paging.data.sources.network.models.PassengersDataModel
import javax.inject.Inject

class PassengerMapperToUsersDb @Inject constructor() {

    fun mapList(apiModel: PassengersDataModel) : List<UserDb> {
        return apiModel.list.map { UserDb(name = it.firstName) }
    }
}