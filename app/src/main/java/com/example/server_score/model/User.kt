package com.example.server_score.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "avg_tips") val avgTips: Float?,
    @ColumnInfo(name = "avg_hourly") val avgHourly: Float?,
    @ColumnInfo(name = "avg_add_ons") val avgAddOns: Float?,
    @ColumnInfo(name = "avg_check_time") val avgCheckTime: Int?
)