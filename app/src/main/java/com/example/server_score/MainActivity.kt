package com.example.server_score

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.server_score.add.AddActivity
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Users
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_user_dialog.*
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        getSpinnerList(db)

        bt_start.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
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
                val newUser = Users(
                    getUserCount(db) + 1, newUser.text.toString(), null,
                    null, null, null
                )
                insertUser(db, newUser)
                getSpinnerList(db)
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        fab_delete.setOnClickListener {
            val alert
        }
    }

    fun getUsers(db: AppDatabase): List<Users> = runBlocking {
        db.userDao().getAllUsers()
    }

    fun getUserCount(db: AppDatabase): Int = runBlocking{
        db.userDao().getCount()
    }

    fun insertUser(db: AppDatabase, newUser: Users) = runBlocking {
        db.userDao().insert(newUser)
    }

    fun getSpinnerList(db: AppDatabase){
        val userList : MutableList<String> = ArrayList()
        getUsers(db).forEach(){
            it.name?.let { it1 -> userList.add(it1) }
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, userList
        )
        spinner.adapter = adapter
    }

}
