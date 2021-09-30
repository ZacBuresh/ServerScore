package com.example.server_score.main

import android.content.Context
import android.widget.ArrayAdapter
import androidx.room.Room
import com.example.server_score.R
import com.example.server_score.model.AppDatabase
import com.example.server_score.model.Shifts
import com.example.server_score.model.Users
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainPresenter(val v: MainInterface, val context: Context) {

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "Server-Score-Database"
    ).fallbackToDestructiveMigration().build()

    fun startPresenter() {
        getSpinnerList(db)
    }

    private fun getSpinnerList(db: AppDatabase){
        val userList : MutableList<String> = ArrayList()
        getUsers(db).forEach(){
            if(!spinnerUserExists(userList, it.name!!)) {
                userList.add(it.name)
            }
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context, R.layout.spinner_item, userList
        )
        v.sendAdapter(adapter)
    }

    fun makeNewUser(newUser: String) {
        if(!userExists(db, newUser)) {
            val user = Users(
                Random.nextInt(), newUser, null,
                null, null, null, null
            )
            insertUser(db, user)
        }
        else{
            v.showToast()
        }
        getSpinnerList(db)
    }

    fun deleteUsers(username: String) {
        getUsers(db).forEach(){
            if(it.name == username){
                deleteUser(db, it)
            }
        }
        getShifts(db).forEach(){
            if(it.name == username){
                deleteShift(db, it)
            }
        }
        getSpinnerList(db)
    }

    private fun spinnerUserExists(userList: MutableList<String>, name: String): Boolean{
        userList.forEach{
            if(it == name){
                return true
            }
        }
        return false
    }

    private fun userExists(db: AppDatabase, newUser: String): Boolean{
        getUsers(db).forEach(){
            if(it.name == newUser){
                return true
            }
        }
        return false
    }

    private fun getUsers(db: AppDatabase): List<Users> = runBlocking {
        db.userDao().getAllUsers()
    }

    private fun insertUser(db: AppDatabase, newUser: Users) = runBlocking {
        db.userDao().insert(newUser)
    }

    private fun getShifts(db: AppDatabase): List<Shifts> = runBlocking {
        db.shiftDao().getAllShifts()
    }

    private fun deleteUser(db: AppDatabase, user: Users) = runBlocking{
        db.userDao().delete(user)
    }

    private fun deleteShift(db: AppDatabase, shift: Shifts) = runBlocking{
        db.shiftDao().delete(shift)
    }
}