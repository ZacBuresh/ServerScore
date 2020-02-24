package com.example.server_score.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.server_score.MainActivity
import com.example.server_score.R
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.predictions.PredictionsActivity
import com.example.server_score.score.ScoreActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.runBlocking
import java.util.*

class AddActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

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

        navigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_score -> {
                        val intent = Intent(this, ScoreActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                    R.id.nav_add -> {

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

        bt_add.setOnClickListener {
            val newShift = Shifts(getShifts(db) + 1, username, et_total_tips.text.toString().toFloat(),
                et_hours.text.toString().toFloat(), et_guests.text.toString().toInt(),
                et_adds.text.toString().toFloat(), et_time.text.toString().toInt())
            insertShift(db, newShift)
            et_total_tips.text.clear()
            et_hours.text.clear()
            et_guests.text.clear()
            et_adds.text.clear()
            et_time.text.clear()
        }

    }

    fun insertShift(db: AppDatabase, shift: Shifts) = runBlocking {
        db.shiftDao().insert(shift)
    }

    fun getShifts(db: AppDatabase): Int = runBlocking{
        db.shiftDao().getAllShifts().count()
    }
}
