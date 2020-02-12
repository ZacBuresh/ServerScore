package com.example.server_score.predictions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.server_score.R
import com.example.server_score.add.AddActivity
import com.example.server_score.score.ScoreActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_add.*

class PredictionsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predictions)

        toolbar = findViewById(R.id.toolbar_score)
        setSupportActionBar(toolbar)
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
        val username = intent.getStringExtra("USERNAME")
        toolbarTitle.text = username

        navigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_score -> {
                        val intent = Intent(this, ScoreActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent)
                    }
                    R.id.nav_add -> {
                        val intent = Intent(this, AddActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent)
                    }
                    R.id.nav_pred -> {

                    }
                }
                false
            }
    }
}
