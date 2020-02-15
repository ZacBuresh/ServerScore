package com.example.server_score.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}