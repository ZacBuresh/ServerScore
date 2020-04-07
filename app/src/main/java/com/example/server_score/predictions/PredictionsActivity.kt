package com.example.server_score.predictions

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.server_score.main.MainActivity
import com.example.server_score.R
import com.example.server_score.add.AddActivity
import com.example.server_score.score.ScoreActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_add.navigationView
import kotlinx.android.synthetic.main.activity_predictions.*


class PredictionsActivity : PredictionsInterface, AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var data: BarData
    lateinit var dataset: BarDataSet
    lateinit var today: String
    var predMon = 0F; var predTues = 0F; var predWed = 0F; var predThur = 0F; var predFri = 0F
    var predSat = 0F; var predSun = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predictions)

        val username = setupActivity()

        val presenter = PredictionsPresenter(this, this)
        presenter.startPresenter(username)

        customizeBarChart()

        presenter.getDayOfWeek()
        presenter.getPredictions()
        when(today){
            "Monday" -> tv_prediction_num.text = "$" + String.format("%.2f", predMon)
            "Tuesday" -> tv_prediction_num.text = "$" + String.format("%.2f", predTues)
            "Wednesday" -> tv_prediction_num.text = "$" + String.format("%.2f", predWed)
            "Thursday" -> tv_prediction_num.text = "$" + String.format("%.2f", predThur)
            "Friday" -> tv_prediction_num.text = "$" + String.format("%.2f", predFri)
            "Saturday" -> tv_prediction_num.text = "$" + String.format("%.2f", predSat)
            "Sunday" -> tv_prediction_num.text = "$" + String.format("%.2f", predSun)
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

    override fun sendData(data: BarData) {
        this.data = data
    }

    override fun sendDataSet(dataset: BarDataSet) {
        this.dataset = dataset
    }

    override fun getDayOfWeek(today: String) {
        this.today = today
    }

    override fun sendPredictions(predMon: Float, predTues: Float, predWed: Float, predThur: Float,
        predFri: Float, predSat: Float, predSun: Float
    ) {
        this.predMon = predMon
        this.predTues = predTues
        this.predWed = predWed
        this.predThur = predThur
        this.predFri = predFri
        this.predSat = predSat
        this.predSun = predSun
    }

    private fun setupActivity(): String{
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
        return username
    }

    private fun customizeBarChart(){
        val barChart = findViewById<BarChart>(R.id.barChart)
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
        dataset.color = R.color.black
        dataset.valueTextSize = 15F
    }

}

class MyXAxisFormatter : ValueFormatter() {
    private val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}
