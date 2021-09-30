package com.example.server_score.predictions

import android.content.Context
import androidx.room.Room
import com.example.server_score.R
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.model.Users
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.runBlocking
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PredictionsPresenter(val v: PredictionsInterface, val context: Context) {

    var predMon = 0F; var predTues = 0F; var predWed = 0F; var predThur = 0F; var predFri = 0F
    var predSat = 0F; var predSun = 0F

    fun startPresenter(username: String) {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        val entries: ArrayList<BarEntry> = ArrayList()
        addBarChartData(db, username, entries)

        val dataset = BarDataSet(entries, "Avg Total Sales")
        v.sendDataSet(dataset)
        val data = BarData(dataset)
        v.sendData(data)
    }

    fun getDayOfWeek(){
        val date = Calendar.getInstance().time
        val formatted = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        v.getDayOfWeek(formatted.toString())
    }

    fun getShifts(db: AppDatabase): List<Shifts> = runBlocking {
        db.shiftDao().getAllShifts()
    }

    fun getUsers(db: AppDatabase): List<Users> = runBlocking {
        db.userDao().getAllUsers()
    }

    fun addBarChartData(db: AppDatabase, username: String, entries: ArrayList<BarEntry>){
        var numMon = 0; var numTues = 0; var numWed = 0; var numThur = 0; var numFri = 0
        var numSat = 0; var numSun = 0
        var salesMon = 0F; var salesTues = 0F; var salesWed = 0F; var salesThur = 0F; var salesFri = 0F
        var salesSat = 0F; var salesSun = 0F
        getShifts(db).forEach() {
            if (it.name == username) {
                val day = it.date
                when(day){
                    "Monday" -> numMon++
                    "Tuesday" -> numTues++
                    "Wednesday" -> numWed++
                    "Thursday" -> numThur++
                    "Friday" -> numFri++
                    "Saturday" -> numSat++
                    "Sunday" -> numSun++
                }
                when(day){
                    "Monday" -> salesMon += it.totalSales!!
                    "Tuesday" -> salesTues += it.totalSales!!
                    "Wednesday" -> salesWed += it.totalSales!!
                    "Thursday" -> salesThur += it.totalSales!!
                    "Friday" -> salesFri += it.totalSales!!
                    "Saturday" -> salesSat += it.totalSales!!
                    "Sunday" -> salesSun += it.totalSales!!
                }
            }
        }

        val avgMon:Float = if(numMon == 0){
            0F
        } else{
            salesMon / numMon
        }

        val avgTues:Float = if(numTues == 0){
            0F
        } else{
            salesTues / numTues
        }

        val avgWed:Float = if(numWed == 0){
            0F
        } else{
            salesWed / numWed
        }

        val avgThur:Float = if(numThur == 0){
            0F
        } else{
            salesThur / numThur
        }

        val avgFri:Float = if(numFri == 0){
            0F
        } else{
            salesFri / numFri
        }

        val avgSat:Float = if(numSat == 0){
            0F
        } else{
            salesSat / numSat
        }

        val avgSun:Float = if(numSun == 0){
            0F
        } else{
            salesSun / numSun
        }

        entries.add(BarEntry(0F,  avgMon))
        entries.add(BarEntry(1F, avgTues))
        entries.add(BarEntry(2F, avgWed))
        entries.add(BarEntry(3F, avgThur))
        entries.add(BarEntry(4F, avgFri))
        entries.add(BarEntry(5F, avgSat))
        entries.add(BarEntry(6F, avgSun))

        getUsers(db).forEach() {
            if (username == it.name) {
                try{
                    val userTipAvg = it.avgTips!! / it.avgSales!!

                    predMon = avgMon * userTipAvg
                    predTues = avgTues * userTipAvg
                    predWed = avgWed * userTipAvg
                    predThur = avgThur * userTipAvg
                    predFri = avgFri * userTipAvg
                    predSat = avgSat * userTipAvg
                    predSun = avgSun * userTipAvg
                }

                catch (e: NullPointerException){
                    val userTipAvg = 0
                    predMon = avgMon * userTipAvg
                    predTues = avgTues * userTipAvg
                    predWed = avgWed * userTipAvg
                    predThur = avgThur * userTipAvg
                    predFri = avgFri * userTipAvg
                    predSat = avgSat * userTipAvg
                    predSun = avgSun * userTipAvg
                }
            }
        }
    }

    fun getPredictions() {
        v.sendPredictions(predMon, predTues, predWed, predThur, predFri, predSat, predSun)
    }
}