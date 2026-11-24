package com.techhub.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interview_sessions")
data class InterviewSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val level: String,
    val question: String,
    val userAnswer: String,
    val score: Int,
    val evaluationFeedback: String,
    val timestamp: Long = System.currentTimeMillis()
)
