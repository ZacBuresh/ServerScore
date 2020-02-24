package com.example.server_score.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ShiftDao {

    @Insert
    suspend fun insert(shift: Shifts)

    @Delete
    fun delete(shift: Shifts)

    @Query("SELECT * FROM shifts")
    suspend fun getAllShifts() : List<Shifts>
}