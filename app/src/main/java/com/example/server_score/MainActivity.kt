package com.example.server_score

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.server_score.model.AppDatabase
import com.example.server_score.score.ScoreActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        bt_start.setOnClickListener {
            username = et_name.text.toString()
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

    }

}
