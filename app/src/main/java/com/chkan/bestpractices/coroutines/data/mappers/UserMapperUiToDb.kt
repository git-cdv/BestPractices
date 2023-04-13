package com.chkan.bestpractices.coroutines.data.mappers

import com.chkan.bestpractices.coroutines.data.room.UserDb
import com.chkan.bestpractices.coroutines.domain.User
import javax.inject.Inject

class UserMapperUiToDb @Inject constructor() {
    fun map(user: User) : UserDb {
        return UserDb(name = user.name, uid = null)
    }
}