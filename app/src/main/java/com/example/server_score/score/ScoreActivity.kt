package com.example.server_score.score

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
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
import com.example.server_score.predictions.PredictionsActivity
import kotlinx.android.synthetic.main.activity_add.navigationView
import kotlinx.android.synthetic.main.activity_score.*
import kotlinx.coroutines.runBlocking
import java.lang.Integer.parseInt
import java.math.RoundingMode
import java.text.DecimalFormat


class ScoreActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    companion object{
        const val STANDARD_HOURLY_AVG = 20.0
        const val STANDARD_ADD_ON_AVG = 5.0
        const val STANDARD_CHECK_TIME_AVG = 53
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

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
        val item: MenuItem = navigationView.menu.getItem(0)
        item.isChecked = true

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        var shiftCount = 0
        var tipTotal = 0F
        var salesTotal = 0F
        var hoursTotal = 0F
        var addOnsTotal = 0F
        var checkTimeTotal = 0
        getShifts(db).forEach() {
            if (it.name == username) {
                shiftCount++
                tipTotal += it.totalTips!!
                salesTotal += it.totalSales!!
                hoursTotal += it.hours!!
                addOnsTotal += it.addOns!!
                checkTimeTotal += it.checkTime!!
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
                    tv_avg_tip_num.text = "0"
                    tv_sales_num.text = "0"
                    tv_wage_num.text = "0"
                    tv_add_ons_num.text = "0"
                    tv_check_time_num.text = "0"
                } else {
                    val updatedUser = Users(
                        it.uid, username, tipTotal / shiftCount, salesTotal / shiftCount,
                        tipTotal / hoursTotal, addOnsTotal / shiftCount,
                        checkTimeTotal / shiftCount
                    )
                    updateUser(db, updatedUser)
                    tv_avg_tip_num.text = "$" + String.format("%.2f", updatedUser.avgTips)
                    tv_sales_num.text = String.format("%.2f", ((updatedUser.avgTips?.div(updatedUser.avgSales!!))?.times(100))) + "%"
                    tv_wage_num.text = "$" + String.format("%.2f", updatedUser.avgHourly)
                    tv_add_ons_num.text = "$" + String.format("%.2f", updatedUser.avgAddOns)
                    tv_check_time_num.text = updatedUser.avgCheckTime.toString() + " Minutes"
                }
            }
        }

        calculateServerScore(db, username)

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_score -> {

                }
                R.id.nav_add -> {
                    val intent = Intent(this, AddActivity::class.java)
                    intent.putExtra("USERNAME", username)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                R.id.nav_pred -> {
                    val intent = Intent(this, PredictionsActivity::class.java)
                    intent.putExtra("USERNAME", username)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
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

    fun updateUser(db: AppDatabase, updatedUser: Users) = runBlocking {
        db.userDao().update(updatedUser)
    }

    fun calculateServerScore(db: AppDatabase, username: String){
        getUsers(db).forEach(){
            if(it.name == username){
                if(it.avgHourly == null || it.avgCheckTime == null || it.avgAddOns == null || it.avgTips == null){
                    tv_score.text = "0"
                }
                else {
                    val hourlyResult = it.avgHourly - STANDARD_HOURLY_AVG
                    val addonResult = it.avgAddOns - STANDARD_ADD_ON_AVG
                    val checktimeResult = STANDARD_CHECK_TIME_AVG - it.avgCheckTime
                    val serverScore =
                        String.format("%.0f", hourlyResult + addonResult + checktimeResult)
                    tv_score.text = serverScore
                    slider.progress = parseInt(serverScore) + 50
                }
            }
        }
    }
}
