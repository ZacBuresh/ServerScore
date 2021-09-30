package com.example.server_score.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.server_score.R
import com.example.server_score.score.ScoreActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MainInterface, AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = MainPresenter(this, this)
        presenter.startPresenter()

        spinner.adapter = adapter

        bt_start.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("USERNAME", spinner.selectedItem.toString())
            startActivity(intent)
        }

        fab_add.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.new_user_dialog)
            val submit = dialog.findViewById<Button>(R.id.bt_submit)
            val cancel = dialog.findViewById<Button>(R.id.bt_cancel)
            val newUser = dialog.findViewById<EditText>(R.id.et_user)
            submit.setOnClickListener {
                presenter.makeNewUser(newUser.text.toString())
                spinner.adapter = adapter
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        fab_delete.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.delete_user_dialog)
            val yes = dialog.findViewById<Button>(R.id.bt_yes)
            val no = dialog.findViewById<Button>(R.id.bt_no)
            yes.setOnClickListener {
                presenter.deleteUsers(spinner.selectedItem.toString())
                spinner.adapter = adapter
                dialog.dismiss()
            }
            no.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun sendAdapter(adapter: ArrayAdapter<String>) {
        this.adapter = adapter
    }

    override fun showToast() {
        Toast.makeText(this, "User Already Exists", Toast.LENGTH_LONG).show()
    }
}
