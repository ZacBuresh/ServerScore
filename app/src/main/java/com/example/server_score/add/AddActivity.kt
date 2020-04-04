package com.example.server_score.add

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.server_score.MainActivity
import com.example.server_score.R
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.predictions.PredictionsActivity
import com.example.server_score.score.ScoreActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


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
        val item: MenuItem = navigationView.menu.getItem(1)
        item.isChecked = true

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
            if(et_total_tips.text.toString() != "" && et_hours.text.toString() != "" &&
                    et_time.text.toString() != "" && et_sales.text.toString() != "") {
                val newShift = Shifts(
                    Random.nextInt(), username, et_total_tips.text.toString().toFloat(),
                    et_sales.text.toString().toFloat(), et_hours.text.toString().toFloat(),
                    et_adds.text.toString().toFloat(), et_time.text.toString().toInt(), getDayOfWeek()
                )
                insertShift(db, newShift)
                et_total_tips.text.clear()
                et_sales.text.clear()
                et_hours.text.clear()
                et_adds.text.clear()
                et_time.text.clear()

                Toast.makeText(this, "Shift Added!", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Complete All Fields", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun insertShift(db: AppDatabase, shift: Shifts) = runBlocking {
        db.shiftDao().insert(shift)
    }

    private fun getDayOfWeek(): String{
        val date = Calendar.getInstance().time
        val formatted = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        return formatted.toString()
    }
}
