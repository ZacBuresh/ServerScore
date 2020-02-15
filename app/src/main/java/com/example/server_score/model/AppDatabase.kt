package com.example.server_score.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Shift::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shiftDao(): ShiftDao
}