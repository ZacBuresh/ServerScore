package com.example.server_score

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.model.Users
import com.example.server_score.score.ScoreActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_user_dialog.*
import kotlinx.coroutines.runBlocking
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Server-Score-Database"
        ).build()

        getSpinnerList(db)

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
                if(!userExists(db, newUser)) {
                    val user = Users(
                        Random.nextInt(), newUser.text.toString(), null,
                        null, null, null
                    )
                    insertUser(db, user)
                }
                else{
                    Toast.makeText(this, "User Already Exists", Toast.LENGTH_LONG).show()
                }
                getSpinnerList(db)
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
                getUsers(db).forEach(){
                    if(it.name == spinner.selectedItem.toString()){
                        deleteUser(db, it)
                    }
                }
                getShifts(db).forEach(){
                    if(it.name == spinner.selectedItem.toString()){
                        deleteShift(db, it)
                    }
                }
                getSpinnerList(db)
                dialog.dismiss()
            }
            no.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun getUsers(db: AppDatabase): List<Users> = runBlocking {
        db.userDao().getAllUsers()
    }

    private fun getShifts(db: AppDatabase): List<Shifts> = runBlocking {
        db.shiftDao().getAllShifts()
    }

    private fun insertUser(db: AppDatabase, newUser: Users) = runBlocking {
        db.userDao().insert(newUser)
    }

    private fun deleteUser(db: AppDatabase, user: Users) = runBlocking{
        db.userDao().delete(user)
    }

    private fun deleteShift(db: AppDatabase, shift: Shifts) = runBlocking{
        db.shiftDao().delete(shift)
    }

    private fun getSpinnerList(db: AppDatabase){
        val userList : MutableList<String> = ArrayList()
        getUsers(db).forEach(){
            if(!spinnerUserExists(userList, it.name!!)) {
                userList.add(it.name)
            }
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this, R.layout.spinner_item, userList
        )
        spinner.adapter = adapter
    }

    private fun spinnerUserExists(userList: MutableList<String>, name: String): Boolean{
        userList.forEach{
            if(it == name){
                return true
            }
        }
        return false
    }

    private fun userExists(db: AppDatabase, newUser: EditText): Boolean{
        getUsers(db).forEach(){
            if(it.name == newUser.text.toString()){
                return true
            }
        }
        return false
    }

}
