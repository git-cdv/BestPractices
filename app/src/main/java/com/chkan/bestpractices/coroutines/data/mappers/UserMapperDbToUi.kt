package com.chkan.bestpractices.coroutines.data.mappers

import com.chkan.bestpractices.coroutines.data.room.UserDb
import com.chkan.bestpractices.coroutines.domain.User
import javax.inject.Inject

class UserMapperDbToUi @Inject constructor() {
    fun map(userDb:UserDb) : User {
        return User(name = userDb.name)
    }
    fun mapList(list: List<UserDb>) : List<User> {
        return list.map { User(name = it.name) }
    }
}