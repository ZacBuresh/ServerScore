package com.example.server_score.main

import android.widget.ArrayAdapter

interface MainInterface {
    fun sendAdapter(adapter: ArrayAdapter<String>)
    fun showToast()
}