package com.example.server_score.score

import android.content.Context
import androidx.room.Room
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.model.Users
import kotlinx.coroutines.runBlocking

class ScorePresenter(val v: ScoreInterface, val context: Context) {

    fun startPresenter(username: String){
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        calculateUserStatistics(db, username)
        calculateServerScore(db, username)
    }


    private fun getShifts(db: AppDatabase): List<Shifts> = runBlocking {
        db.shiftDao().getAllShifts()
    }

    private fun getUsers(db: AppDatabase): List<Users> = runBlocking {
        db.userDao().getAllUsers()
    }

    private fun updateUser(db: AppDatabase, updatedUser: Users) = runBlocking {
        db.userDao().update(updatedUser)
    }

    private fun calculateUserStatistics(db: AppDatabase, username: String){
        var shiftCount = 0; var tipTotal = 0F; var salesTotal = 0F; var hoursTotal = 0F
        var addOnsTotal = 0F; var checkTimeTotal = 0
        getShifts(db).forEach() {
            if (it.name == username) {
                shiftCount++
                tipTotal += it.totalTips!!; salesTotal += it.totalSales!!; hoursTotal += it.hours!!
                addOnsTotal += it.addOns!!; checkTimeTotal += it.checkTime!!
            }
        }

        var userShiftCount = 0
        var userId = 0
        var ct = 0
        getUsers(db).forEach() {
            userId++
            if (it.name == username && ct == 0) {
                ct++
                userShiftCount++
                if (userShiftCount == 0 || shiftCount == 0) {
                    v.initializeTextViews()
                } else {
                    val updatedUser = Users(
                        it.uid, username, tipTotal / shiftCount, salesTotal / shiftCount,
                        tipTotal / hoursTotal, addOnsTotal / shiftCount,
                        checkTimeTotal / shiftCount
                    )
                    updateUser(db, updatedUser)
                    v.setTextViews(updatedUser)
                }
            }
        }
    }

    private fun calculateServerScore(db: AppDatabase, username: String){
        getUsers(db).forEach(){
            if(it.name == username){
                if(it.avgHourly == null || it.avgCheckTime == null || it.avgAddOns == null || it.avgTips == null){
                    v.initializeScore()
                }
                else {
                    val hourlyResult = it.avgHourly - ScoreActivity.STANDARD_HOURLY_AVG
                    val addonResult = it.avgAddOns - ScoreActivity.STANDARD_ADD_ON_AVG
                    val checktimeResult = ScoreActivity.STANDARD_CHECK_TIME_AVG - it.avgCheckTime
                    val serverScore =
                        String.format("%.0f", hourlyResult + addonResult + checktimeResult)
                    v.setServerScore(serverScore)
                }
            }
        }
    }
}