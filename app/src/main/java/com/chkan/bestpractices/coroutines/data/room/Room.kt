package com.chkan.bestpractices.coroutines.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM userdb")
    fun getUsersFlow(): Flow<List<UserDb>>

    @Query("SELECT * FROM userdb")
    suspend fun getUsers(): List<UserDb>

    @Insert
    suspend fun insertAll(users: List<UserDb>)

    @Insert
    suspend fun insert(user: UserDb)

    @Query("DELETE FROM userdb")
    suspend fun deleteAll()

}

@Database(entities = [UserDb::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}