package com.example.server_score.model

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: Users)

    @Delete
    suspend fun delete(user: Users)

    @Update
    suspend fun update(user: Users)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<Users>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int


}