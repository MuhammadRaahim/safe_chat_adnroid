package com.instances.safechat.db

import androidx.room.*

@Dao
interface UserDoa {
    @Query("SELECT * FROM userEntity where id = :userId")
    fun getUser(userId:String): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Query("DELETE FROM UserEntity")
    fun deleteAllUsers()
}