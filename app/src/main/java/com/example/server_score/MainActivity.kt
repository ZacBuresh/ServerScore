package com.example.server_score

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.server_score.score.ScoreActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_start.setOnClickListener {
            username = et_name.text.toString()
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

    }

}
