package com.example.server_score.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface ShiftDao {
    @Insert
    fun insert(shift: Shift)

    @Delete
    fun delete(shift: Shift)
}