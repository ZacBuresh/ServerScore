package com.example.server_score.add

import android.content.Context
import androidx.room.Room
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AddPresenter(val v: AddInterface, private val context: Context) {

    fun startPresenter() {

    }

    fun insertShift(shift: Shifts) = runBlocking {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()
        db.shiftDao().insert(shift)
    }

    fun getDayOfWeek(){
        val date = Calendar.getInstance().time
        val formatted = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        v.getDayOfWeek(formatted.toString())
    }
}