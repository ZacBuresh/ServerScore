package com.example.server_score.score

import com.example.server_score.model.Users

interface ScoreInterface {
    fun initializeTextViews()
    fun setTextViews(updatedUser: Users)
    fun initializeScore()
    fun setServerScore(serverScore: String)
}