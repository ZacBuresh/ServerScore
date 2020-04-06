package com.example.server_score.score

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.server_score.MainActivity
import com.example.server_score.R
import com.example.server_score.add.AddActivity
import com.example.server_score.model.Users
import com.example.server_score.predictions.PredictionsActivity
import kotlinx.android.synthetic.main.activity_add.navigationView
import kotlinx.android.synthetic.main.activity_score.*
import java.lang.Integer.parseInt


class ScoreActivity : ScoreInterface, AppCompatActivity(){

    lateinit var toolbar: Toolbar
    private lateinit var updatedUser: Users

    companion object{
        const val STANDARD_HOURLY_AVG = 20.0
        const val STANDARD_ADD_ON_AVG = 5.0
        const val STANDARD_CHECK_TIME_AVG = 53
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val username = setupActivity()

        val presenter = ScorePresenter(this, this)
        presenter.startPresenter(username)

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

    override fun initializeTextViews() {
        tv_avg_tip_num.text = "0"
        tv_sales_num.text = "0"
        tv_wage_num.text = "0"
        tv_add_ons_num.text = "0"
        tv_check_time_num.text = "0"
    }

    @SuppressLint("SetTextI18n")
    override fun setTextViews(updatedUser: Users){
        System.out.println(updatedUser)
        tv_avg_tip_num.text = "$" + String.format("%.2f", updatedUser.avgTips)
        tv_sales_num.text = String.format("%.2f", ((updatedUser.avgTips?.div(updatedUser.avgSales!!))?.times(100))) + "%"
        tv_wage_num.text = "$" + String.format("%.2f", updatedUser.avgHourly)
        tv_add_ons_num.text = "$" + String.format("%.2f", updatedUser.avgAddOns)
        tv_check_time_num.text = updatedUser.avgCheckTime.toString() + " Minutes"
    }

    override fun initializeScore(){
        tv_score.text = "0"
    }

    override fun setServerScore(serverScore: String) {
        tv_score.text = serverScore
        slider.progress = parseInt(serverScore) + 20
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
        val item: MenuItem = navigationView.menu.getItem(0)
        item.isChecked = true
        slider.max = 40
        return username
    }
}
