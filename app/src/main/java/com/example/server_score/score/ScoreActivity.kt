package com.example.server_score.score

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import java.math.RoundingMode
import java.text.DecimalFormat


class ScoreActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

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
        toolbarTitle.text = "$username's Server Score"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        var shiftCount = 0
        var tipTotal = 0F
        var hoursTotal = 0F
        var addOnsTotal = 0F
        var checkTimeTotal = 0
        getShifts(db).forEach() {
            if (it.name == username) {
                shiftCount++
                tipTotal += it.totalTips!!
                hoursTotal += it.hours!!
                addOnsTotal += it.addOns!!
                checkTimeTotal += it.checkTime!!
            }
        }

        var userShiftCount = 0
        var userId = 0
        getUsers(db).forEach() {
            userId++
            if (it.name == username) {
                userShiftCount++
                userId = it.uid
            }
        }

        if (userShiftCount == 0) {
            val newUser = Users(
                userId + 1, username, tipTotal / 1,
                tipTotal / hoursTotal, addOnsTotal / 1, checkTimeTotal / 1
            )
            insertUser(db, newUser)
        } else {
            val updatedUser = Users(
                userId, username, tipTotal / shiftCount, tipTotal / hoursTotal,
                addOnsTotal / shiftCount, checkTimeTotal / shiftCount
            )
            updateUser(db, updatedUser)
        }

        getUsers(db).forEach() {
            if (it.name == username) {
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.CEILING
                tv_avg_tip_num.text = it.avgTips.toString()
                val hourly = df.format(it.avgHourly).toString()
                tv_wage_num.text = hourly
                tv_add_ons_num.text = it.avgAddOns.toString()
                tv_check_time_num.text = it.avgCheckTime.toString()
            }
        }


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

    fun insertUser(db: AppDatabase, newUser: Users) = runBlocking {
        db.userDao().insert(newUser)
    }

    fun updateUser(db: AppDatabase, updatedUser: Users) = runBlocking {
        db.userDao().update(updatedUser)
    }
}
