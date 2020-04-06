package com.example.server_score.predictions

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet

interface PredictionsInterface {
    fun sendDataSet(dataset: BarDataSet)
    fun sendData(data: BarData)
    fun getDayOfWeek(today: String)
    fun sendPredictions(predMon: Float, predTues: Float, predWed: Float, predThur: Float, predFri: Float, predSat: Float, predSun: Float)
}