package com.example.server_score.predictions

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.server_score.MainActivity
import com.example.server_score.R
import com.example.server_score.add.AddActivity
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.model.Users
import com.example.server_score.score.ScoreActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.navigationView
import kotlinx.android.synthetic.main.activity_predictions.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList


class PredictionsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predictions)

        toolbar = findViewById(R.id.toolbar_score)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
        val username = intent.getStringExtra("USERNAME")
        toolbarTitle.text = username
        val item: MenuItem = navigationView.menu.getItem(2)
        item.isChecked = true

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

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

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(0F,  avgMon))
        entries.add(BarEntry(1F, avgTues))
        entries.add(BarEntry(2F, avgWed))
        entries.add(BarEntry(3F, avgThur))
        entries.add(BarEntry(4F, avgFri))
        entries.add(BarEntry(5F, avgSat))
        entries.add(BarEntry(6F, avgSun))

        val dataset = BarDataSet(entries, "Avg Total Sales")

        val barChart = findViewById<BarChart>(R.id.barChart)
        dataset.color = R.color.black
        dataset.valueTextSize = 15F
        val data = BarData(dataset)
        barChart.xAxis.valueFormatter = MyXAxisFormatter()

        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.data = data
        barChart.legend.isEnabled = false
        barChart.description = null
        barChart.axisRight.setDrawLabels(false)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM;
        barChart.xAxis.textSize = 20F
        barChart.extraBottomOffset = 5F
        barChart.axisLeft.textSize = 20F
        barChart.axisLeft.axisMinimum = 0F

        getUsers(db).forEach() {
            if(username == it.name){
                val userTipAvg = it.avgTips!! / it.avgSales!!
                val predMon = avgMon * userTipAvg
                val predTues = avgTues * userTipAvg
                val predWed = avgWed * userTipAvg
                val predThur = avgThur * userTipAvg
                val predFri = avgFri * userTipAvg
                val predSat = avgSat * userTipAvg
                val predSun = avgSun * userTipAvg
                when(getDayOfWeek()){
                    "Monday" -> tv_prediction_num.text = "$" + String.format("%.2f", predMon)
                    "Tuesday" -> tv_prediction_num.text = "$" + String.format("%.2f", predTues)
                    "Wednesday" -> tv_prediction_num.text = "$" + String.format("%.2f", predWed)
                    "Thursday" -> tv_prediction_num.text = "$" + String.format("%.2f", predThur)
                    "Friday" -> tv_prediction_num.text = "$" + String.format("%.2f", predFri)
                    "Saturday" -> tv_prediction_num.text = "$" + String.format("%.2f", predSat)
                    "Sunday" -> tv_prediction_num.text = "$" + String.format("%.2f", predSun)
                }
            }
        }

        navigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_score -> {
                        val intent = Intent(this, ScoreActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                    R.id.nav_add -> {
                        val intent = Intent(this, AddActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                    R.id.nav_pred -> {

                    }
                }
                false
            }
    }

    fun getShifts(db: AppDatabase): List<Shifts> = runBlocking {
        db.shiftDao().getAllShifts()
    }

    fun getUsers(db: AppDatabase): List<Users> = runBlocking {
        db.userDao().getAllUsers()
    }

    private fun getDayOfWeek(): String{
        val date = Calendar.getInstance().time
        val formatted = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        return formatted.toString()
    }
}

class MyXAxisFormatter : ValueFormatter() {
    private val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}
