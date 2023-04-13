package com.chkan.bestpractices.coroutines.data

import com.chkan.bestpractices.coroutines.data.mappers.PassengerMapperToUsersDb
import com.chkan.bestpractices.coroutines.data.mappers.UserMapperDbToUi
import com.chkan.bestpractices.coroutines.data.mappers.UserMapperUiToDb
import com.chkan.bestpractices.coroutines.data.room.UserDao
import com.chkan.bestpractices.coroutines.domain.User
import com.chkan.bestpractices.coroutines.domain.UserRepository
import com.chkan.bestpractices.simple_paging.data.sources.network.NetworkPassengersSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userMapperDbToUi: UserMapperDbToUi,
    private val userMapperUiToDb: UserMapperUiToDb,
    private val passMapperToUserDb: PassengerMapperToUsersDb,
    private val networkApi: NetworkPassengersSource
) : UserRepository {

    override fun getFlowUsers(): Flow<List<User>> {
        return userDao.getUsersFlow().map { userMapperDbToUi.mapList(it) }
    }

    override suspend fun getUsers(): List<User> {
        return userDao.getUsers().map { userMapperDbToUi.map(it) }
    }

    override suspend fun addUser(user: User) {
        userDao.insert(userMapperUiToDb.map(user))
    }

    override suspend fun deleteAllUsers() {
        userDao.deleteAll()
    }

    override suspend fun fetchUsers() {
        val usersApi = networkApi.getPassengers(0,10)
        val usersDb = passMapperToUserDb.mapList(usersApi)
        userDao.deleteAll()
        userDao.insertAll(usersDb)
    }

}