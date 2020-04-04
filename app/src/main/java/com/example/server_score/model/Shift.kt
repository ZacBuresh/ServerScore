package com.example.server_score.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Shifts(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "total_tips") val totalTips: Float?,
    @ColumnInfo(name = "total_sales") val totalSales: Float?,
    @ColumnInfo(name = "hours") val hours: Float?,
    @ColumnInfo(name = "add_ons") val addOns: Float?,
    @ColumnInfo(name = "check_time") val checkTime: Int?,
    @ColumnInfo(name = "date") val date: String?
)